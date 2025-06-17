/**
 * Fixed Control screen view model with proper error handling
 * Author: Alims-Repo
 * Date: 2025-06-17
 */
package com.alimsrepo.vehicledetection.ui.viewmodel

import com.alimsrepo.vehicledetection.data.model.ConfigUpdateRequest
import com.alimsrepo.vehicledetection.data.model.ConfigurationStats
import com.alimsrepo.vehicledetection.data.repository.VehicleDetectionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ControlViewModel(
    private val repository: VehicleDetectionRepository
) {
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val _currentConfig = MutableStateFlow<ConfigurationStats?>(null)
    val currentConfig: StateFlow<ConfigurationStats?> = _currentConfig.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    suspend fun loadCurrentConfig() {
        _isLoading.value = true
        try {
            println("🔄 Loading current configuration...")
            val result = repository.getConfig()
            result.fold(
                onSuccess = { config ->
                    _currentConfig.value = config
                    _message.value = "Configuration loaded successfully"
                    println("✅ Config loaded: $config")
                },
                onFailure = { error ->
                    _message.value = "Failed to load config: ${error.message}"
                    println("❌ Config load failed: ${error.message}")
                }
            )
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun updateConfig(updates: Map<String, Any>) {
        _isLoading.value = true
        try {
            println("🔄 Updating configuration: $updates")

            val request = ConfigUpdateRequest(
                confidence_threshold = updates["confidence_threshold"] as? Double,
                target_fps = updates["target_fps"] as? Int,
                jpeg_quality = updates["jpeg_quality"] as? Int
            )

            val result = repository.updateConfig(request)
            result.fold(
                onSuccess = { response ->
                    _message.value = "Configuration updated successfully"
                    println("✅ Config updated: $response")
                    // Reload config after update
                    loadCurrentConfig()
                },
                onFailure = { error ->
                    _message.value = "Failed to update config: ${error.message}"
                    println("❌ Config update failed: ${error.message}")
                }
            )
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun controlPlayback(action: String) {
        try {
            println("🔄 Playback control: $action")
            val result = repository.controlPlayback(action)
            result.fold(
                onSuccess = { response ->
                    _message.value = "Playback $action successful"
                    println("✅ Playback $action: $response")
                },
                onFailure = { error ->
                    _message.value = "Playback control failed: ${error.message}"
                    println("❌ Playback control failed: ${error.message}")
                }
            )
        } catch (e: Exception) {
            _message.value = "Playback control error: ${e.message}"
            println("❌ Playback control exception: ${e.message}")
        }
    }

    suspend fun switchDevice(device: String) {
        try {
            println("🔄 Switching device to: $device")
            val result = repository.controlDevice(device)
            result.fold(
                onSuccess = { response ->
                    _message.value = "Device switched to ${device.uppercase()}"
                    println("✅ Device switched: $response")
                },
                onFailure = { error ->
                    _message.value = "Device switch failed: ${error.message}"
                    println("❌ Device switch failed: ${error.message}")
                }
            )
        } catch (e: Exception) {
            _message.value = "Device switch error: ${e.message}"
            println("❌ Device switch exception: ${e.message}")
        }
    }

    suspend fun controlBroadcast(action: String) {
        try {
            println("🔄 Broadcast control: $action")
            val result = repository.controlBroadcast(action)
            result.fold(
                onSuccess = { response ->
                    _message.value = "Broadcast $action successful"
                    println("✅ Broadcast $action: $response")
                },
                onFailure = { error ->
                    _message.value = "Broadcast control failed: ${error.message}"
                    println("❌ Broadcast control failed: ${error.message}")
                }
            )
        } catch (e: Exception) {
            _message.value = "Broadcast control error: ${e.message}"
            println("❌ Broadcast control exception: ${e.message}")
        }
    }

    fun clearMessage() {
        _message.value = null
    }

    fun cleanup() {
        scope.cancel()
    }
}