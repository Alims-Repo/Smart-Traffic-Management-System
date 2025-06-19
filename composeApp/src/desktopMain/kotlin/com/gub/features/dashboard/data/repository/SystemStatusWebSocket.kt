package com.gub.features.dashboard.data.repository

import com.gub.core.domain.Response
import com.gub.models.ModelSystemStatus
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

class SystemStatusWebSocket(
    private val onMessageReceived: (Response<ModelSystemStatus>) -> Unit
) : WebSocketClient(URI("ws://localhost:8080/ws/system-status")) {

    private var lastPingTime: Long = 0L

    override fun onOpen(handshakedata: ServerHandshake?) {
        println("✅ WebSocket Connected")
        sendPingMessage() // Send first ping after connected
    }

    override fun onMessage(message: String?) {
        message?.let {
            try {
                val status = Json.decodeFromString<ModelSystemStatus>(it)

                // Calculate latency if field is null
                val updatedStatus = status.copy(
                    latencyMs = System.currentTimeMillis() - status.timestamp
                )

                onMessageReceived(Response.Success(updatedStatus))

                // Ping again after handling
                sendPingMessage()
            } catch (e: Exception) {
                println("WebSocket Error: $e")
                onMessageReceived(Response.Error(e.toString()))
            }
        }
    }

    private fun sendPingMessage() {
        lastPingTime = System.currentTimeMillis()
        try {
            send("ping:$lastPingTime")
        } catch (e: Exception) {
            println("❌ Ping Failed: ${e.message}")
        }
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        println("❌ WebSocket Closed. Code: $code, reason: $reason, remote: $remote")
    }

    override fun onError(ex: Exception?) {
        println("❌ WebSocket Error: ${ex?.message}")
    }
}