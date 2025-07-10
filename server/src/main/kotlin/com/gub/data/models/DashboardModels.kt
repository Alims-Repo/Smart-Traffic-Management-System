package com.gub.data.models

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class AiControlMetricEntity(
    val id: Long = 0,
    val efficiency: Double,
    val runningModel: Int,
    val decisionSpeed: Int,
    val timestamp: String = LocalDateTime.now().toString()
)

@Serializable
data class TrafficMetricEntity(
    val id: Long = 0,
    val vehicleCount: Int,
    val vehicleDifference: Int,
    val vehicleUpwards: Boolean,
    val congestionCount: Int,
    val congestionDifference: Int,
    val congestionUpwards: Boolean,
    val timestamp: String = LocalDateTime.now().toString()
)

@Serializable
data class SystemMetricEntity(
    val id: Long = 0,
    val systemHealth: Double,
    val aiResponseTime: Double,
    val avgWaitTime: Double,
    val currentFlow: Double,
    val timestamp: String = LocalDateTime.now().toString()
)