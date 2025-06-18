/**
 * Repository for vehicle detection data
 * Author: Alims-Repo
 * Date: 2025-06-17
 */
package com.alimsrepo.vehicledetection.data.repository

import com.alimsrepo.vehicledetection.data.model.*
import com.alimsrepo.vehicledetection.data.network.ApiService
import com.alimsrepo.vehicledetection.data.network.WebSocketManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

class VehicleDetectionRepository(
    private val apiService: ApiService,
    private val webSocketManager: WebSocketManager
) {
    
    // WebSocket data streams
    val connectionState: StateFlow<WebSocketManager.ConnectionState> = webSocketManager.connectionState
    val detectionData: StateFlow<DetectionResponse?> = webSocketManager.detectionData
    
    // Connection management
    fun connect() = webSocketManager.connect()
    fun disconnect() = webSocketManager.disconnect()
    
    // API calls
    suspend fun getHealth() = apiService.getHealth()
    suspend fun getStats() = apiService.getStats()
    suspend fun getPerformance() = apiService.getPerformance()
    suspend fun getConfig() = apiService.getConfig()
    
    // Control operations
    suspend fun updateConfig(request: ConfigUpdateRequest) = apiService.updateConfig(request)
    suspend fun controlPlayback(action: String, frame: Int? = null) = 
        apiService.controlPlayback(PlaybackControlRequest(action, frame))
    
    suspend fun controlDetection(
        confidenceThreshold: Double? = null,
        iouThreshold: Double? = null,
        maxDetections: Int? = null,
        vehicleClasses: List<String>? = null
    ) = apiService.controlDetection(
        DetectionControlRequest(confidenceThreshold, iouThreshold, maxDetections, vehicleClasses)
    )
    
    suspend fun controlBroadcast(action: String) = 
        apiService.controlBroadcast(BroadcastControlRequest(action))
    
    suspend fun controlDevice(device: String) = 
        apiService.controlDevice(DeviceControlRequest(device))
    
    // WebSocket commands
    suspend fun sendWebSocketCommand(command: String, data: Map<String, String>? = null) {
        webSocketManager.sendCommand(command, data)
    }
    
    suspend fun resetStats() {
        webSocketManager.sendCommand("reset_stats")
    }
    
    suspend fun getSystemInfo() {
        webSocketManager.sendCommand("get_system_info")
    }
    
    fun getStatsUpdates(): Flow<Map<String, Any>> = webSocketManager.getStatsUpdates()
    
    fun cleanup() {
        webSocketManager.disconnect()
        apiService.close()
    }
}