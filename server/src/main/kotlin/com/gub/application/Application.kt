package com.gub.application

import com.gub.SERVER_PORT
import com.gub.routes.systemRoutes
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import oshi.SystemInfo
import oshi.hardware.CentralProcessor.TickType
import io.ktor.server.websocket.WebSockets
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

fun main() {
    embeddedServer(
        factory = Netty, port = SERVER_PORT,
        host = "0.0.0.0", module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    install(WebSockets)
    install(ContentNegotiation) {
        json()
    }

    systemRoutes()
}