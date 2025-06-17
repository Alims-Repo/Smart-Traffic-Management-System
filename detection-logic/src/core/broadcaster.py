"""
Video broadcasting and streaming logic
Author: Alims-Repo
Date: 2025-06-17
"""

import asyncio
import base64
import json
import time
from typing import List, Tuple, Optional, Set
import cv2
import numpy as np

from config.settings import get_config
from src.utils.logging_config import get_logger
from src.utils.helpers import async_timer, PerformanceTracker
from src.core.detector import VehicleDetector

config = get_config()
logger = get_logger("broadcaster")


class VideoBroadcaster:
    """Handles video streaming and broadcasting to WebSocket clients"""
    
    def __init__(self, video_path: str, detector: VehicleDetector, device: str):
        self.video_path = video_path
        self.detector = detector
        self.device = device
        
        # Video capture setup
        self.cap = cv2.VideoCapture(str(video_path))
        if not self.cap.isOpened():
            raise RuntimeError(f"Failed to open video: {video_path}")
        
        self._configure_capture()
        
        # State management
        self.vehicle_count = 0
        self.latest_frame = None
        self.frame_count = 0
        self.is_running = False
        
        # Batching for GPU optimization
        self.batch_size = config.BATCH_SIZE_GPU if device == "mps" else config.BATCH_SIZE_CPU
        self.frame_buffer = []
        
        # Client management
        self.clients: Set = set()
        self.lock = asyncio.Lock()
        
        # Performance tracking
        self.broadcast_tracker = PerformanceTracker()
        
        # Task management
        self.broadcast_task: Optional[asyncio.Task] = None
        
        logger.info(f"🎥 Broadcaster initialized: {video_path}")
        logger.info(f"📊 Batch size: {self.batch_size}")
    
    def _configure_capture(self):
        """Configure video capture for optimal performance"""
        self.cap.set(cv2.CAP_PROP_BUFFERSIZE, 1)
        
        # Get video properties
        fps = self.cap.get(cv2.CAP_PROP_FPS)
        frame_count = int(self.cap.get(cv2.CAP_PROP_FRAME_COUNT))
        width = int(self.cap.get(cv2.CAP_PROP_FRAME_WIDTH))
        height = int(self.cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
        
        logger.info(f"📹 Video properties: {width}x{height}, {fps:.1f}fps, {frame_count} frames")
    
    @async_timer
    async def read_frame(self) -> Tuple[bool, Optional[np.ndarray]]:
        """
        Async frame reading to avoid blocking
        
        Returns:
            Tuple of (success, frame)
        """
        loop = asyncio.get_event_loop()
        ret, frame = await loop.run_in_executor(None, self.cap.read)
        return ret, frame
    
    async def process_frame_batch(self, frames: List[np.ndarray]) -> Tuple[List[np.ndarray], List[int]]:
        """
        Process a batch of frames
        
        Args:
            frames: List of frames to process
            
        Returns:
            Tuple of (processed_frames, vehicle_counts)
        """
        # Run detection in thread pool for CPU-bound work
        loop = asyncio.get_event_loop()
        return await loop.run_in_executor(None, self.detector.detect_vehicles, frames)
    
    @async_timer
    async def broadcast_frame(self):
        """Broadcast current frame to all connected clients"""
        if not self.clients or self.latest_frame is None:
            return
        
        # Encode frame
        encode_params = [int(cv2.IMWRITE_JPEG_QUALITY), config.JPEG_QUALITY]
        _, buffer = cv2.imencode('.jpg', self.latest_frame, encode_params)
        image_base64 = base64.b64encode(buffer).decode('utf-8')
        
        # Create message
        message = json.dumps({
            "image": image_base64,
            "vehicleCount": self.vehicle_count,
            "timestamp": time.time(),
            "device": self.device,
            "frameCount": self.frame_count,
            "performance": {
                "avgDetectionTime": self.detector.avg_detection_time,
                "detectionFps": self.detector.detection_fps,
                "broadcastFps": self.broadcast_tracker.fps
            },
            "metadata": {
                "batchSize": self.batch_size,
                "clients": len(self.clients),
                "m1ProOptimized": True
            }
        })
        
        # Broadcast to all clients
        await self._send_to_all_clients(message)
    
    async def _send_to_all_clients(self, message: str):
        """Send message to all connected clients"""
        if not self.clients:
            return
        
        async with self.lock:
            disconnected = set()
            
            # Send to all clients concurrently
            send_tasks = []
            for client in self.clients:
                task = asyncio.create_task(self._send_to_client(client, message))
                send_tasks.append((client, task))
            
            # Wait for all sends to complete
            for client, task in send_tasks:
                try:
                    await task
                except Exception as e:
                    logger.warning(f"🔌 Client send failed: {e}")
                    disconnected.add(client)
            
            # Remove disconnected clients
            if disconnected:
                self.clients -= disconnected
                logger.info(f"🔌 Removed {len(disconnected)} disconnected clients")
    
    async def _send_to_client(self, client, message: str):
        """Send message to a single client"""
        try:
            await client.send_str(message)
        except Exception as e:
            logger.debug(f"Failed to send to client: {e}")
            raise
    
    async def broadcast_loop(self):
        """Main broadcasting loop"""
        logger.info("🚀 Starting broadcast loop")
        self.is_running = True
        last_stats = time.time()
        
        try:
            while self.is_running:
                loop_start = time.time()
                
                # Read frame
                ret, frame = await self.read_frame()
                if not ret:
                    # Loop video
                    logger.debug("📹 Looping video")
                    self.cap.set(cv2.CAP_PROP_POS_FRAMES, 0)
                    continue
                
                self.frame_count += 1
                
                # Handle batching for GPU optimization
                if self.device == "mps" and len(self.frame_buffer) < self.batch_size:
                    self.frame_buffer.append(frame)
                    if len(self.frame_buffer) < self.batch_size:
                        continue
                    
                    # Process batch
                    processed_frames, counts = await self.process_frame_batch(self.frame_buffer)
                    self.latest_frame = processed_frames[-1]
                    self.vehicle_count = counts[-1]
                    self.frame_buffer = []
                else:
                    # Single frame processing
                    processed_frames, counts = await self.process_frame_batch([frame])
                    self.latest_frame = processed_frames[0]
                    self.vehicle_count = counts[0]
                
                # Broadcast to clients
                await self.broadcast_frame()
                
                # Track broadcast performance
                loop_time = time.time() - loop_start
                self.broadcast_tracker.add_sample(loop_time)
                
                # Print stats periodically
                if time.time() - last_stats > config.STATS_INTERVAL:
                    await self.print_stats()
                    last_stats = time.time()
                
                # Frame rate control
                target_delay = 1.0 / config.TARGET_FPS
                actual_delay = max(0, target_delay - loop_time)
                if actual_delay > 0:
                    await asyncio.sleep(actual_delay)
                
        except Exception as e:
            logger.error(f"🚨 Broadcast loop error: {e}")
        finally:
            self.is_running = False
            logger.info("🛑 Broadcast loop stopped")
    
    async def print_stats(self):
        """Print performance statistics"""
        detection_stats = self.detector.get_performance_stats()
        
        logger.info("📊 Performance Stats:")
        logger.info(f"   Device: {self.device.upper()}")
        logger.info(f"   Frames processed: {self.frame_count}")
        logger.info(f"   Current vehicles: {self.vehicle_count}")
        logger.info(f"   Connected clients: {len(self.clients)}")
        logger.info(f"   Detection FPS: {detection_stats.get('fps', 0):.1f}")
        logger.info(f"   Broadcast FPS: {self.broadcast_tracker.fps:.1f}")
        logger.info(f"   Avg detection time: {detection_stats.get('avg_time', 0):.3f}s")
        logger.info(f"   Avg vehicles/frame: {detection_stats.get('avg_count', 0):.1f}")
    
    def add_client(self, websocket):
        """Add WebSocket client"""
        self.clients.add(websocket)
        logger.info(f"🔌 Client connected (total: {len(self.clients)})")
    
    def remove_client(self, websocket):
        """Remove WebSocket client"""
        self.clients.discard(websocket)
        logger.info(f"🔌 Client disconnected (total: {len(self.clients)})")
    
    def start(self):
        """Start broadcasting"""
        if self.broadcast_task is None or self.broadcast_task.done():
            self.broadcast_task = asyncio.create_task(self.broadcast_loop())
            logger.info("▶️  Broadcasting started")
    
    async def stop(self):
        """Stop broadcasting"""
        logger.info("⏹️  Stopping broadcaster...")
        self.is_running = False
        
        if self.broadcast_task and not self.broadcast_task.done():
            self.broadcast_task.cancel()
            try:
                await self.broadcast_task
            except asyncio.CancelledError:
                pass
        
        # Close all client connections
        async with self.lock:
            for client in list(self.clients):
                try:
                    await client.close(code=1000, message="Server shutdown")
                except:
                    pass
            self.clients.clear()
        
        # Release video capture
        if self.cap:
            self.cap.release()
        
        logger.info("✅ Broadcaster stopped")
    
    def get_stats(self) -> dict:
        """Get comprehensive statistics"""
        detection_stats = self.detector.get_performance_stats()
        broadcast_stats = self.broadcast_tracker.get_stats()
        
        return {
            "video": {
                "path": str(self.video_path),
                "frame_count": self.frame_count,
                "current_vehicles": self.vehicle_count,
                "is_running": self.is_running
            },
            "clients": {
                "connected": len(self.clients),
                "total_connected": len(self.clients)  # Could track historical
            },
            "performance": {
                "detection": detection_stats,
                "broadcast": broadcast_stats,
                "device": self.device,
                "batch_size": self.batch_size
            },
            "configuration": {
                "target_fps": config.TARGET_FPS,
                "jpeg_quality": config.JPEG_QUALITY,
                "input_size": config.INPUT_SIZE,
                "confidence_threshold": config.CONFIDENCE_THRESHOLD
            }
        }