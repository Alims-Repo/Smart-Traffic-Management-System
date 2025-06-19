"""
Web request handlers
Author: Alims-Repo
Date: 2025-06-17
"""

import json
import time
from typing import Dict, Any
import aiohttp
from aiohttp import web

from config.settings import get_config
from src.utils.logging_config import get_logger

config = get_config()
logger = get_logger("handlers")


class WebSocketHandlers:
    """WebSocket message handlers"""
    
    def __init__(self, broadcaster, detector, device):
        self.broadcaster = broadcaster
        self.detector = detector
        self.device = device
    
    async def handle_get_stats(self, ws) -> Dict[str, Any]:
        """Handle get_stats command"""
        if not self.broadcaster:
            return {"error": "Broadcaster not available"}
        
        stats = self.broadcaster.get_stats()
        return {
            "command": "stats_response",
            "data": stats,
            "timestamp": time.time()
        }
    
    async def handle_get_system_info(self, ws) -> Dict[str, Any]:
        """Handle get_system_info command"""
        import torch
        
        return {
            "command": "system_info_response",
            "data": {
                "device": self.device,
                "mps_available": torch.backends.mps.is_available(),
                "vehicle_classes": config.VEHICLE_CLASSES,
                "input_size": config.INPUT_SIZE,
                "confidence_threshold": config.CONFIDENCE_THRESHOLD,
                "target_fps": config.TARGET_FPS,
                "version": "1.0.0",
                "author": "Alims-Repo"
            },
            "timestamp": time.time()
        }
    
    async def handle_reset_stats(self, ws) -> Dict[str, Any]:
        """Handle reset_stats command"""
        if self.detector:
            self.detector.reset_performance_stats()
        
        return {
            "command": "reset_response",
            "data": {"message": "Performance stats reset"},
            "timestamp": time.time()
        }
    
    async def handle_get_clients(self, ws) -> Dict[str, Any]:
        """Handle get_clients command"""
        if not self.broadcaster:
            return {"error": "Broadcaster not available"}
        
        return {
            "command": "clients_response",
            "data": {
                "connected_clients": len(self.broadcaster.clients),
                "current_client": str(ws),
                "client_list": [str(client) for client in self.broadcaster.clients]
            },
            "timestamp": time.time()
        }


class HTTPHandlers:
    """HTTP request handlers"""
    
    def __init__(self, broadcaster, detector, device):
        self.broadcaster = broadcaster
        self.detector = detector
        self.device = device
    
    async def health_check(self, request) -> web.Response:
        """Health check endpoint"""
        import torch
        
        health_data = {
            "status": "healthy" if self.broadcaster and self.broadcaster.is_running else "unhealthy",
            "device": self.device,
            "mps_available": torch.backends.mps.is_available(),
            "detection_enabled": self.detector is not None,
            "clients": len(self.broadcaster.clients) if self.broadcaster else 0,
            "timestamp": time.time(),
            "version": "1.0.0",
            "uptime": time.time()  # Could track actual uptime
        }
        
        status_code = 200 if health_data["status"] == "healthy" else 503
        return web.json_response(health_data, status=status_code)
    
    async def get_stats(self, request) -> web.Response:
        """Statistics endpoint"""
        if not self.broadcaster:
            return web.json_response(
                {"error": "Broadcaster not initialized"}, 
                status=503
            )
        
        stats = self.broadcaster.get_stats()
        return web.json_response(stats)
    
    async def get_config(self, request) -> web.Response:
        """Configuration endpoint"""
        config_data = {
            "vehicle_classes": config.VEHICLE_CLASSES,
            "input_size": config.INPUT_SIZE,
            "confidence_threshold": config.CONFIDENCE_THRESHOLD,
            "iou_threshold": config.IOU_THRESHOLD,
            "max_detections": config.MAX_DETECTIONS,
            "target_fps": config.TARGET_FPS,
            "jpeg_quality": config.JPEG_QUALITY,
            "batch_size_gpu": config.BATCH_SIZE_GPU,
            "batch_size_cpu": config.BATCH_SIZE_CPU,
            "mps_memory_fraction": config.MPS_MEMORY_FRACTION
        }
        
        return web.json_response(config_data)
    
    async def update_config(self, request) -> web.Response:
        """Update configuration endpoint"""
        try:
            data = await request.json()
            
            # Validate and update specific config values
            updated = {}
            
            if "confidence_threshold" in data:
                confidence = float(data["confidence_threshold"])
                if 0.1 <= confidence <= 0.9:
                    config.CONFIDENCE_THRESHOLD = confidence
                    updated["confidence_threshold"] = confidence
            
            if "target_fps" in data:
                fps = int(data["target_fps"])
                if 1 <= fps <= 60:
                    config.TARGET_FPS = fps
                    updated["target_fps"] = fps
            
            if "jpeg_quality" in data:
                quality = int(data["jpeg_quality"])
                if 10 <= quality <= 100:
                    config.JPEG_QUALITY = quality
                    updated["jpeg_quality"] = quality
            
            logger.info(f"⚙️  Configuration updated: {updated}")
            
            return web.json_response({
                "message": "Configuration updated",
                "updated": updated,
                "timestamp": time.time()
            })
            
        except Exception as e:
            logger.error(f"❌ Config update failed: {e}")
            return web.json_response(
                {"error": str(e)}, 
                status=400
            )
    
    async def get_performance(self, request) -> web.Response:
        """Detailed performance metrics endpoint"""
        if not self.detector:
            return web.json_response(
                {"error": "Detector not available"}, 
                status=503
            )
        
        performance_data = self.detector.get_performance_stats()
        
        if self.broadcaster:
            performance_data["broadcast"] = self.broadcaster.broadcast_tracker.get_stats()
        
        return web.json_response(performance_data)
    
    async def api_info(self, request) -> web.Response:
        """API information endpoint"""
        api_info = {
            "name": "M1 Vehicle Detection Server",
            "version": "1.0.0",
            "author": "Alims-Repo",
            "description": "Real-time vehicle detection optimized for M1 Pro",
            "endpoints": {
                "GET /health": "Health check",
                "GET /stats": "Performance statistics", 
                "GET /config": "Current configuration",
                "POST /config": "Update configuration",
                "GET /performance": "Detailed performance metrics",
                "GET /api": "API information",
                "WS /ws": "WebSocket for real-time streaming"
            },
            "websocket_commands": {
                "get_stats": "Get current statistics",
                "get_system_info": "Get system information", 
                "reset_stats": "Reset performance statistics",
                "get_clients": "Get connected clients info"
            },
            "supported_vehicles": config.VEHICLE_CLASSES,
            "timestamp": time.time()
        }
        
        return web.json_response(api_info)