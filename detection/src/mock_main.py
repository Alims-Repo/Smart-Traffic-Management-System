"""
    Simple mock detection service for testing
    This provides the same API as the full detection service but with mock data
"""

import json
import asyncio
import random
from aiohttp import web
import time
from typing import Dict, Any, List
import psutil

class MockVehicleDetectionServer:
    """Mock vehicle detection server for testing without ML dependencies"""

    def __init__(self):
        self.app = web.Application()
        self.clients: List[web.WebSocketResponse] = []
        self.setup_routes()
        self.setup_middleware()
        
        # Mock data
        self.vehicle_count = 0
        self.mock_data = {
            "vehicle_count": 0,
            "average_speed": 0.0,
            "congestion_level": 0.0,
            "timestamp": time.time() * 1000,
            "vehicles_by_type": {
                "car": 0,
                "truck": 0,
                "motorcycle": 0,
                "bus": 0
            }
        }

    def setup_middleware(self):
        """Setup CORS middleware"""
        async def cors_handler(request, handler):
            response = await handler(request)
            response.headers['Access-Control-Allow-Origin'] = '*'
            response.headers['Access-Control-Allow-Methods'] = 'GET, POST, OPTIONS'
            response.headers['Access-Control-Allow-Headers'] = 'Content-Type'
            return response

        self.app.middlewares.append(cors_handler)

    def setup_routes(self):
        """Setup API routes"""
        self.app.router.add_get("/health", self.health_check)
        self.app.router.add_get("/stats", self.get_stats)
        self.app.router.add_get("/api", self.get_api_info)
        self.app.router.add_get("/performance", self.get_performance)
        self.app.router.add_get("/ws", self.websocket_handler)

    async def health_check(self, request):
        """Health check endpoint"""
        return web.json_response({
            "status": "healthy",
            "service": "Mock Vehicle Detection Server",
            "timestamp": time.time() * 1000,
            "version": "1.0.0-mock"
        })

    async def get_stats(self, request):
        """Get current traffic statistics"""
        # Generate mock data with some variation
        self.mock_data.update({
            "vehicle_count": random.randint(15, 45),
            "average_speed": round(random.uniform(25.0, 55.0), 1),
            "congestion_level": round(random.uniform(0.1, 0.8), 2),
            "timestamp": time.time() * 1000,
            "vehicles_by_type": {
                "car": random.randint(10, 30),
                "truck": random.randint(1, 5),
                "motorcycle": random.randint(0, 3),
                "bus": random.randint(0, 2)
            }
        })
        
        return web.json_response(self.mock_data)

    async def get_api_info(self, request):
        """Get API information"""
        return web.json_response({
            "service": "Mock Vehicle Detection API",
            "version": "1.0.0-mock",
            "endpoints": {
                "health": "/health",
                "stats": "/stats",
                "api": "/api",
                "performance": "/performance",
                "websocket": "/ws"
            },
            "status": "running",
            "mock_mode": True
        })

    async def get_performance(self, request):
        """Get system performance metrics"""
        cpu_percent = psutil.cpu_percent(interval=1)
        memory = psutil.virtual_memory()
        
        return web.json_response({
            "cpu_usage": cpu_percent,
            "memory_used": memory.used,
            "memory_total": memory.total,
            "memory_percent": memory.percent,
            "timestamp": time.time() * 1000
        })

    async def websocket_handler(self, request):
        """Handle WebSocket connections"""
        ws = web.WebSocketResponse()
        await ws.prepare(request)
        
        self.clients.append(ws)
        print(f"WebSocket client connected. Total clients: {len(self.clients)}")
        
        try:
            # Send periodic updates
            while not ws.closed:
                stats = await self.get_mock_stats()
                await ws.send_str(json.dumps({
                    "type": "traffic_update",
                    "data": stats
                }))
                await asyncio.sleep(5)  # Send update every 5 seconds
                
        except Exception as e:
            print(f"WebSocket error: {e}")
        finally:
            if ws in self.clients:
                self.clients.remove(ws)
            print(f"WebSocket client disconnected. Total clients: {len(self.clients)}")
        
        return ws

    async def get_mock_stats(self):
        """Generate mock traffic statistics"""
        return {
            "vehicle_count": random.randint(10, 50),
            "average_speed": round(random.uniform(20.0, 60.0), 1),
            "congestion_level": round(random.uniform(0.0, 1.0), 2),
            "timestamp": time.time() * 1000,
            "camera_id": "mock-camera-001",
            "vehicles_by_type": {
                "car": random.randint(8, 35),
                "truck": random.randint(0, 6),
                "motorcycle": random.randint(0, 4),
                "bus": random.randint(0, 3)
            }
        }

    def run(self, host="0.0.0.0", port=1234):
        """Run the mock server"""
        print("🎭 Mock Vehicle Detection Server")
        print("=" * 40)
        print(f"📍 URL: http://{host}:{port}")
        print("🎯 Mode: Mock/Testing")
        print("💡 Generating synthetic traffic data")
        print("=" * 40)
        
        web.run_app(self.app, host=host, port=port)

if __name__ == "__main__":
    import os
    
    # Check if we should run in mock mode (no ML dependencies available)
    try:
        import cv2
        import torch
        from ultralytics import YOLO
        print("✅ ML dependencies available - using full detection service")
        # Import and run the real service
        from src.server.app import VehicleDetectionServer
        server = VehicleDetectionServer()
        server.run()
    except ImportError as e:
        print(f"⚠️  ML dependencies not available: {e}")
        print("🎭 Running in mock mode")
        # Run mock service
        server = MockVehicleDetectionServer()
        server.run()