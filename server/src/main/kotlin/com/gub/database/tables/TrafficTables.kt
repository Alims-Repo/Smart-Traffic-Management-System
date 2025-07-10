package com.gub.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

/**
 * Table for storing intersection/location data
 */
object Intersections : IntIdTable() {
    val name = varchar("name", 100)
    val latitude = decimal("latitude", 10, 8)
    val longitude = decimal("longitude", 11, 8)
    val description = text("description").nullable()
    val isActive = bool("is_active").default(true)
    val createdAt = datetime("created_at").default(LocalDateTime.now())
}

/**
 * Table for storing real-time traffic measurements
 */
object TrafficData : IntIdTable() {
    val intersectionId = reference("intersection_id", Intersections)
    val timestamp = datetime("timestamp")
    val vehicleCount = integer("vehicle_count")
    val averageSpeed = decimal("average_speed", 5, 2)
    val congestionLevel = enumerationByName("congestion_level", 20, DbCongestionLevel::class)
    val temperature = decimal("temperature", 5, 2).nullable()
    val humidity = integer("humidity").nullable()
    val visibility = decimal("visibility", 5, 2).nullable()
    val createdAt = datetime("created_at").default(LocalDateTime.now())
}

/**
 * Table for storing aggregated analytics metrics
 */
object TrafficAnalytics : IntIdTable() {
    val intersectionId = reference("intersection_id", Intersections)
    val timestamp = datetime("timestamp")
    val totalVehicles = integer("total_vehicles")
    val averageSpeed = decimal("average_speed", 5, 2)
    val congestionLevel = decimal("congestion_level", 3, 2)
    val incidentCount = integer("incident_count").default(0)
    val peakHourIntensity = decimal("peak_hour_intensity", 3, 2).nullable()
    val aiOptimizedTiming = bool("ai_optimized_timing").default(false)
    val periodType = enumerationByName("period_type", 20, DbPeriodType::class)
    val createdAt = datetime("created_at").default(LocalDateTime.now())
}

/**
 * Table for storing peak traffic periods
 */
object PeakHours : IntIdTable() {
    val intersectionId = reference("intersection_id", Intersections)
    val startTime = datetime("start_time")
    val endTime = datetime("end_time")
    val period = varchar("period", 50) // e.g., "Morning Rush", "Evening Rush"
    val intensity = integer("intensity") // 1-10 scale
    val isActive = bool("is_active")
    val peakType = enumerationByName("peak_type", 20, DbPeakType::class)
    val averageVolume = integer("average_volume")
    val createdAt = datetime("created_at").default(LocalDateTime.now())
}

/**
 * Table for storing traffic incidents and congestion events
 */
object TrafficIncidents : IntIdTable() {
    val intersectionId = reference("intersection_id", Intersections)
    val incidentType = enumerationByName("incident_type", 30, DbIncidentType::class)
    val severity = enumerationByName("severity", 20, DbIncidentSeverity::class)
    val description = text("description").nullable()
    val startTime = datetime("start_time")
    val endTime = datetime("end_time").nullable()
    val isResolved = bool("is_resolved").default(false)
    val affectedLanes = integer("affected_lanes").nullable()
    val estimatedDelay = integer("estimated_delay_minutes").nullable()
    val createdAt = datetime("created_at").default(LocalDateTime.now())
}

/**
 * Table for storing AI predictions for future traffic patterns
 */
object TrafficPredictions : IntIdTable() {
    val intersectionId = reference("intersection_id", Intersections)
    val predictionTimestamp = datetime("prediction_timestamp") // When the prediction is for
    val predictedVehicleCount = integer("predicted_vehicle_count")
    val predictedAverageSpeed = decimal("predicted_average_speed", 5, 2)
    val predictedCongestionLevel = decimal("predicted_congestion_level", 3, 2)
    val confidence = decimal("confidence", 3, 2) // 0.0 to 1.0
    val predictionModel = varchar("prediction_model", 50) // e.g., "LSTM", "ARIMA"
    val createdAt = datetime("created_at").default(LocalDateTime.now())
}

/**
 * Database enums (prefixed with Db to avoid conflicts)
 */
enum class DbCongestionLevel {
    LOW, MEDIUM, HIGH, CRITICAL
}

enum class DbPeriodType {
    HOURLY, DAILY, WEEKLY, MONTHLY
}

enum class DbPeakType {
    CURRENT, PREDICTED, HISTORICAL
}

enum class DbIncidentType {
    ACCIDENT, CONSTRUCTION, WEATHER, SIGNAL_FAILURE, CONGESTION, OTHER
}

enum class DbIncidentSeverity {
    LOW, MEDIUM, HIGH, CRITICAL
}