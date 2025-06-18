package com.gub.models

import kotlinx.serialization.Serializable

@Serializable
data class SystemStatus(
    val backendStatus: String,
    val aiServiceStatus: String,
    val trafficSignalStatus: String,
    val timestamp: String,
    val cpuUsage: Double,
    val usedMemoryMb: Long,
    val totalMemoryMb: Long,
    val networkStats: List<NetworkInfo>
)