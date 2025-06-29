import cv2
import torch
from ultralytics import YOLO
from deep_sort_realtime.deepsort_tracker import DeepSort
from collections import defaultdict
from pathlib import Path

# Paths
BASE_DIR = Path(__file__).parent.parent
MODEL_DIR = BASE_DIR / "model"
RESOURCES_DIR = BASE_DIR / "resources"
VIDEO_PATH = RESOURCES_DIR / "1.mp4"

# Device: use MPS (Apple Silicon GPU) if available, else CPU
device = "mps" if torch.backends.mps.is_available() else "cpu"
print(f"Using device: {device}")

# Load smaller YOLOv8 model (nano) on device
model = YOLO(MODEL_DIR / "yolov8s.pt").to(device)

# Initialize DeepSORT tracker with tuned parameters
tracker = DeepSort(max_age=15, n_init=2)

# COCO vehicle classes to detect
TARGET_CLASSES = {2: "car", 3: "motorcycle", 5: "bus", 7: "truck"}

# Video capture
cap = cv2.VideoCapture(str(VIDEO_PATH))

# Resize settings
INPUT_WIDTH = 640
FRAME_SKIP = 2  # process every 2nd frame; adjust as needed
frame_id = 0

while True:
    ret, frame = cap.read()
    if not ret:
        break

    # Frame skipping for speed
    if frame_id % FRAME_SKIP != 0:
        frame_id += 1
        continue
    frame_id += 1

    # Resize frame keeping aspect ratio
    scale = INPUT_WIDTH / frame.shape[1]
    input_height = int(frame.shape[0] * scale)
    frame_resized = cv2.resize(frame, (INPUT_WIDTH, input_height))
    frame_rgb = cv2.cvtColor(frame_resized, cv2.COLOR_BGR2RGB)

    # Inference with mixed precision on MPS if available
    with torch.autocast(device_type=device):
        results = model.predict(frame_rgb,
                                device=device,
                                classes=list(TARGET_CLASSES.keys()),
                                conf=0.4,
                                verbose=False)
    result = results[0]

    # Prepare detections for DeepSORT: scale back to original frame size
    detections = []
    for box, cls in zip(result.boxes.xyxy.tolist(), result.boxes.cls.tolist()):
        x1, y1, x2, y2 = box
        x1 = int(x1 / scale)
        y1 = int(y1 / scale)
        x2 = int(x2 / scale)
        y2 = int(y2 / scale)
        cls_id = int(cls)
        conf = 1.0  # You can use result.boxes.conf if needed
        detections.append(([x1, y1, x2 - x1, y2 - y1], conf, cls_id))

    # Update tracker
    tracks = tracker.update_tracks(detections, frame=frame)

    # Count vehicles visible this frame
    current_frame_counts = defaultdict(int)

    # Draw tracking boxes and labels
    for track in tracks:
        if not track.is_confirmed():
            continue

        tid = track.track_id
        ltrb = track.to_ltrb()
        cls_id = track.det_class
        cls_name = TARGET_CLASSES.get(cls_id, "unknown")

        current_frame_counts[cls_name] += 1

        x1, y1, x2, y2 = map(int, ltrb)
        cv2.rectangle(frame, (x1, y1), (x2, y2), (0, 255, 0), 2)
        cv2.putText(frame, f"{cls_name} ID:{tid}", (x1, y1 - 10),
                    cv2.FONT_HERSHEY_SIMPLEX, 0.6, (0, 255, 0), 2)

    # Show current counts on top-left
    y0, dy = 30, 25
    for i, (cls_name, count) in enumerate(current_frame_counts.items()):
        cv2.putText(frame, f"{cls_name}: {count}", (10, y0 + i * dy),
                    cv2.FONT_HERSHEY_SIMPLEX, 0.8, (255, 255, 255), 2)

    cv2.imshow("Vehicle Detection and Tracking (Optimized)", frame)
    if cv2.waitKey(1) & 0xFF == 27:  # ESC key to quit
        break

cap.release()
cv2.destroyAllWindows()