#!/bin/bash

# Smart Traffic Management System - Startup Script
# This script builds and starts the complete system using Docker Compose

set -e

echo "🚦 Smart Traffic Management System - Starting Full System"
echo "=========================================================="

# Check if Docker and Docker Compose are available
if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed or not in PATH"
    exit 1
fi

if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose is not installed or not in PATH"
    exit 1
fi

# Create necessary directories
echo "📁 Creating necessary directories..."
mkdir -p detection/resources
mkdir -p detection/model

# Check for video file
if [ ! -f "detection/resources/1.mp4" ]; then
    echo "⚠️  No video file found at detection/resources/1.mp4"
    echo "💡 The system will use synthetic data for demonstration"
fi

# Load environment variables
if [ -f ".env" ]; then
    echo "📄 Loading environment variables from .env"
    set -a
    source .env
    set +a
fi

# Build and start services
echo "🏗️  Building Docker images..."
docker-compose build --no-cache

echo "🚀 Starting services..."
docker-compose up -d

# Wait for services to be healthy
echo "⏳ Waiting for services to be ready..."
sleep 10

# Check service health
echo "🔍 Checking service health..."

# Check PostgreSQL
echo -n "🐘 PostgreSQL: "
if docker-compose exec -T postgres pg_isready -U ktoruser -d ktordb >/dev/null 2>&1; then
    echo "✅ Ready"
else
    echo "❌ Not ready"
fi

# Check Python Detection Service
echo -n "🐍 Detection Service: "
if curl -s http://localhost:1234/health >/dev/null 2>&1; then
    echo "✅ Ready"
else
    echo "❌ Not ready"
fi

# Check Ktor Server
echo -n "🏗️  Ktor Server: "
if curl -s http://localhost:8080/api/system/overview >/dev/null 2>&1; then
    echo "✅ Ready"
else
    echo "⚠️  Starting up..."
fi

echo ""
echo "🌐 System Access URLs:"
echo "   • Main API:          http://localhost:8080"
echo "   • Detection Service: http://localhost:1234"
echo "   • Nginx Proxy:       http://localhost:80"
echo "   • Database:          localhost:5432"
echo ""
echo "📊 API Endpoints:"
echo "   • System Status:     http://localhost:8080/api/system/overview"
echo "   • Traffic Stats:     http://localhost:8080/api/traffic/stats"
echo "   • Traffic Signals:   http://localhost:8080/api/traffic/signals"
echo "   • Detection Health:  http://localhost:8080/api/traffic/detection/health"
echo ""
echo "🔌 WebSocket Endpoints:"
echo "   • System Status:     ws://localhost:8080/ws/system-status"
echo "   • Traffic Updates:   ws://localhost:8080/ws/traffic"
echo ""
echo "🎮 Management Commands:"
echo "   • View logs:         docker-compose logs -f"
echo "   • Stop system:       docker-compose down"
echo "   • Restart:           docker-compose restart"
echo "   • Clean up:          docker-compose down -v"
echo ""
echo "✅ Smart Traffic Management System is ready!"