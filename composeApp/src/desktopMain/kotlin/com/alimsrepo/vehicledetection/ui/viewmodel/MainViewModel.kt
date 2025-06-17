/**
 * Main screen view model
 * Author: Alims-Repo
 * Date: 2025-06-17
 */
package com.alimsrepo.vehicledetection.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import com.alimsrepo.vehicledetection.data.model.StatsResponse
import com.alimsrepo.vehicledetection.data.repository.VehicleDetectionRepository
import com.alimsrepo.vehicledetection.utils.Constants

class MainViewModel(
    private val repository: VehicleDetectionRepository
) {
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val _statsResponse = MutableStateFlow<StatsResponse?>(null)
    val statsResponse: StateFlow<StatsResponse?> = _statsResponse.asStateFlow()

    private val _isPlaying = MutableStateFlow(true)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private var statsUpdateJob: Job? = null

    fun startStatsUpdates() {
        statsUpdateJob?.cancel()
        statsUpdateJob = scope.launch {
            while (isActive) {
                try {
                    val result = repository.getStats()
                    result.getOrNull()?.let { stats ->
                        _statsResponse.value = stats
                    }
                } catch (e: Exception) {
                    println("Error fetching stats: ${e.message}")
                }
                delay(Constants.STATS_UPDATE_INTERVAL_MS)
            }
        }
    }

    suspend fun pausePlayback() {
        val result = repository.controlPlayback("pause")
        result.getOrNull()?.let {
            _isPlaying.value = false
        }
    }

    suspend fun resumePlayback() {
        val result = repository.controlPlayback("resume")
        result.getOrNull()?.let {
            _isPlaying.value = true
        }
    }

    suspend fun restartPlayback() {
        val result = repository.controlPlayback("restart")
        result.getOrNull()?.let {
            _isPlaying.value = true
        }
    }

    fun cleanup() {
        statsUpdateJob?.cancel()
        scope.cancel()
    }
}