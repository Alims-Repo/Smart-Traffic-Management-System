package com.gub.services

import com.gub.models.traffic.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class TrafficService {
    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }
    
    private val detectionServiceUrl = System.getenv("DETECTION_SERVICE_URL") ?: "http://python-server:1234"
    private val trafficStats = ConcurrentHashMap<String, TrafficStats>()
    private val trafficSignals = ConcurrentHashMap<String, TrafficSignal>()
    
    init {
        // Initialize with default traffic signals
        initializeDefaultSignals()
    }
    
    private fun initializeDefaultSignals() {
        val defaultSignal = TrafficSignal(
            id = "signal-001",
            name = "Main Street & Oak Avenue",
            location = Location(40.7128, -74.0060, "Main Street & Oak Avenue"),
            currentPhase = SignalPhase("phase-1", "North-South Green", 30, "green", "north-south"),
            phases = listOf(
                SignalPhase("phase-1", "North-South Green", 30, "green", "north-south"),
                SignalPhase("phase-2", "North-South Yellow", 5, "yellow", "north-south"),
                SignalPhase("phase-3", "East-West Green", 25, "green", "east-west"),
                SignalPhase("phase-4", "East-West Yellow", 5, "yellow", "east-west")
            ),
            status = "active",
            lastUpdated = System.currentTimeMillis()
        )
        trafficSignals[defaultSignal.id] = defaultSignal
    }
    
    suspend fun getTrafficStats(cameraId: String = "default"): TrafficStats? {
        return try {
            // Try to get live data from detection service
            val response = httpClient.get("$detectionServiceUrl/stats")
            response.body<TrafficStats>()
        } catch (e: Exception) {
            // Fallback to cached data or default
            trafficStats[cameraId] ?: TrafficStats(
                vehicleCount = 0,
                averageSpeed = 0.0,
                congestionLevel = 0.0,
                timestamp = System.currentTimeMillis(),
                cameraId = cameraId
            )
        }
    }
    
    suspend fun getAllTrafficStats(): Map<String, TrafficStats> {
        return try {
            val response = httpClient.get("$detectionServiceUrl/api")
            val apiInfo = response.body<Map<String, Any>>()
            
            // For now, return default stats
            mapOf("default" to (getTrafficStats() ?: TrafficStats(
                vehicleCount = 0,
                averageSpeed = 0.0,
                congestionLevel = 0.0,
                timestamp = System.currentTimeMillis()
            )))
        } catch (e: Exception) {
            trafficStats.toMap()
        }
    }
    
    fun updateTrafficStats(stats: TrafficStats) {
        trafficStats[stats.cameraId] = stats
    }
    
    fun getTrafficSignals(): List<TrafficSignal> {
        return trafficSignals.values.toList()
    }
    
    fun getTrafficSignal(id: String): TrafficSignal? {
        return trafficSignals[id]
    }
    
    fun updateTrafficSignal(signal: TrafficSignal) {
        trafficSignals[signal.id] = signal.copy(lastUpdated = System.currentTimeMillis())
    }
    
    suspend fun controlSignal(control: SignalControl): Boolean {
        val signal = trafficSignals[control.signalId] ?: return false
        
        when (control.action) {
            "optimize" -> {
                // Implement optimization logic based on current traffic
                val stats = getTrafficStats()
                if (stats != null && stats.congestionLevel > 0.7) {
                    // Extend green phase for high congestion direction
                    val optimizedPhases = signal.phases.map { phase ->
                        if (phase.state == "green") {
                            phase.copy(duration = (phase.duration * 1.5).toInt())
                        } else phase
                    }
                    updateTrafficSignal(signal.copy(phases = optimizedPhases))
                }
            }
            "manual" -> {
                // Manual control implementation
                val phaseId = control.parameters["phaseId"]
                if (phaseId != null) {
                    val newPhase = signal.phases.find { it.id == phaseId }
                    if (newPhase != null) {
                        updateTrafficSignal(signal.copy(currentPhase = newPhase))
                    }
                }
            }
            "emergency" -> {
                // Emergency override - set all to red except emergency direction
                val emergencyDirection = control.parameters["direction"] ?: "north-south"
                val emergencyPhase = SignalPhase(
                    "emergency", "Emergency Override", 60, "green", emergencyDirection
                )
                updateTrafficSignal(signal.copy(currentPhase = emergencyPhase))
            }
        }
        
        return true
    }
    
    suspend fun getDetectionServiceHealth(): Boolean {
        return try {
            val response = httpClient.get("$detectionServiceUrl/health")
            response.status.value in 200..299
        } catch (e: Exception) {
            false
        }
    }
}