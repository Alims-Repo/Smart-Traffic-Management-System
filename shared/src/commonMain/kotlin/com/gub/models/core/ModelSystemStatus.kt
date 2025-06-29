package com.gub.models.core

import kotlinx.serialization.Serializable

@Serializable
data class ModelSystemStatus(
    val backendStatus: String,
    val aiServiceStatus: String,
    val trafficSignalStatus: String,
    val timestamp: Long,
    val cpuUsage: Int,
    val usedMemoryMb: Long,
    val totalMemoryMb: Long,
    val networkStats: List<NetworkInfo>,
    val latencyMs: Long
)