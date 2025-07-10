package com.gub.routes

import com.gub.models.traffic.*
import com.gub.services.TrafficService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

fun Application.trafficRoutes() {
    val trafficService = TrafficService()
    val trafficClients = ConcurrentHashMap<DefaultWebSocketSession, String>()

    routing {
        route("/api/traffic") {
            // Get current traffic statistics
            get("/stats") {
                val cameraId = call.request.queryParameters["cameraId"] ?: "default"
                val stats = trafficService.getTrafficStats(cameraId)
                if (stats != null) {
                    call.respond(stats)
                } else {
                    call.respond(mapOf("error" to "No traffic data available"))
                }
            }

            // Get all traffic statistics
            get("/stats/all") {
                val allStats = trafficService.getAllTrafficStats()
                call.respond(allStats)
            }

            // Get traffic signals
            get("/signals") {
                val signals = trafficService.getTrafficSignals()
                call.respond(signals)
            }

            // Get specific traffic signal
            get("/signals/{id}") {
                val id = call.parameters["id"] ?: return@get call.respond(
                    mapOf("error" to "Signal ID required")
                )
                val signal = trafficService.getTrafficSignal(id)
                if (signal != null) {
                    call.respond(signal)
                } else {
                    call.respond(mapOf("error" to "Signal not found"))
                }
            }

            // Control traffic signal
            post("/signals/control") {
                try {
                    val control = call.receive<SignalControl>()
                    val success = trafficService.controlSignal(control)
                    call.respond(mapOf("success" to success))
                } catch (e: Exception) {
                    call.respond(mapOf("error" to e.message))
                }
            }

            // Update traffic signal
            put("/signals/{id}") {
                try {
                    val id = call.parameters["id"] ?: return@put call.respond(
                        mapOf("error" to "Signal ID required")
                    )
                    val signal = call.receive<TrafficSignal>()
                    trafficService.updateTrafficSignal(signal.copy(id = id))
                    call.respond(mapOf("success" to true))
                } catch (e: Exception) {
                    call.respond(mapOf("error" to e.message))
                }
            }

            // Detection service health
            get("/detection/health") {
                val healthy = trafficService.getDetectionServiceHealth()
                call.respond(mapOf(
                    "healthy" to healthy,
                    "service" to "vehicle-detection",
                    "timestamp" to System.currentTimeMillis()
                ))
            }
        }

        // WebSocket for real-time traffic updates
        webSocket("/ws/traffic") {
            val clientId = "client-${System.currentTimeMillis()}"
            trafficClients[this] = clientId
            println("Traffic client connected: $clientId")

            try {
                // Send initial data
                val initialStats = trafficService.getAllTrafficStats()
                send(Frame.Text(Json.encodeToString(TrafficUpdate(data = 
                    initialStats.values.firstOrNull() ?: TrafficStats(
                        vehicleCount = 0,
                        averageSpeed = 0.0,
                        congestionLevel = 0.0,
                        timestamp = System.currentTimeMillis()
                    )
                ))))

                // Send periodic updates
                launch {
                    while (true) {
                        try {
                            val stats = trafficService.getTrafficStats()
                            if (stats != null) {
                                val update = TrafficUpdate(data = stats)
                                send(Frame.Text(Json.encodeToString(update)))
                            }
                            delay(5000) // Update every 5 seconds
                        } catch (e: Exception) {
                            println("Error sending traffic update: ${e.message}")
                            break
                        }
                    }
                }

                // Handle incoming messages
                for (frame in incoming) {
                    if (frame is Frame.Text) {
                        val text = frame.readText()
                        try {
                            // Handle client commands if needed
                            println("Received traffic command: $text")
                        } catch (e: Exception) {
                            println("Error processing traffic command: ${e.message}")
                        }
                    }
                }
            } catch (e: Exception) {
                println("Traffic WebSocket error: ${e.message}")
            } finally {
                trafficClients.remove(this)
                println("Traffic client disconnected: $clientId")
            }
        }
    }
}