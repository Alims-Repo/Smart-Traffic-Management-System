package com.gub.models.traffic

import kotlinx.serialization.Serializable

/**
 * Data model for intersection/location information
 */
@Serializable
data class IntersectionModel(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val description: String? = null,
    val isActive: Boolean = true,
    val createdAt: String
)

/**
 * Data model for real-time traffic measurements
 */
@Serializable
data class TrafficDataModel(
    val id: Int,
    val intersectionId: Int,
    val timestamp: String,
    val vehicleCount: Int,
    val averageSpeed: Double,
    val congestionLevel: CongestionLevel,
    val temperature: Double? = null,
    val humidity: Int? = null,
    val visibility: Double? = null,
    val createdAt: String
)

/**
 * Data model for aggregated analytics metrics
 */
@Serializable
data class TrafficAnalyticsModel(
    val id: Int,
    val intersectionId: Int,
    val timestamp: String,
    val totalVehicles: Int,
    val averageSpeed: Double,
    val congestionLevel: Double,
    val incidentCount: Int,
    val peakHourIntensity: Double? = null,
    val aiOptimizedTiming: Boolean,
    val periodType: PeriodType,
    val createdAt: String
)

/**
 * Data model for peak traffic periods
 */
@Serializable
data class PeakHourModel(
    val id: Int,
    val intersectionId: Int,
    val startTime: String,
    val endTime: String,
    val period: String,
    val intensity: Int,
    val isActive: Boolean,
    val peakType: PeakType,
    val averageVolume: Int,
    val createdAt: String
)

/**
 * Data model for traffic incidents
 */
@Serializable
data class TrafficIncidentModel(
    val id: Int,
    val intersectionId: Int,
    val incidentType: IncidentType,
    val severity: IncidentSeverity,
    val description: String? = null,
    val startTime: String,
    val endTime: String? = null,
    val isResolved: Boolean,
    val affectedLanes: Int? = null,
    val estimatedDelay: Int? = null,
    val createdAt: String
)

/**
 * Data model for AI predictions
 */
@Serializable
data class TrafficPredictionModel(
    val id: Int,
    val intersectionId: Int,
    val predictionTimestamp: String,
    val predictedVehicleCount: Int,
    val predictedAverageSpeed: Double,
    val predictedCongestionLevel: Double,
    val confidence: Double,
    val predictionModel: String,
    val createdAt: String
)

/**
 * Request model for creating traffic data
 */
@Serializable
data class CreateTrafficDataRequest(
    val intersectionId: Int,
    val vehicleCount: Int,
    val averageSpeed: Double,
    val congestionLevel: CongestionLevel,
    val temperature: Double? = null,
    val humidity: Int? = null,
    val visibility: Double? = null
)

/**
 * Request model for creating traffic analytics
 */
@Serializable
data class CreateTrafficAnalyticsRequest(
    val intersectionId: Int,
    val totalVehicles: Int,
    val averageSpeed: Double,
    val congestionLevel: Double,
    val incidentCount: Int,
    val peakHourIntensity: Double? = null,
    val aiOptimizedTiming: Boolean,
    val periodType: PeriodType
)

/**
 * Enums for traffic data
 */
@Serializable
enum class CongestionLevel {
    LOW, MEDIUM, HIGH, CRITICAL
}

@Serializable
enum class PeriodType {
    HOURLY, DAILY, WEEKLY, MONTHLY
}

@Serializable
enum class PeakType {
    CURRENT, PREDICTED, HISTORICAL
}

@Serializable
enum class IncidentType {
    ACCIDENT, CONSTRUCTION, WEATHER, SIGNAL_FAILURE, CONGESTION, OTHER
}

@Serializable
enum class IncidentSeverity {
    LOW, MEDIUM, HIGH, CRITICAL
}