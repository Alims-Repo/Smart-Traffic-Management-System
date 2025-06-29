package com.gub.di

import com.gub.app.Const.BASE_URL
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object NetworkModule {

    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = true
                    encodeDefaults = true
                })
            }

            install(Logging) {
                level = LogLevel.INFO // Change to LogLevel.ALL for debugging
                logger = Logger.DEFAULT
            }

            install(WebSockets) {
                pingInterval = 20_000
            }

            install(DefaultRequest) {
                url("http://$BASE_URL/") // Default server URL
                headers.append("Content-Type", "application/json")
                headers.append("Accept", "application/json")
            }

            // Configure timeouts
            engine {
                requestTimeout = 30_000
            }
        }
    }
}