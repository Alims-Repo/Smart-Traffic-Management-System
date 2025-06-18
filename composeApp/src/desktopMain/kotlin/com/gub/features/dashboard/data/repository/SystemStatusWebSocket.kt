package com.gub.features.dashboard.data.repository

import com.gub.core.domain.Response
import com.gub.models.ModelSystemStatus
import kotlinx.coroutines.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

class SystemStatusWebSocket(
    private val onMessageReceived: (Response<ModelSystemStatus>) -> Unit
) : WebSocketClient(URI("ws://localhost:8080/ws/system-status")) {

    override fun onOpen(handshakedata: ServerHandshake?) {
        println("✅ WebSocket Connected")
    }

    override fun onMessage(message: String?) {
        message?.let {
            try {
                val status = Json.decodeFromString<ModelSystemStatus>(it)
                onMessageReceived(Response.Success(status))
            } catch (e: Exception) {
                println("WebSocket Error: $e")
                Response.Error(e.toString())
            }
        }
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        println("WebSocket Disconnected. Code: $code, reason: $reason, remote: $remote")
        println("❌ WebSocket Closed: $reason")
    }

    override fun onError(ex: Exception?) {
        println("❌ WebSocket Error: ${ex?.message}")
    }
}