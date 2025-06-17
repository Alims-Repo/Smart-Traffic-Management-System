/**
 * Data models for detection responses
 * Author: Alims-Repo
 * Date: 2025-06-17
 */
package com.alimsrepo.vehicledetection.data.model

import kotlinx.serialization.Serializable

@Serializable
data class DetectionResponse(
    val image: String,
    val vehicleCount: Int,
    val timestamp: Double,
    val device: String,
    val frameCount: Int? = null,
    val performance: PerformanceData? = null,
    val metadata: MetaData? = null,
    val m1ProOptimized: Boolean? = null
)

@Serializable
data class PerformanceData(
    val avgDetectionTime: Double,
    val detectionFps: Double,
    val broadcastFps: Double
)

@Serializable
data class MetaData(
    val batchSize: Int,
    val clients: Int,
    val m1ProOptimized: Boolean
)

@Serializable
data class StatsResponse(
    val video: VideoStats,
    val clients: ClientStats,
    val performance: PerformanceStats,
    val configuration: ConfigurationStats
)

@Serializable
data class VideoStats(
    val path: String,
    val frame_count: Int,
    val current_vehicles: Int,
    val is_running: Boolean
)

@Serializable
data class ClientStats(
    val connected: Int,
    val total_connected: Int
)

@Serializable
data class PerformanceStats(
    val detection: DetectionStats,
    val broadcast: BroadcastStats,
    val device: String,
    val batch_size: Int
)

@Serializable
data class DetectionStats(
    val samples: Int,
    val avg_time: Double,
    val avg_count: Double,
    val fps: Double,
    val min_time: Double,
    val max_time: Double,
    val recent_times: List<Double>? = null,
    val device: String? = null,
    val model_name: String? = null,
    val input_size: Int? = null,
    val confidence_threshold: Double? = null,
    val vehicle_classes: List<String>? = null
)

@Serializable
data class BroadcastStats(
    val samples: Int,
    val avg_time: Double,
    val fps: Double,
    val min_time: Double,
    val max_time: Double
)

@Serializable
data class ConfigurationStats(
    val target_fps: Int,
    val jpeg_quality: Int,
    val input_size: Int,
    val confidence_threshold: Double
)

@Serializable
data class SystemInfo(
    val device: String,
    val mpsAvailable: Boolean,
    val vehicleClasses: List<String>,
    val inputSize: Int,
    val confidence: Double,
    val version: String? = null,
    val author: String? = null
)

@Serializable
data class HealthResponse(
    val status: String,
    val device: String,
    val mpsAvailable: Boolean,
    val detectionEnabled: Boolean,
    val clients: Int,
    val timestamp: Double,
    val version: String? = null,
    val uptime: Double? = null
)