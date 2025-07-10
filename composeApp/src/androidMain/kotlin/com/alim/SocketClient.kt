package com.alim

import android.util.Base64
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit

class SocketClient(private val serverUrl: String) {
    private val client = OkHttpClient.Builder()
        .pingInterval(10, TimeUnit.SECONDS)
        .build()

    private lateinit var webSocket: WebSocket

    fun connect() {
        val request = Request.Builder()
            .url(serverUrl)
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(ws: WebSocket, response: Response) {
                println("WebSocket Connected")
            }

            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                println("WebSocket Error: ${t.message}")
            }
        })
    }

    fun sendImageBytes(imageBytes: ByteArray) {
        webSocket.send(ByteString.of(*imageBytes))
    }

    fun close() {
        webSocket.close(1000, "Closed by client")
    }
}