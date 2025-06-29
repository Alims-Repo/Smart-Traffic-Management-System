package com.gub.models.core

import kotlinx.serialization.Serializable

@Serializable
data class LatencyPing(
    val type: String = "ping",
    val timestamp: Long
)

@Serializable
data class LatencyPong(
    val type: String = "pong",
    val latency: Long
)