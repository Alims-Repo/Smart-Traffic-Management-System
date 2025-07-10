package com.gub.routes

import com.gub.models.traffic.TrafficDataModel
import com.gub.services.traffic.TrafficDataService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

/**
 * WebSocket routes for real-time traffic data updates
 */
fun Application.trafficWebSocketRoutes() {
    val trafficService = TrafficDataService()
    
    routing {
        webSocket("/ws/traffic-updates") {
            println("Client connected to traffic updates WebSocket")
            
            // Send periodic traffic updates
            launch(Dispatchers.IO) {
                while (true) {
                    try {
                        // Get latest traffic data for all intersections
                        val latestTrafficData = trafficService.getLatestTrafficData()
                        
                        // Send traffic data update
                        val updateMessage = TrafficUpdateMessage(
                            type = "traffic_data_update",
                            data = latestTrafficData,
                            timestamp = System.currentTimeMillis()
                        )
                        
                        val json = Json.encodeToString(updateMessage)
                        send(Frame.Text(json))
                        
                        // Send update every 30 seconds
                        delay(30_000L)
                        
                    } catch (e: Exception) {
                        println("Error sending traffic update: ${e.message}")
                        // Continue the loop even if there's an error
                        delay(5_000L)
                    }
                }
            }
            
            // Handle incoming messages (for potential client commands)
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val text = frame.readText()
                    try {
                        println("Received WebSocket message: $text")
                        // Could handle client requests for specific intersection data
                        // For now, just acknowledge
                        send(Frame.Text("""{"type": "acknowledgment", "message": "Message received"}"""))
                    } catch (e: Exception) {
                        println("Error processing WebSocket message: ${e.message}")
                    }
                }
            }
            
            println("Client disconnected from traffic updates WebSocket")
        }
        
        webSocket("/ws/traffic-alerts") {
            println("Client connected to traffic alerts WebSocket")
            
            // Send traffic alerts and incidents
            launch(Dispatchers.IO) {
                while (true) {
                    try {
                        // Get latest traffic data to check for high congestion
                        val latestTrafficData = trafficService.getLatestTrafficData()
                        
                        // Check for high congestion alerts
                        val highCongestionAlerts = latestTrafficData.filter { 
                            it.congestionLevel == com.gub.models.traffic.CongestionLevel.HIGH || 
                            it.congestionLevel == com.gub.models.traffic.CongestionLevel.CRITICAL 
                        }
                        
                        if (highCongestionAlerts.isNotEmpty()) {
                            val alertMessage = TrafficAlertMessage(
                                type = "congestion_alert",
                                severity = "HIGH",
                                message = "High congestion detected at ${highCongestionAlerts.size} intersection(s)",
                                affectedIntersections = highCongestionAlerts.map { it.intersectionId },
                                timestamp = System.currentTimeMillis()
                            )
                            
                            val json = Json.encodeToString(alertMessage)
                            send(Frame.Text(json))
                        }
                        
                        // Check for alerts every 60 seconds
                        delay(60_000L)
                        
                    } catch (e: Exception) {
                        println("Error sending traffic alert: ${e.message}")
                        delay(10_000L)
                    }
                }
            }
            
            // Handle incoming messages
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val text = frame.readText()
                    println("Received alert subscription message: $text")
                }
            }
            
            println("Client disconnected from traffic alerts WebSocket")
        }
    }
}

/**
 * Data class for traffic update messages
 */
@kotlinx.serialization.Serializable
data class TrafficUpdateMessage(
    val type: String,
    val data: List<TrafficDataModel>,
    val timestamp: Long
)

/**
 * Data class for traffic alert messages
 */
@kotlinx.serialization.Serializable
data class TrafficAlertMessage(
    val type: String,
    val severity: String,
    val message: String,
    val affectedIntersections: List<Int>,
    val timestamp: Long
)