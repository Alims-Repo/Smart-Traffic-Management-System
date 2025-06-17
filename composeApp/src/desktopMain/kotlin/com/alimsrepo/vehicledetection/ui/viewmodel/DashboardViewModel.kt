/**
 * Dashboard view model
 * Author: Alims-Repo
 * Date: 2025-06-17
 */
package com.alimsrepo.vehicledetection.ui.viewmodel

import com.alimsrepo.vehicledetection.data.model.DetectionStats
import com.alimsrepo.vehicledetection.data.model.HealthResponse
import com.alimsrepo.vehicledetection.data.model.SystemInfo
import com.alimsrepo.vehicledetection.data.repository.VehicleDetectionRepository
import com.alimsrepo.vehicledetection.utils.Constants
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DashboardViewModel(
    private val repository: VehicleDetectionRepository
) {
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val _healthStatus = MutableStateFlow<HealthResponse?>(null)
    val healthStatus: StateFlow<HealthResponse?> = _healthStatus.asStateFlow()

    private val _performanceStats = MutableStateFlow<DetectionStats?>(null)
    val performanceStats: StateFlow<DetectionStats?> = _performanceStats.asStateFlow()

    private val _systemInfo = MutableStateFlow<SystemInfo?>(null)
    val systemInfo: StateFlow<SystemInfo?> = _systemInfo.asStateFlow()

    private var updateJob: Job? = null

    fun startUpdates() {
        updateJob?.cancel()
        updateJob = scope.launch {
            while (isActive) {
                refreshData()
                delay(Constants.STATS_UPDATE_INTERVAL_MS)
            }
        }
    }

    suspend fun refreshData() {
        // Fetch health status
        repository.getHealth().getOrNull()?.let { health ->
            _healthStatus.value = health
        }

        // Fetch performance stats
        repository.getPerformance().getOrNull()?.let { performance ->
            _performanceStats.value = performance
        }

        // Request system info via WebSocket
        repository.getSystemInfo()
    }

    fun cleanup() {
        updateJob?.cancel()
        scope.cancel()
    }
}