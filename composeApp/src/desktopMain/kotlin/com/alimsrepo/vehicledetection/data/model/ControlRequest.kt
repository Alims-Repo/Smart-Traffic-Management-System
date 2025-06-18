/**
 * Control request models
 * Author: Alims-Repo
 * Date: 2025-06-17
 */
package com.alimsrepo.vehicledetection.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PlaybackControlRequest(
    val action: String, // "pause", "resume", "restart", "seek"
    val frame: Int? = null // For seek action
)

@Serializable
data class DetectionControlRequest(
    val confidence_threshold: Double? = null,
    val iou_threshold: Double? = null,
    val max_detections: Int? = null,
    val vehicle_classes: List<String>? = null
)

@Serializable
data class BroadcastControlRequest(
    val action: String // "start", "stop", "disconnect_all"
)

@Serializable
data class DeviceControlRequest(
    val device: String // "cpu", "mps"
)

@Serializable
data class ConfigUpdateRequest(
    val confidence_threshold: Double? = null,
    val target_fps: Int? = null,
    val jpeg_quality: Int? = null
)

@Serializable
data class WebSocketCommand(
    val command: String,
    val data: Map<String, String>? = null
)

@Serializable
data class ControlResponse(
    val status: String,
    val message: String? = null,
    val updated: Map<String, Any>? = null,
    val timestamp: Double? = null
)