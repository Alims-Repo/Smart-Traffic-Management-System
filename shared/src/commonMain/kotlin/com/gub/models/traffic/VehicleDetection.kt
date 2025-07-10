package com.gub.models.traffic

import kotlinx.serialization.Serializable

@Serializable
data class VehicleDetection(
    val id: String,
    val type: String, // "car", "truck", "motorcycle", "bus"
    val confidence: Double,
    val boundingBox: BoundingBox,
    val timestamp: Long,
    val cameraId: String = "default"
)

@Serializable
data class BoundingBox(
    val x: Double,
    val y: Double,
    val width: Double,
    val height: Double
)

@Serializable
data class TrafficStats(
    val vehicleCount: Int,
    val averageSpeed: Double,
    val congestionLevel: Double, // 0.0 to 1.0
    val timestamp: Long,
    val cameraId: String = "default",
    val vehiclesByType: Map<String, Int> = emptyMap()
)

@Serializable
data class TrafficUpdate(
    val type: String = "traffic_update",
    val data: TrafficStats
)