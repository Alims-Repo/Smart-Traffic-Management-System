# Traffic Congestion Database Design

This document describes the PostgreSQL database schema designed to store traffic congestion data for the Smart Traffic Management System.

## 📋 Database Schema Overview

The database consists of 6 main tables designed to capture comprehensive traffic data:

### 1. **Intersections Table**
Stores intersection/location information where traffic is monitored.

| Column | Type | Description |
|--------|------|-------------|
| id | SERIAL PRIMARY KEY | Unique identifier |
| name | VARCHAR(100) | Intersection name (e.g., "Main St & 1st Ave") |
| latitude | DECIMAL(10,8) | Geographic latitude |
| longitude | DECIMAL(11,8) | Geographic longitude |
| description | TEXT | Optional description |
| is_active | BOOLEAN | Whether the intersection is actively monitored |
| created_at | TIMESTAMP | Record creation timestamp |

### 2. **TrafficData Table**
Stores real-time traffic measurements from sensors and AI detection systems.

| Column | Type | Description |
|--------|------|-------------|
| id | SERIAL PRIMARY KEY | Unique identifier |
| intersection_id | INTEGER FK | Reference to Intersections table |
| timestamp | TIMESTAMP | When the measurement was taken |
| vehicle_count | INTEGER | Number of vehicles detected |
| average_speed | DECIMAL(5,2) | Average speed in km/h |
| congestion_level | ENUM | LOW, MEDIUM, HIGH, CRITICAL |
| temperature | DECIMAL(5,2) | Weather temperature (optional) |
| humidity | INTEGER | Weather humidity percentage (optional) |
| visibility | DECIMAL(5,2) | Weather visibility in km (optional) |
| created_at | TIMESTAMP | Record creation timestamp |

### 3. **TrafficAnalytics Table**
Stores aggregated analytics metrics calculated from raw traffic data.

| Column | Type | Description |
|--------|------|-------------|
| id | SERIAL PRIMARY KEY | Unique identifier |
| intersection_id | INTEGER FK | Reference to Intersections table |
| timestamp | TIMESTAMP | Time period for this aggregation |
| total_vehicles | INTEGER | Total vehicles in the period |
| average_speed | DECIMAL(5,2) | Average speed during period |
| congestion_level | DECIMAL(3,2) | Calculated congestion score (0-1) |
| incident_count | INTEGER | Number of incidents in period |
| peak_hour_intensity | DECIMAL(3,2) | Peak hour intensity score |
| ai_optimized_timing | BOOLEAN | Whether AI optimization was applied |
| period_type | ENUM | HOURLY, DAILY, WEEKLY, MONTHLY |
| created_at | TIMESTAMP | Record creation timestamp |

### 4. **PeakHours Table**
Stores identified peak traffic periods for traffic optimization.

| Column | Type | Description |
|--------|------|-------------|
| id | SERIAL PRIMARY KEY | Unique identifier |
| intersection_id | INTEGER FK | Reference to Intersections table |
| start_time | TIMESTAMP | Peak period start time |
| end_time | TIMESTAMP | Peak period end time |
| period | VARCHAR(50) | Description (e.g., "Morning Rush") |
| intensity | INTEGER | Peak intensity scale (1-10) |
| is_active | BOOLEAN | Whether this peak is currently active |
| peak_type | ENUM | CURRENT, PREDICTED, HISTORICAL |
| average_volume | INTEGER | Average vehicle volume during peak |
| created_at | TIMESTAMP | Record creation timestamp |

### 5. **TrafficIncidents Table**
Stores traffic incidents and congestion events that affect traffic flow.

| Column | Type | Description |
|--------|------|-------------|
| id | SERIAL PRIMARY KEY | Unique identifier |
| intersection_id | INTEGER FK | Reference to Intersections table |
| incident_type | ENUM | ACCIDENT, CONSTRUCTION, WEATHER, SIGNAL_FAILURE, CONGESTION, OTHER |
| severity | ENUM | LOW, MEDIUM, HIGH, CRITICAL |
| description | TEXT | Incident description (optional) |
| start_time | TIMESTAMP | When the incident started |
| end_time | TIMESTAMP | When the incident was resolved (optional) |
| is_resolved | BOOLEAN | Whether the incident is resolved |
| affected_lanes | INTEGER | Number of lanes affected (optional) |
| estimated_delay | INTEGER | Estimated delay in minutes (optional) |
| created_at | TIMESTAMP | Record creation timestamp |

### 6. **TrafficPredictions Table**
Stores AI-generated predictions for future traffic patterns.

| Column | Type | Description |
|--------|------|-------------|
| id | SERIAL PRIMARY KEY | Unique identifier |
| intersection_id | INTEGER FK | Reference to Intersections table |
| prediction_timestamp | TIMESTAMP | When this prediction is for |
| predicted_vehicle_count | INTEGER | Predicted number of vehicles |
| predicted_average_speed | DECIMAL(5,2) | Predicted average speed |
| predicted_congestion_level | DECIMAL(3,2) | Predicted congestion score |
| confidence | DECIMAL(3,2) | Prediction confidence (0.0-1.0) |
| prediction_model | VARCHAR(50) | AI model used (e.g., "LSTM", "ARIMA") |
| created_at | TIMESTAMP | When prediction was made |

## 🔧 Database Setup

### Prerequisites
1. PostgreSQL installed and running
2. Database and user created as specified in `DatabaseFactory.kt`

### Database Setup Commands
```sql
-- Connect to PostgreSQL as superuser
psql postgres

-- Create user and database
CREATE USER ktoruser WITH PASSWORD 'ktorpass';
ALTER USER ktoruser CREATEDB;
CREATE DATABASE ktordb WITH OWNER ktoruser;

-- Exit psql
\q
```

### Automatic Initialization
The system automatically creates tables and sample data when the server starts:

```kotlin
// In Application.kt
DatabaseFactory.init()
TrafficDatabaseInitializer.createTables()
TrafficDatabaseInitializer.insertSampleData()
```

## 📡 API Endpoints

### Intersections
- `GET /api/traffic/intersections` - Get all active intersections
- `GET /api/traffic/intersections/{id}` - Get specific intersection

### Traffic Data
- `POST /api/traffic/data` - Create new traffic data entry
- `GET /api/traffic/data/intersection/{id}` - Get traffic data for intersection
- `GET /api/traffic/data/latest` - Get latest traffic data for all intersections
- `GET /api/traffic/data/intersection/{id}/range?start={start}&end={end}` - Get data by time range

### Analytics
- `POST /api/traffic/analytics` - Create analytics entry
- `GET /api/traffic/analytics/intersection/{id}?period={HOURLY|DAILY|WEEKLY|MONTHLY}` - Get analytics

## 📊 Example API Usage

### Creating Traffic Data
```json
POST /api/traffic/data
{
  "intersectionId": 1,
  "vehicleCount": 45,
  "averageSpeed": 35.5,
  "congestionLevel": "MEDIUM",
  "temperature": 22.5,
  "humidity": 65,
  "visibility": 8.2
}
```

### Creating Analytics
```json
POST /api/traffic/analytics
{
  "intersectionId": 1,
  "totalVehicles": 1250,
  "averageSpeed": 32.8,
  "congestionLevel": 0.65,
  "incidentCount": 2,
  "peakHourIntensity": 0.85,
  "aiOptimizedTiming": true,
  "periodType": "HOURLY"
}
```

## 🧪 Testing

To test the database system, you can run the included test:

```bash
# Compile and run the database test
./gradlew server:build
java -cp server/build/libs/server-all.jar com.gub.test.DatabaseTestKt
```

## 🔄 Integration with Frontend

The database integrates with the existing frontend models:
- `TrafficVolumeData` → `TrafficData` table
- `AnalyticsMetrics` → `TrafficAnalytics` table
- `PeakHourData` → `PeakHours` table
- `CongestionLevel` enum → Used across multiple tables

## 📈 Data Flow

1. **Real-time Data Collection**: AI detection systems send data to `POST /api/traffic/data`
2. **Data Storage**: Raw measurements stored in `TrafficData` table
3. **Analytics Processing**: Periodic aggregation creates entries in `TrafficAnalytics`
4. **Peak Detection**: AI identifies peaks and stores them in `PeakHours`
5. **Incident Management**: Traffic incidents tracked in `TrafficIncidents`
6. **Predictions**: AI models generate forecasts stored in `TrafficPredictions`
7. **Frontend Display**: WebSocket real-time updates deliver data to Compose UI

## 🚀 Performance Considerations

- **Indexing**: Primary keys and foreign keys are automatically indexed
- **Partitioning**: Consider partitioning large tables by date for better performance
- **Archiving**: Implement data archiving strategy for old traffic data
- **Caching**: Use Redis or similar for frequently accessed data
- **Connection Pooling**: Exposed ORM handles connection pooling automatically

## 🔒 Security

- Database credentials stored in environment variables (recommended for production)
- Input validation on all API endpoints
- SQL injection prevention through ORM parameterized queries
- Rate limiting on API endpoints to prevent abuse