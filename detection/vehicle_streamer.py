from fastapi import FastAPI, Response
from fastapi.responses import StreamingResponse, JSONResponse
from ultralytics import YOLO
import cv2
import threading
import time

app = FastAPI()
model = YOLO("yolov8n.pt")

# Shared state
latest_counts = {"fps": 0.0, "vehicle_count": 0}
lock = threading.Lock()

def gen_frames():
    cap = cv2.VideoCapture(0)  # or your RTSP stream URL
    prev_time = time.time()
    frame_count = 0
    while True:
        ret, frame = cap.read()
        if not ret:
            break
        results = model(frame)[0]
        vehicle_count = 0
        for box in results.boxes:
            cls = int(box.cls[0])
            # COCO classes: car=2, motorcycle=3, bus=5, truck=7
            if cls in [2, 3, 5, 7]:
                vehicle_count += 1
                x1, y1, x2, y2 = map(int, box.xyxy[0])
                label = model.names[cls]
                conf = float(box.conf[0])
                cv2.rectangle(frame, (x1, y1), (x2, y2), (0,255,0), 2)
                cv2.putText(frame, f"{label} {conf:.2f}", (x1, y1-10),
                            cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0,255,0), 2)
        frame_count += 1
        now = time.time()
        elapsed = now - prev_time
        if elapsed >= 1.0:
            fps = frame_count / elapsed
            with lock:
                latest_counts["fps"] = fps
                latest_counts["vehicle_count"] = vehicle_count
            prev_time = now
            frame_count = 0
        # Encode as JPEG
        ret, buffer = cv2.imencode('.jpg', frame)
        if not ret: continue
        frame_bytes = buffer.tobytes()
        yield (b'--frame\r\n'
               b'Content-Type: image/jpeg\r\n\r\n' + frame_bytes + b'\r\n')

@app.get("/video")
def video_feed():
    return StreamingResponse(gen_frames(), media_type="multipart/x-mixed-replace; boundary=frame")

@app.get("/stats")
def stats():
    with lock:
        return JSONResponse(content=latest_counts.copy())

if __name__ == "__main__":
    import uvicorn
    # Use multiple workers for better performance if needed, but streaming works best with workers=1
    uvicorn.run("vehicle_streamer:app", host="0.0.0.0", port=8000, reload=False)