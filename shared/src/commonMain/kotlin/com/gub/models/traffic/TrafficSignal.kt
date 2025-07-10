package com.gub.models.traffic

import kotlinx.serialization.Serializable

@Serializable
data class TrafficSignal(
    val id: String,
    val name: String,
    val location: Location,
    val currentPhase: SignalPhase,
    val phases: List<SignalPhase>,
    val status: String, // "active", "maintenance", "offline"
    val lastUpdated: Long
)

@Serializable
data class SignalPhase(
    val id: String,
    val name: String,
    val duration: Int, // seconds
    val state: String, // "red", "yellow", "green"
    val direction: String // "north-south", "east-west", "all"
)

@Serializable
data class Location(
    val latitude: Double,
    val longitude: Double,
    val address: String = ""
)

@Serializable
data class SignalControl(
    val signalId: String,
    val action: String, // "optimize", "manual", "emergency"
    val parameters: Map<String, String> = emptyMap()
)