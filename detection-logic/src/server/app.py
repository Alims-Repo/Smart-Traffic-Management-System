"""
Main application server
Author: Alims-Repo
Date: 2025-06-17
"""

import json
import traceback
from typing import Dict, Any, Optional
import aiohttp
from aiohttp import web

from config.settings import get_config
from src.utils.logging_config import get_logger
from src.core.device_optimizer import DeviceOptimizer
from src.core.detector import VehicleDetector
from src.core.broadcaster import VideoBroadcaster
from src.server.handlers import WebSocketHandlers, HTTPHandlers

config = get_config()
logger = get_logger("app")


class VehicleDetectionServer:
    """Main server application"""
    
    def __init__(self):
        self.app = web.Application()
        self.broadcaster: Optional[VideoBroadcaster] = None
        self.detector: Optional[VehicleDetector] = None
        self.model = None
        self.device = None
        
        # Handlers
        self.ws_handlers: Optional[WebSocketHandlers] = None
        self.http_handlers: Optional[HTTPHandlers] = None
        
        self._setup_routes()
        self._setup_middleware()
    
    def _setup_routes(self):
        """Setup web routes"""
        # WebSocket endpoint
        self.app.router.add_get("/ws", self.websocket_handler)
        
        # HTTP endpoints - will be configured after handlers are created
        self.app.on_startup.append(self._setup_http_routes)
        
        # Lifecycle hooks
        self.app.on_startup.append(self.on_startup)
        self.app.on_shutdown.append(self.on_shutdown)
    
    async def _setup_http_routes(self, app):
        """Setup HTTP routes after handlers are initialized"""
        if self.http_handlers:
            self.app.router.add_get("/health", self.http_handlers.health_check)
            self.app.router.add_get("/stats", self.http_handlers.get_stats)
            self.app.router.add_get("/config", self.http_handlers.get_config)
            self.app.router.add_post("/config", self.http_handlers.update_config)
            self.app.router.add_get("/performance", self.http_handlers.get_performance)
            self.app.router.add_get("/api", self.http_handlers.api_info)
    
    def _setup_middleware(self):
        """Setup middleware for CORS, logging, etc."""
        
        @web.middleware
        async def cors_handler(request, handler):
            """CORS middleware"""
            response = await handler(request)
            response.headers['Access-Control-Allow-Origin'] = '*'
            response.headers['Access-Control-Allow-Methods'] = 'GET, POST, OPTIONS'
            response.headers['Access-Control-Allow-Headers'] = 'Content-Type'
            return response
        
        @web.middleware
        async def error_handler(request, handler):
            """Error handling middleware"""
            try:
                return await handler(request)
            except Exception as e:
                logger.error(f"❌ Request error: {e}")
                if config.DEBUG:
                    traceback.print_exc()
                return web.json_response(
                    {"error": str(e), "path": request.path},
                    status=500
                )
        
        self.app.middlewares.append(cors_handler)
        self.app.middlewares.append(error_handler)
    
    async def websocket_handler(self, request) -> web.WebSocketResponse:
        """Handle WebSocket connections"""
        ws = web.WebSocketResponse(
            heartbeat=config.WS_HEARTBEAT,
            timeout=config.WS_TIMEOUT
        )
        await ws.prepare(request)
        
        # Add client to broadcaster
        if self.broadcaster:
            self.broadcaster.add_client(ws)
        
        logger.info(f"🔌 WebSocket connected from {request.remote}")
        
        try:
            async for msg in ws:
                if msg.type == aiohttp.WSMsgType.TEXT:
                    await self._handle_websocket_message(ws, msg.data)
                elif msg.type == aiohttp.WSMsgType.ERROR:
                    logger.error(f"🚨 WebSocket error: {ws.exception()}")
                    break
                
        except Exception as e:
            logger.error(f"🚨 WebSocket handler error: {e}")
            if config.DEBUG:
                traceback.print_exc()
        finally:
            # Remove client from broadcaster
            if self.broadcaster:
                self.broadcaster.remove_client(ws)
            logger.info(f"🔌 WebSocket disconnected from {request.remote}")
        
        return ws
    
    async def _handle_websocket_message(self, ws: web.WebSocketResponse, data: str):
        """Handle incoming WebSocket messages"""
        try:
            message = json.loads(data)
            command = message.get("command")
            
            if not self.ws_handlers:
                await ws.send_json({"error": "Server not ready"})
                return
            
            # Route command to appropriate handler
            handler_map = {
                "get_stats": self.ws_handlers.handle_get_stats,
                "get_system_info": self.ws_handlers.handle_get_system_info,
                "reset_stats": self.ws_handlers.handle_reset_stats,
                "get_clients": self.ws_handlers.handle_get_clients
            }
            
            handler = handler_map.get(command)
            if handler:
                response = await handler(ws)
                await ws.send_json(response)
            else:
                await ws.send_json({
                    "error": f"Unknown command: {command}",
                    "available_commands": list(handler_map.keys())
                })
                
        except json.JSONDecodeError:
            await ws.send_json({"error": "Invalid JSON message"})
        except Exception as e:
            logger.error(f"❌ WebSocket message error: {e}")
            await ws.send_json({"error": str(e)})
    
    async def on_startup(self, app):
        """Application startup"""
        logger.info("🚀 Starting M1 Pro Vehicle Detection Server...")
        logger.info(f"📁 Video path: {config.VIDEO_PATH}")
        logger.info(f"🎯 Target FPS: {config.TARGET_FPS}")
        logger.info(f"🔧 Debug mode: {config.DEBUG}")
        
        # Check video file
        if not config.VIDEO_PATH.exists():
            logger.error(f"❌ Video file not found: {config.VIDEO_PATH}")
            logger.info("💡 Place your video file at: resources/1.mp4")
            return
        
        try:
            # Initialize model and device
            logger.info("🧠 Initializing AI model...")
            self.model, self.device = DeviceOptimizer.find_optimal_config()
            
            # Initialize detector
            logger.info("🔍 Setting up vehicle detector...")
            self.detector = VehicleDetector(self.model, self.device)
            
            # Initialize broadcaster
            logger.info("📡 Starting video broadcaster...")
            self.broadcaster = VideoBroadcaster(config.VIDEO_PATH, self.detector, self.device)
            self.broadcaster.start()
            
            # Initialize handlers
            self.ws_handlers = WebSocketHandlers(self.broadcaster, self.detector, self.device)
            self.http_handlers = HTTPHandlers(self.broadcaster, self.detector, self.device)
            
            logger.info("✅ Server startup complete!")
            logger.info(f"🌐 Access the server at: http://{config.HOST}:{config.PORT}")
            logger.info(f"🔗 WebSocket endpoint: ws://{config.HOST}:{config.PORT}/ws")
            logger.info(f"📊 Health check: http://{config.HOST}:{config.PORT}/health")
            
        except Exception as e:
            logger.error(f"❌ Startup failed: {e}")
            if config.DEBUG:
                traceback.print_exc()
            raise
    
    async def on_shutdown(self, app):
        """Application shutdown"""
        logger.info("🛑 Shutting down server...")
        
        try:
            if self.broadcaster:
                await self.broadcaster.stop()
            
            logger.info("✅ Server shutdown complete")
        except Exception as e:
            logger.error(f"❌ Shutdown error: {e}")
    
    def run(self):
        """Run the server"""
        logger.info("🌟 M1 Pro Vehicle Detection Server")
        logger.info("=" * 50)
        logger.info(f"📍 URL: http://{config.HOST}:{config.PORT}")
        logger.info(f"🎯 Vehicle Classes: {', '.join(config.VEHICLE_CLASSES)}")
        logger.info(f"🔧 Configuration: {config.__class__.__name__}")
        logger.info("=" * 50)
        
        web.run_app(
            self.app, 
            host=config.HOST, 
            port=config.PORT,
            access_log=logger if config.DEBUG else None
        )


def create_app() -> web.Application:
    """Factory function to create the application"""
    server = VehicleDetectionServer()
    return server.app