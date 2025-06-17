/**
 * Control screen view model
 * Author: Alims-Repo
 * Date: 2025-06-17
 */
package com.alimsrepo.vehicledetection.ui.viewmodel

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import com.alimsrepo.vehicledetection.data.model.*
import com.alimsrepo.vehicledetection.data.repository.VehicleDetectionRepository

class ControlViewModel(
    private val repository: VehicleDetectionRepository
) {
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val _currentConfig = MutableStateFlow<ConfigurationStats?>(null)
    val currentConfig: StateFlow<ConfigurationStats?> = _currentConfig.asStateFlow()

    suspend fun loadCurrentConfig() {
        repository.getConfig().getOrNull()?.let { config ->
            _currentConfig.value = config
        }
    }

    suspend fun updateConfig(updates: Map<String, Any>) {
        val request = ConfigUpdateRequest(
            confidence_threshold = updates["confidence_threshold"] as? Double,
            target_fps = updates["target_fps"] as? Int,
            jpeg_quality = updates["jpeg_quality"] as? Int
        )

        repository.updateConfig(request).getOrNull()?.let {
            // Reload config after update
            loadCurrentConfig()
        }
    }

    suspend fun controlPlayback(action: String) {
        repository.controlPlayback(action)
    }

    suspend fun switchDevice(device: String) {
        repository.controlDevice(device)
    }

    suspend fun controlBroadcast(action: String) {
        repository.controlBroadcast(action)
    }

    fun cleanup() {
        scope.cancel()
    }
}