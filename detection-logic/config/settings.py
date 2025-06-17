"""
Configuration settings for M1 Vehicle Detection Server
Author: Alims-Repo
Date: 2025-06-17
"""

import os
from pathlib import Path

# Base paths
BASE_DIR = Path(__file__).parent.parent
MODEL_DIR = BASE_DIR / "model"
RESOURCES_DIR = BASE_DIR / "resources"
LOGS_DIR = BASE_DIR / "logs"

# Ensure directories exist
MODEL_DIR.mkdir(exist_ok=True)
RESOURCES_DIR.mkdir(exist_ok=True)
LOGS_DIR.mkdir(exist_ok=True)


class Config:
    """Main configuration class"""
    
    # Server settings
    HOST = os.getenv("HOST", "0.0.0.0")
    PORT = int(os.getenv("PORT", 1234))
    DEBUG = os.getenv("DEBUG", "false").lower() == "true"
    
    # Video settings
    VIDEO_PATH = RESOURCES_DIR / "1.mp4"
    DEFAULT_VIDEO_URL = "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_1mb.mp4"
    
    # Model settings
    MODEL_PATHS = [
        MODEL_DIR / "yolov8n.pt",
        MODEL_DIR / "yolov8s.pt",
        MODEL_DIR / "yolov8m.pt"
    ]
    VEHICLE_CLASSES = ["car", "truck", "bus", "motorcycle", "bicycle"]
    
    # Detection parameters
    INPUT_SIZE = 640
    CONFIDENCE_THRESHOLD = float(os.getenv("CONFIDENCE", 0.35))
    IOU_THRESHOLD = 0.45
    MAX_DETECTIONS = 200
    
    # Performance settings
    MPS_MEMORY_FRACTION = 0.8
    BATCH_SIZE_GPU = int(os.getenv("BATCH_SIZE_GPU", 2))
    BATCH_SIZE_CPU = 1
    TARGET_FPS = int(os.getenv("FPS", 30))
    STATS_INTERVAL = 5.0
    
    # Encoding settings
    JPEG_QUALITY = int(os.getenv("JPEG_QUALITY", 80))
    
    # Logging
    LOG_LEVEL = os.getenv("LOG_LEVEL", "INFO")
    LOG_FILE = LOGS_DIR / "vehicle_detection.log"
    
    # WebSocket settings
    WS_HEARTBEAT = 30
    WS_TIMEOUT = 60


class DevelopmentConfig(Config):
    """Development configuration"""
    DEBUG = True
    LOG_LEVEL = "DEBUG"
    TARGET_FPS = 15  # Lower FPS for development


class ProductionConfig(Config):
    """Production configuration"""
    DEBUG = False
    LOG_LEVEL = "WARNING"
    TARGET_FPS = 30


# Configuration selection
config_map = {
    "development": DevelopmentConfig,
    "production": ProductionConfig,
    "default": Config
}

def get_config():
    """Get configuration based on environment"""
    env = os.getenv("ENVIRONMENT", "default")
    return config_map.get(env, Config)