from fastapi import FastAPI, WebSocket
from fastapi.responses import HTMLResponse
from starlette.staticfiles import StaticFiles
import numpy as np
import cv2
import os
from ultralytics import YOLO

app = FastAPI()

os.makedirs("frames", exist_ok=True)

model = YOLO("yolov8n.pt")
vehicle_classes = {2, 3, 5, 7}
class_names = model.names

@app.websocket("/ws")
async def websocket_endpoint(websocket: WebSocket):
    await websocket.accept()
    print("WebSocket client connected")
    while True:
        try:
            data = await websocket.receive_bytes()

            # Convert to image
            img_arr = np.frombuffer(data, dtype=np.uint8)
            frame = cv2.imdecode(img_arr, cv2.IMREAD_COLOR)

            if frame is None:
                continue

            # Detection
            results = model(frame)[0]
            boxes = []
            for r in results.boxes:
                cls = int(r.cls[0])
                if cls in vehicle_classes:
                    boxes.append(r)

            for box in boxes:
                x1, y1, x2, y2 = map(int, box.xyxy[0])
                cls_id = int(box.cls[0])
                label = f"{class_names[cls_id]} {box.conf[0]:.2f}"

                cv2.rectangle(frame, (x1, y1), (x2, y2), (0, 255, 0), 2)
                cv2.putText(frame, label, (x1, y1 - 10),
                            cv2.FONT_HERSHEY_SIMPLEX, 0.6, (0, 255, 0), 2)

            # Save and serve
            cv2.imwrite("frames/latest.jpg", frame)

        except Exception as e:
            print("WebSocket Error:", e)
            break

@app.get("/", response_class=HTMLResponse)
async def index():
    return """
    <html>
        <head><title>Live Stream</title></head>
        <body>
            <h2>YOLOv8 Vehicle Detection Stream</h2>
            <img src="/frames/latest.jpg" width="640" height="480" />
            <script>
                setInterval(() => {
                    const img = document.querySelector('img');
                    img.src = '/frames/latest.jpg?rand=' + Math.random();
                }, 500);
            </script>
        </body>
    </html>
    """

app.mount("/frames", StaticFiles(directory="frames"), name="frames")

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="0.0.0.0", port=8000)