package com.gub.application

import com.gub.SERVER_PORT
import com.gub.data.database.DatabaseConfig
import com.gub.di.dashboardModule
import com.gub.routes.dashboardRoute
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.cio.CIO
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import oshi.SystemInfo
import oshi.hardware.CentralProcessor.TickType
import io.ktor.server.websocket.WebSockets
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import org.koin.ktor.plugin.Koin

fun main() {
    embeddedServer(
        factory = Netty, port = SERVER_PORT,
        host = "0.0.0.0", module = Application::module
    ).start(wait = true)
}

//fun Application.module() {
//    install(WebSockets)
//    install(ContentNegotiation) {
//        json()
//    }
//
//    routing {
//        dashboardRoute()
//    }
//}

fun Application.module() {
    // Initialize database
    DatabaseConfig.init()

    // Websocket
    install(WebSockets)

    // Configure Koin
    install(Koin) {
        modules(dashboardModule)
    }

    // Configure JSON serialization
    install(ContentNegotiation) {
        json()
    }

    // Configure routing
    routing {
        dashboardRoute()
    }
}