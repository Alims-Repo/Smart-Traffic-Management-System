"""
Device optimization for M1 Pro
Author: Alims-Repo
Date: 2025-06-17
"""

import time
from typing import List, Tuple
import numpy as np
import torch
from ultralytics import YOLO

from config.settings import get_config
from src.utils.logging_config import get_logger
from src.utils.helpers import timer

config = get_config()
logger = get_logger("device_optimizer")


class DeviceOptimizer:
    """Handles device selection and optimization for M1 Pro"""
    
    @staticmethod
    def setup_mps() -> bool:
        """Configure MPS if available"""
        if not torch.backends.mps.is_available():
            logger.info("MPS not available on this system")
            return False
        
        try:
            torch.mps.set_per_process_memory_fraction(config.MPS_MEMORY_FRACTION)
            torch.mps.empty_cache()
            logger.info("✓ MPS optimizations applied")
            return True
        except Exception as e:
            logger.error(f"✗ MPS setup failed: {e}")
            return False
    
    @staticmethod
    @timer
    def benchmark_device(model: YOLO, device: str, test_frames: List[np.ndarray]) -> float:
        """
        Benchmark model performance on specific device
        
        Args:
            model: YOLO model instance
            device: Device to test ('cpu' or 'mps')
            test_frames: List of test frames
            
        Returns:
            Average time per frame
        """
        logger.info(f"🔍 Benchmarking {device.upper()}...")
        
        try:
            model.to(device)
            
            if device == "mps":
                torch.mps.empty_cache()
                # Warmup run
                _ = model.predict(
                    test_frames[0], 
                    verbose=False, 
                    device=device, 
                    half=False,
                    imgsz=config.INPUT_SIZE
                )
                torch.mps.synchronize()
            
            # Actual benchmark
            start_time = time.time()
            for frame in test_frames:
                _ = model.predict(
                    frame,
                    verbose=False,
                    device=device,
                    half=False,
                    imgsz=config.INPUT_SIZE,
                    conf=config.CONFIDENCE_THRESHOLD,
                    iou=config.IOU_THRESHOLD,
                    max_det=config.MAX_DETECTIONS
                )
            
            if device == "mps":
                torch.mps.synchronize()
            
            total_time = time.time() - start_time
            avg_time = total_time / len(test_frames)
            
            logger.info(f"📊 {device.upper()}: {avg_time:.3f}s/frame ({total_time:.3f}s total)")
            return avg_time
            
        except Exception as e:
            logger.error(f"✗ {device} benchmark failed: {e}")
            return float('inf')
    
    @classmethod
    def find_optimal_config(cls) -> Tuple[YOLO, str]:
        """
        Find the best model and device combination
        
        Returns:
            Tuple of (best_model, best_device)
        """
        logger.info("🚀 Starting device optimization...")
        
        # Setup MPS if available
        mps_available = cls.setup_mps()
        
        # Generate test data
        test_frames = [
            np.random.randint(0, 255, (config.INPUT_SIZE, config.INPUT_SIZE, 3), dtype=np.uint8)
            for _ in range(4)
        ]
        logger.info(f"📋 Generated {len(test_frames)} test frames")
        
        best_model = None
        best_device = "cpu"
        best_time = float('inf')
        results = []
        
        # Test each available model
        for model_path in config.MODEL_PATHS:
            if not model_path.exists():
                logger.warning(f"⚠️  Model not found: {model_path}")
                continue
            
            model_name = model_path.name
            logger.info(f"🧠 Testing model: {model_name}")
            
            try:
                model = YOLO(str(model_path))
                
                # Test CPU
                cpu_time = cls.benchmark_device(model, "cpu", test_frames)
                
                # Test MPS if available
                mps_time = float('inf')
                if mps_available:
                    mps_time = cls.benchmark_device(model, "mps", test_frames)
                
                # Record results
                result = {
                    "model": model_name,
                    "cpu_time": cpu_time,
                    "mps_time": mps_time,
                    "cpu_fps": 1.0 / cpu_time if cpu_time > 0 else 0,
                    "mps_fps": 1.0 / mps_time if mps_time > 0 and mps_time != float('inf') else 0
                }
                results.append(result)
                
                logger.info(f"📊 {model_name} results:")
                logger.info(f"   CPU: {cpu_time:.3f}s ({result['cpu_fps']:.1f} FPS)")
                if mps_available:
                    logger.info(f"   MPS: {mps_time:.3f}s ({result['mps_fps']:.1f} FPS)")
                
                # Update best configuration
                if cpu_time < best_time:
                    best_time = cpu_time
                    best_device = "cpu"
                    best_model = model
                    logger.info(f"🏆 New best: {model_name} on CPU")
                
                if mps_time < best_time:
                    best_time = mps_time
                    best_device = "mps"
                    best_model = model
                    logger.info(f"🏆 New best: {model_name} on MPS")
                
            except Exception as e:
                logger.error(f"✗ Failed to test {model_name}: {e}")
        
        # Fallback to default if no models found
        if best_model is None:
            logger.warning("⚠️  No models found, using fallback")
            best_model = YOLO("yolov8n.pt")  # This will download if not present
            best_device = "cpu"
            best_time = float('inf')
        
        # Move model to selected device
        best_model.to(best_device)
        
        # Log final selection
        logger.info("🎯 Final Selection:")
        logger.info(f"   Model: {best_model.ckpt_path if hasattr(best_model, 'ckpt_path') else 'yolov8n.pt'}")
        logger.info(f"   Device: {best_device.upper()}")
        logger.info(f"   Performance: {best_time:.3f}s/frame ({1.0/best_time:.1f} FPS)")
        
        # Log all results for comparison
        if results:
            logger.info("📊 Complete benchmark results:")
            for result in results:
                logger.info(f"   {result['model']}: CPU={result['cpu_fps']:.1f}fps, MPS={result['mps_fps']:.1f}fps")
        
        return best_model, best_device
    
    @staticmethod
    def get_device_info() -> dict:
        """Get system device information"""
        info = {
            "mps_available": torch.backends.mps.is_available(),
            "torch_version": torch.__version__,
            "device_count": 1 if torch.backends.mps.is_available() else 0
        }
        
        if torch.backends.mps.is_available():
            info["mps_memory_fraction"] = config.MPS_MEMORY_FRACTION
        
        return info