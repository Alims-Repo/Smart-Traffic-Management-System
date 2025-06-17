"""
Vehicle detection core logic
Author: Alims-Repo
Date: 2025-06-17
"""

from typing import List, Tuple, Optional
import time
import cv2
import numpy as np
import torch
from ultralytics import YOLO

from config.settings import get_config
from src.utils.logging_config import get_logger
from src.utils.helpers import PerformanceTracker, timer

config = get_config()
logger = get_logger("detector")


class VehicleDetector:
    """Handles vehicle detection with M1 Pro optimizations"""

    def __init__(self, model: YOLO, device: str):
        self.model = model
        self.device = device
        self.performance_tracker = PerformanceTracker()

        logger.info(f"🔧 Detector initialized: {device.upper()}")
        logger.info(f"📋 Vehicle classes: {', '.join(config.VEHICLE_CLASSES)}")

    @timer
    def preprocess_frame(self, frame: np.ndarray) -> np.ndarray:
        """
        Preprocess frame for optimal inference

        Args:
            frame: Input frame

        Returns:
            Preprocessed frame
        """
        if frame.shape[:2] != (config.INPUT_SIZE, config.INPUT_SIZE):
            return cv2.resize(frame, (config.INPUT_SIZE, config.INPUT_SIZE))
        return frame

    def postprocess_detections(
        self,
        frame: np.ndarray,
        result,
        scale_x: float,
        scale_y: float
    ) -> Tuple[np.ndarray, int]:
        """
        Process detection results and draw annotations

        Args:
            frame: Original frame
            result: YOLO detection result
            scale_x: X-axis scaling factor
            scale_y: Y-axis scaling factor

        Returns:
            Tuple of (annotated_frame, vehicle_count)
        """
        vehicle_count = 0
        output_frame = frame.copy()

        if result.boxes is None or len(result.boxes) == 0:
            return output_frame, vehicle_count

        for box in result.boxes:
            cls_id = int(box.cls[0])
            cls_name = self.model.names[cls_id]

            if cls_name not in config.VEHICLE_CLASSES:
                continue

            vehicle_count += 1

            # Scale coordinates back to original size
            x1, y1, x2, y2 = box.xyxy[0].cpu().numpy()
            x1, x2 = int(x1 * scale_x), int(x2 * scale_x)
            y1, y2 = int(y1 * scale_y), int(y2 * scale_y)

            # Ensure coordinates are within frame bounds
            x1 = max(0, min(x1, frame.shape[1] - 1))
            y1 = max(0, min(y1, frame.shape[0] - 1))
            x2 = max(0, min(x2, frame.shape[1] - 1))
            y2 = max(0, min(y2, frame.shape[0] - 1))

            conf = float(box.conf[0])
            label = f"{cls_name}: {conf:.2f}"

            # Draw bounding box with confidence-based styling
            color = self._get_confidence_color(conf)
            thickness = 3 if conf > 0.7 else 2

            cv2.rectangle(output_frame, (x1, y1), (x2, y2), color, thickness)

            # Draw label with background
            self._draw_label(output_frame, label, (x1, y1), color)

        return output_frame, vehicle_count

    def _get_confidence_color(self, confidence: float) -> Tuple[int, int, int]:
        """Get color based on confidence level"""
        if confidence > 0.8:
            return (0, 255, 0)  # Green for high confidence
        elif confidence > 0.6:
            return (0, 255, 255)  # Yellow for medium confidence
        else:
            return (0, 165, 255)  # Orange for low confidence

    def _draw_label(
        self,
        frame: np.ndarray,
        label: str,
        position: Tuple[int, int],
        color: Tuple[int, int, int]
    ):
        """Draw label with background"""
        x, y = position
        font_scale = 0.6
        text_thickness = 2

        # Get text size
        (text_width, text_height), baseline = cv2.getTextSize(
            label, cv2.FONT_HERSHEY_SIMPLEX, font_scale, text_thickness
        )

        # Draw background rectangle
        cv2.rectangle(
            frame,
            (x, y - text_height - baseline - 5),
            (x + text_width + 5, y),
            color,
            -1
        )

        # Draw text
        cv2.putText(
            frame, label, (x + 2, y - 5),
            cv2.FONT_HERSHEY_SIMPLEX, font_scale, (0, 0, 0), text_thickness
        )

    def detect_vehicles(self, frames: List[np.ndarray]) -> Tuple[List[np.ndarray], List[int]]:
        """
        Perform vehicle detection on frame(s)

        Args:
            frames: List of input frames

        Returns:
            Tuple of (processed_frames, vehicle_counts)
        """
        start_time = time.time()

        results_list = []
        counts_list = []

        try:
            for frame in frames:
                # Preprocess
                original_shape = frame.shape
                processed_frame = self.preprocess_frame(frame)

                # Calculate scaling factors
                scale_x = original_shape[1] / config.INPUT_SIZE
                scale_y = original_shape[0] / config.INPUT_SIZE

                # Inference
                results = self.model.predict(
                    processed_frame,
                    verbose=False,
                    device=self.device,
                    conf=config.CONFIDENCE_THRESHOLD,
                    iou=config.IOU_THRESHOLD,
                    max_det=config.MAX_DETECTIONS,
                    half=False,  # M1 Pro prefers float32
                    augment=False,
                    agnostic_nms=False,
                    retina_masks=False,
                    save=False,
                    stream=False,
                )

                if self.device == "mps":
                    torch.mps.synchronize()

                # Postprocess
                output_frame, vehicle_count = self.postprocess_detections(
                    frame, results[0], scale_x, scale_y
                )

                results_list.append(output_frame)
                counts_list.append(vehicle_count)

            # Track performance
            detection_time = time.time() - start_time
            total_vehicles = sum(counts_list)
            self.performance_tracker.add_sample(detection_time, total_vehicles)

            if detection_time > 0.2:  # Log slow detections
                logger.warning(f"⚠️  Slow detection: {detection_time:.3f}s for {len(frames)} frames")

            return results_list, counts_list

        except Exception as e:
            logger.error(f"🚨 Detection error: {e}")
            # Return original frames with zero counts on error
            return frames, [0] * len(frames)

    @property
    def avg_detection_time(self) -> float:
        """Get average detection time"""
        return self.performance_tracker.average_time

    @property
    def avg_vehicle_count(self) -> float:
        """Get average vehicle count"""
        return self.performance_tracker.average_count

    @property
    def detection_fps(self) -> float:
        """Get detection FPS"""
        return self.performance_tracker.fps

    def get_performance_stats(self) -> dict:
        """Get comprehensive performance statistics"""
        stats = self.performance_tracker.get_stats()
        stats.update({
            "device": self.device,
            "model_name": getattr(self.model, 'ckpt_path', 'unknown'),
            "input_size": config.INPUT_SIZE,
            "confidence_threshold": config.CONFIDENCE_THRESHOLD,
            "vehicle_classes": config.VEHICLE_CLASSES
        })
        return stats

    def reset_performance_stats(self):
        """Reset performance tracking"""
        self.performance_tracker = PerformanceTracker()
        logger.info("📊 Performance stats reset")