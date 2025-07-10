FROM python:3.12-slim

# Install required system packages for OpenCV and YOLO
RUN apt-get update && \
    apt-get install -y libgl1 libglib2.0-0 curl && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY . .

# Simplify requirements for build environment
RUN pip install --upgrade pip --trusted-host pypi.org --trusted-host pypi.python.org --trusted-host files.pythonhosted.org && \
    pip install --trusted-host pypi.org --trusted-host pypi.python.org --trusted-host files.pythonhosted.org -r requirements.txt

EXPOSE 1234

CMD ["python", "src/main.py"]