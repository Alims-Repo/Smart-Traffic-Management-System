# Smart Traffic Management System - Development Setup

## 🚀 Quick Start

This system is fully configured and ready for development. All components have been integrated on the `Development` branch.

### System Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Frontend Layer                        │
│                Kotlin Compose App                       │
│            (composeApp/ directory)                      │
└─────────────────────────────────────────────────────────┘
                            ▼
┌─────────────────────────────────────────────────────────┐
│                   API Gateway                           │
│                  Nginx Proxy                           │
│                  (nginx.conf)                          │
└─────────────────────────────────────────────────────────┘
                            ▼
┌─────────────────────────────────────────────────────────┐
│                 Backend Services                        │
│  ┌─────────────────┐    ┌─────────────────────────────┐  │
│  │  Ktor Server    │    │  Python Detection Service  │  │
│  │  (Port 8080)    │◄──►│  (Port 1234)               │  │
│  │                 │    │                             │  │
│  │ • REST APIs     │    │ • Vehicle Detection         │  │
│  │ • WebSockets    │    │ • Traffic Analysis          │  │
│  │ • Traffic Mgmt  │    │ • Real-time Data            │  │
│  └─────────────────┘    └─────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
                            ▼
┌─────────────────────────────────────────────────────────┐
│                    Data Layer                           │
│                PostgreSQL Database                      │
│               (Port 5432)                              │
└─────────────────────────────────────────────────────────┘
```

## 🔧 System Components

### 1. Ktor Server (Backend API)
- **Location**: `server/`
- **Port**: 8080
- **Features**:
  - Traffic management REST APIs
  - Real-time WebSocket connections
  - Integration with Python detection service
  - CORS-enabled for frontend access
  - PostgreSQL database integration

### 2. Python Detection Service
- **Location**: `detection/`
- **Port**: 1234
- **Features**:
  - AI-powered vehicle detection
  - Mock mode for testing without ML dependencies
  - Real-time traffic statistics
  - WebSocket broadcasting

### 3. Database
- **Type**: PostgreSQL
- **Port**: 5432
- **Configuration**: Automatically configured in Docker

### 4. Reverse Proxy
- **Type**: Nginx
- **Port**: 80
- **Purpose**: Route requests and provide load balancing

## 📡 API Endpoints

### System Status
```
GET  /api/system/overview           # System overview
WS   /ws/system-status             # Real-time system status
```

### Traffic Management
```
GET  /api/traffic/stats             # Current traffic statistics
GET  /api/traffic/stats/all         # All camera traffic data
GET  /api/traffic/signals           # Traffic signal status
GET  /api/traffic/signals/{id}      # Specific traffic signal
POST /api/traffic/signals/control   # Control traffic signals
PUT  /api/traffic/signals/{id}      # Update traffic signal
WS   /ws/traffic                   # Real-time traffic updates
```

### Detection Service
```
GET  /api/traffic/detection/health  # Detection service health
```

## 🐳 Docker Deployment

### Prerequisites
- Docker and Docker Compose
- Port availability: 80, 1234, 5432, 8080

### Start System
```bash
# Full system with all services
./start-system.sh

# Or manually
docker compose up -d
```

### Service Management
```bash
# View logs
docker compose logs -f

# Stop system
docker compose down

# Clean restart
docker compose down -v && docker compose up -d

# Scale services
docker compose up --scale ktor-server=2 -d
```

## 🧪 Testing

### Validate System
```bash
./test-system.sh
```

### Test Individual Components

#### 1. Test Detection Service
```bash
curl http://localhost:1234/health
curl http://localhost:1234/stats
```

#### 2. Test Ktor Server
```bash
curl http://localhost:8080/api/system/overview
curl http://localhost:8080/api/traffic/stats
```

#### 3. Test WebSocket Connection
```javascript
const ws = new WebSocket('ws://localhost:8080/ws/traffic');
ws.onmessage = (event) => console.log(JSON.parse(event.data));
```

## 🔗 Frontend Integration

### Compose App Configuration

Update your Compose app's API client configuration:

```kotlin
object ApiConfig {
    const val BASE_URL = "http://localhost:8080"
    const val WS_URL = "ws://localhost:8080"
    
    // For production
    const val PROD_BASE_URL = "https://your-domain.com"
    const val PROD_WS_URL = "wss://your-domain.com"
}
```

### WebSocket Integration Example

```kotlin
class TrafficWebSocketClient {
    private var webSocket: WebSocket? = null
    
    fun connect() {
        val request = Request.Builder()
            .url("${ApiConfig.WS_URL}/ws/traffic")
            .build()
            
        webSocket = OkHttpClient().newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                val update = Json.decodeFromString<TrafficUpdate>(text)
                // Handle traffic update
            }
        })
    }
}
```

## 🛠️ Development

### Adding New Features

1. **New API Endpoint**:
   - Add route in `server/src/main/kotlin/com/gub/routes/TrafficRoutes.kt`
   - Update service in `server/src/main/kotlin/com/gub/services/TrafficService.kt`

2. **New Data Model**:
   - Add to `shared/src/commonMain/kotlin/com/gub/models/`
   - Use `@Serializable` annotation

3. **Database Changes**:
   - Update `DatabaseFactory.kt`
   - Add migration scripts if needed

### Environment Configuration

Modify `.env` file for different environments:

```env
# Development
DATABASE_URL=jdbc:postgresql://localhost:5432/ktordb
DETECTION_SERVICE_URL=http://localhost:1234

# Production
DATABASE_URL=jdbc:postgresql://prod-db:5432/traffic_prod
DETECTION_SERVICE_URL=http://detection-service:1234
```

## 🔧 Troubleshooting

### Common Issues

1. **Port Conflicts**:
   ```bash
   # Check port usage
   netstat -tulpn | grep :8080
   
   # Change ports in docker-compose.yml if needed
   ```

2. **Database Connection**:
   ```bash
   # Check database logs
   docker compose logs postgres
   
   # Connect to database
   docker compose exec postgres psql -U ktoruser -d ktordb
   ```

3. **Service Health**:
   ```bash
   # Check all service health
   curl http://localhost:1234/health
   curl http://localhost:8080/api/system/overview
   ```

### Build Issues

If you encounter dependency issues:

1. **Use Mock Mode**: The system includes a mock detection service that works without ML dependencies
2. **Offline Build**: Use cached dependencies when possible
3. **Network Issues**: The startup script handles network connectivity problems gracefully

## 📈 Monitoring

### Health Checks
All services include health check endpoints and Docker health checks configured.

### Logs
```bash
# All services
docker compose logs -f

# Specific service
docker compose logs -f ktor-server
docker compose logs -f python-server
```

### Performance
Monitor system performance via:
- `/api/traffic/stats` - Traffic metrics
- `/performance` - Detection service performance
- Database query logs

## 🚀 Production Deployment

### Security Considerations
1. Change default database passwords
2. Configure SSL certificates for HTTPS
3. Update CORS settings for production domains
4. Set up proper firewall rules

### Scaling
The system is designed to scale horizontally:
- Multiple Ktor server instances
- Load balancing via Nginx
- Database read replicas
- Separate detection service instances

## 📚 Next Steps

1. **Frontend Integration**: Connect Compose app to backend APIs
2. **Real Video Data**: Add actual traffic video files to `detection/resources/`
3. **ML Model Training**: Train custom models for specific traffic scenarios
4. **Monitoring Setup**: Add comprehensive monitoring and alerting
5. **CI/CD Pipeline**: Set up automated testing and deployment

## ✅ System Status

- ✅ Backend API Server (Ktor)
- ✅ Detection Service (Python with Mock mode)
- ✅ Database Integration (PostgreSQL)
- ✅ Docker Compose Setup
- ✅ API Documentation
- ✅ WebSocket Real-time Updates
- ✅ Traffic Management Models
- ✅ Health Monitoring
- ✅ CORS Configuration
- ✅ Environment Configuration
- 🔄 Frontend Integration (Ready for connection)
- 🔄 Production Deployment (Configured, ready to deploy)

The Smart Traffic Management System is now complete and ready for development and testing!