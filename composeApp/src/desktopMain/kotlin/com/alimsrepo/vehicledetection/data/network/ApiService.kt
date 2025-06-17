/**
 * API service for HTTP requests
 * Author: Alims-Repo
 * Date: 2025-06-17
 */
package com.alimsrepo.vehicledetection.data.network

import com.alimsrepo.vehicledetection.data.model.*
import com.alimsrepo.vehicledetection.utils.Constants
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

class ApiService(
    private val baseUrl: String = Constants.DEFAULT_SERVER_URL
) {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        install(Logging) {
            level = LogLevel.INFO
        }
    }
    
    suspend fun getHealth(): Result<HealthResponse> = try {
        val response = client.get("$baseUrl${Constants.ENDPOINT_HEALTH}")
        Result.success(response.body())
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    suspend fun getStats(): Result<StatsResponse> = try {
        val response = client.get("$baseUrl${Constants.ENDPOINT_STATS}")
        Result.success(response.body())
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    suspend fun getPerformance(): Result<DetectionStats> = try {
        val response = client.get("$baseUrl${Constants.ENDPOINT_PERFORMANCE}")
        Result.success(response.body())
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    suspend fun getConfig(): Result<ConfigurationStats> = try {
        val response = client.get("$baseUrl${Constants.ENDPOINT_CONFIG}")
        Result.success(response.body())
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    suspend fun updateConfig(request: ConfigUpdateRequest): Result<ControlResponse> = try {
        val response = client.post("$baseUrl${Constants.ENDPOINT_CONFIG}") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        Result.success(response.body())
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    suspend fun controlPlayback(request: PlaybackControlRequest): Result<ControlResponse> = try {
        val response = client.post("$baseUrl${Constants.ENDPOINT_CONTROL_PLAYBACK}") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        Result.success(response.body())
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    suspend fun controlDetection(request: DetectionControlRequest): Result<ControlResponse> = try {
        val response = client.post("$baseUrl${Constants.ENDPOINT_CONTROL_DETECTION}") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        Result.success(response.body())
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    suspend fun controlBroadcast(request: BroadcastControlRequest): Result<ControlResponse> = try {
        val response = client.post("$baseUrl${Constants.ENDPOINT_CONTROL_BROADCAST}") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        Result.success(response.body())
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    suspend fun controlDevice(request: DeviceControlRequest): Result<ControlResponse> = try {
        val response = client.post("$baseUrl${Constants.ENDPOINT_CONTROL_DEVICE}") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        Result.success(response.body())
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    fun close() {
        client.close()
    }
}