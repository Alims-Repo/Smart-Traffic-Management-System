package com.gub.application

import com.gub.SERVER_PORT
import com.gub.database.DatabaseFactory
import com.gub.routes.dashboardRoutes
import com.gub.routes.systemRoutes
import com.gub.routes.trafficRoutes
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.cio.CIO
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.http.*
import oshi.SystemInfo
import oshi.hardware.CentralProcessor.TickType
import io.ktor.server.websocket.WebSockets
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(
        factory = Netty, port = SERVER_PORT,
        host = "0.0.0.0", module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    install(WebSockets)
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
    
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowNonSimpleContentTypes = true
        anyHost() // For development - restrict in production
    }

    DatabaseFactory.init()
//    createSchema()

    systemRoutes()
    dashboardRoutes()
    trafficRoutes()
}