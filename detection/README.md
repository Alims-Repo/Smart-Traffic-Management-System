# M1 Pro Vehicle Detection Server

Real-time vehicle detection server optimized for Apple M1 Pro with automatic device selection (CPU/GPU) and WebSocket streaming.

## Features

- 🚗 Real-time vehicle detection (cars, trucks, buses, motorcycles, bicycles)
- 🔥 M1 Pro GPU optimization with Metal Performance Shaders (MPS)
- 📊 Automatic performance benchmarking and device selection
- 🌐 WebSocket streaming for real-time video
- 📈 Performance monitoring and statistics
- 🎛️ Configurable detection parameters
- 🔄 Automatic video looping
- 📱 RESTful API for monitoring and control

## Quick Start

### 1. Installation

```bash
# Clone the repository
git clone <your-repo-url>
cd m1-vehicle-detection

# Create virtual environment
python3 -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate

# Install dependencies
pip install -r requirements.txt
```