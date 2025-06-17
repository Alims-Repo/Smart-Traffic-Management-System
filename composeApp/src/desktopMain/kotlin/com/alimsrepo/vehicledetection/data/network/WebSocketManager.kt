/**
 * WebSocket manager for real-time communication
 * Author: Alims-Repo
 * Date: 2025-06-17
 */
package com.alimsrepo.vehicledetection.data.network

import com.alimsrepo.vehicledetection.data.model.DetectionResponse
import com.alimsrepo.vehicledetection.data.model.WebSocketCommand
import com.alimsrepo.vehicledetection.utils.Constants
import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

class WebSocketManager(
    private val url: String = Constants.DEFAULT_WEBSOCKET_URL
) {
    private val client = HttpClient(CIO) {
        install(WebSockets)
    }
    
    private val gson = Gson()
    private var session: DefaultClientWebSocketSession? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    
    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState
    
    private val _detectionData = MutableStateFlow<DetectionResponse?>(null)
    val detectionData: StateFlow<DetectionResponse?> = _detectionData
    
    private val commandChannel = Channel<WebSocketCommand>(Channel.UNLIMITED)
    
    enum class ConnectionState {
        CONNECTING, CONNECTED, DISCONNECTED, ERROR
    }
    
    fun connect() {
        scope.launch {
            try {
                _connectionState.value = ConnectionState.CONNECTING
                
                client.webSocket(url) {
                    session = this
                    _connectionState.value = ConnectionState.CONNECTED
                    
                    // Launch command sender
                    launch {
                        for (command in commandChannel) {
                            try {
                                val json = gson.toJson(command)
                                send(Frame.Text(json))
                            } catch (e: Exception) {
                                println("Error sending command: ${e.message}")
                            }
                        }
                    }
                    
                    // Listen for incoming messages
                    for (frame in incoming) {
                        if (frame is Frame.Text) {
                            handleMessage(frame.readText())
                        }
                    }
                }
            } catch (e: Exception) {
                _connectionState.value = ConnectionState.ERROR
                println("WebSocket connection error: ${e.message}")
                scheduleReconnect()
            }
        }
    }
    
    private fun handleMessage(message: String) {
        try {
            // Try to parse as detection response first
            val detectionResponse = gson.fromJson(message, DetectionResponse::class.java)
            if (detectionResponse.image.isNotEmpty()) {
                _detectionData.value = detectionResponse
                return
            }
        } catch (e: Exception) {
            // Not a detection response, might be a command response
            println("Received other message: $message")
        }
    }
    
    private fun scheduleReconnect() {
        scope.launch {
            delay(Constants.RECONNECT_INTERVAL_MS)
            if (_connectionState.value != ConnectionState.CONNECTED) {
                connect()
            }
        }
    }
    
    suspend fun sendCommand(command: String, data: Map<String, String>? = null) {
        val wsCommand = WebSocketCommand(command, data)
        commandChannel.trySend(wsCommand)
    }
    
    fun disconnect() {
        scope.cancel()
        session?.cancel()
        _connectionState.value = ConnectionState.DISCONNECTED
        client.close()
    }
    
    fun getStatsUpdates(): Flow<Map<String, Any>> = flow {
        while (true) {
            sendCommand(Constants.WS_COMMAND_GET_STATS)
            delay(Constants.STATS_UPDATE_INTERVAL_MS)
        }
    }
}