package com.gub.routes

import com.gub.models.LatencyPing
import com.gub.models.LatencyPong
import com.gub.services.StatusService
import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

fun Application.systemRoutes() {
    val statusService = StatusService()

    routing {
        webSocket("/ws/system-status") {
            println("Client connected")

            // Status sender loop
            launch(Dispatchers.IO) {
                while (true) {
                    val status = statusService.getStatus()
                    val json = Json.encodeToString(status)
                    send(Frame.Text(json))
                    delay(1000L)
                }
            }

            // Latency ping-pong listener
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val text = frame.readText()
                    try {
                        val ping = Json.decodeFromString<LatencyPing>(text)
                        if (ping.type == "ping") {
                            val now = System.currentTimeMillis()
                            val latency = now - ping.timestamp
                            statusService.latency = latency
                            val pong = LatencyPong(latency = latency)
                            send(Frame.Text(Json.encodeToString(pong)))
                        }
                    } catch (_: Exception) {
                        // Not a latency ping, ignore
                    }
                }
            }
        }
    }
}