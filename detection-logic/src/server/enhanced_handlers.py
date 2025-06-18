"""
Enhanced handlers with complete control functionality
Author: Alims-Repo
Date: 2025-06-17
"""

import cv2
import torch
from aiohttp import web
import json
import asyncio
from config.settings import get_config
from src.utils.logging_config import get_logger

config = get_config()
logger = get_logger("enhanced_handlers")

class EnhancedHTTPHandlers:
    """Enhanced HTTP handlers with full control capabilities"""

    def __init__(self, broadcaster, detector, device):
        self.broadcaster = broadcaster
        self.detector = detector
        self.device = device
        self._paused = False

    async def control_playback(self, request) -> web.Response:
        """Control video playback"""
        try:
            data = await request.json()
            action = data.get("action")

            logger.info(f"Playback control: {action}")

            if action == "pause":
                if self.broadcaster:
                    self.broadcaster.is_running = False
                    self._paused = True
                return web.json_response({
                    "status": "paused",
                    "message": "Video playback paused"
                })

            elif action == "resume":
                if self.broadcaster and self._paused:
                    self.broadcaster.is_running = True
                    self._paused = False
                return web.json_response({
                    "status": "resumed",
                    "message": "Video playback resumed"
                })

            elif action == "restart":
                if self.broadcaster and self.broadcaster.cap:
                    self.broadcaster.cap.set(cv2.CAP_PROP_POS_FRAMES, 0)
                    self.broadcaster.frame_count = 0
                    if self._paused:
                        self.broadcaster.is_running = True
                        self._paused = False
                return web.json_response({
                    "status": "restarted",
                    "message": "Video restarted from beginning"
                })

            elif action == "seek":
                frame_number = data.get("frame", 0)
                if self.broadcaster and self.broadcaster.cap:
                    self.broadcaster.cap.set(cv2.CAP_PROP_POS_FRAMES, frame_number)
                    self.broadcaster.frame_count = frame_number
                return web.json_response({
                    "status": f"seeked to frame {frame_number}",
                    "message": f"Video seeked to frame {frame_number}"
                })

            else:
                return web.json_response(
                    {"error": "Invalid action. Use: pause, resume, restart, seek"},
                    status=400
                )

        except Exception as e:
            logger.error(f"Playback control error: {e}")
            return web.json_response({"error": str(e)}, status=500)

    async def update_detection_settings(self, request) -> web.Response:
        """Update detection parameters in real-time"""
        try:
            data = await request.json()
            updated = {}

            logger.info(f"Updating detection settings: {data}")

            # Vehicle classes filter
            if "vehicle_classes" in data:
                new_classes = data["vehicle_classes"]
                if isinstance(new_classes, list):
                    config.VEHICLE_CLASSES = new_classes
                    updated["vehicle_classes"] = new_classes

            # Detection thresholds
            if "confidence_threshold" in data:
                conf = float(data["confidence_threshold"])
                if 0.1 <= conf <= 0.9:
                    config.CONFIDENCE_THRESHOLD = conf
                    updated["confidence_threshold"] = conf

            if "iou_threshold" in data:
                iou = float(data["iou_threshold"])
                if 0.1 <= iou <= 0.9:
                    config.IOU_THRESHOLD = iou
                    updated["iou_threshold"] = iou

            if "max_detections" in data:
                max_det = int(data["max_detections"])
                if 10 <= max_det <= 1000:
                    config.MAX_DETECTIONS = max_det
                    updated["max_detections"] = max_det

            logger.info(f"Detection settings updated: {updated}")

            return web.json_response({
                "status": "success",
                "message": "Detection settings updated",
                "updated": updated,
                "timestamp": asyncio.get_event_loop().time()
            })

        except Exception as e:
            logger.error(f"Detection settings update error: {e}")
            return web.json_response({"error": str(e)}, status=400)

    async def control_broadcast(self, request) -> web.Response:
        """Control broadcasting to clients"""
        try:
            data = await request.json()
            action = data.get("action")

            logger.info(f"Broadcast control: {action}")

            if action == "start" and self.broadcaster:
                if not self.broadcaster.is_running:
                    self.broadcaster.start()
                return web.json_response({
                    "status": "broadcasting started",
                    "message": "Video broadcasting started"
                })

            elif action == "stop" and self.broadcaster:
                self.broadcaster.is_running = False
                return web.json_response({
                    "status": "broadcasting stopped",
                    "message": "Video broadcasting stopped"
                })

            elif action == "disconnect_all" and self.broadcaster:
                # Disconnect all clients
                disconnected_count = len(self.broadcaster.clients)
                for client in list(self.broadcaster.clients):
                    try:
                        await client.close(code=1000, message="Disconnected by admin")
                    except:
                        pass
                self.broadcaster.clients.clear()

                return web.json_response({
                    "status": "all clients disconnected",
                    "message": f"Disconnected {disconnected_count} clients",
                    "count": disconnected_count
                })

            else:
                return web.json_response(
                    {"error": "Invalid action. Use: start, stop, disconnect_all"},
                    status=400
                )

        except Exception as e:
            logger.error(f"Broadcast control error: {e}")
            return web.json_response({"error": str(e)}, status=500)

    async def switch_device(self, request) -> web.Response:
        """Switch between CPU and GPU processing"""
        try:
            data = await request.json()
            target_device = data.get("device", "").lower()

            logger.info(f"Device switch request: {target_device}")

            if target_device not in ["cpu", "mps"]:
                return web.json_response(
                    {"error": "Invalid device. Use: cpu, mps"},
                    status=400
                )

            if target_device == "mps" and not torch.backends.mps.is_available():
                return web.json_response(
                    {"error": "MPS not available on this system"},
                    status=400
                )

            # Switch model device
            if self.detector and self.detector.model:
                old_device = self.device
                self.detector.model.to(target_device)
                self.device = target_device

                if target_device == "mps":
                    torch.mps.empty_cache()

                logger.info(f"Device switched: {old_device} -> {target_device}")

                return web.json_response({
                    "status": f"switched to {target_device.upper()}",
                    "message": f"Processing device changed from {old_device.upper()} to {target_device.upper()}",
                    "device": target_device,
                    "previous_device": old_device
                })

            return web.json_response(
                {"error": "Detector not available"},
                status=503
            )

        except Exception as e:
            logger.error(f"Device switch error: {e}")
            return web.json_response({"error": str(e)}, status=500)

    async def get_client_management(self, request) -> web.Response:
        """Get client management interface"""
        if not self.broadcaster:
            return web.json_response({"error": "Broadcaster not available"}, status=503)

        clients_info = []
        for i, client in enumerate(self.broadcaster.clients):
            clients_info.append({
                "id": i,
                "address": str(client),
                "connected_at": "unknown",  # Could track this
                "status": "active"
            })

        return web.json_response({
            "total_clients": len(self.broadcaster.clients),
            "clients": clients_info,
            "controls": {
                "disconnect_all": "POST /control/broadcast {action: 'disconnect_all'}",
                "get_stats": "Available via WebSocket"
            }
        })

    async def update_config(self, request) -> web.Response:
        """Update configuration endpoint with better validation"""
        try:
            data = await request.json()

            logger.info(f"Config update request: {data}")

            # Validate and update specific config values
            updated = {}

            if "confidence_threshold" in data:
                confidence = float(data["confidence_threshold"])
                if 0.1 <= confidence <= 0.9:
                    config.CONFIDENCE_THRESHOLD = confidence
                    updated["confidence_threshold"] = confidence
                else:
                    return web.json_response(
                        {"error": "Confidence threshold must be between 0.1 and 0.9"},
                        status=400
                    )

            if "target_fps" in data:
                fps = int(data["target_fps"])
                if 1 <= fps <= 60:
                    config.TARGET_FPS = fps
                    updated["target_fps"] = fps
                else:
                    return web.json_response(
                        {"error": "Target FPS must be between 1 and 60"},
                        status=400
                    )

            if "jpeg_quality" in data:
                quality = int(data["jpeg_quality"])
                if 10 <= quality <= 100:
                    config.JPEG_QUALITY = quality
                    updated["jpeg_quality"] = quality
                else:
                    return web.json_response(
                        {"error": "JPEG quality must be between 10 and 100"},
                        status=400
                    )

            logger.info(f"Configuration updated: {updated}")

            return web.json_response({
                "status": "success",
                "message": "Configuration updated successfully",
                "updated": updated,
                "timestamp": asyncio.get_event_loop().time()
            })

        except ValueError as e:
            logger.error(f"Config update validation error: {e}")
            return web.json_response(
                {"error": f"Invalid value: {str(e)}"},
                status=400
            )
        except Exception as e:
            logger.error(f"Config update error: {e}")
            return web.json_response(
                {"error": str(e)},
                status=400
            )