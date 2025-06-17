/**
 * Main screen with live video stream
 * Author: Alims-Repo
 * Date: 2025-06-17
 */
package com.alimsrepo.vehicledetection.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alimsrepo.vehicledetection.data.repository.VehicleDetectionRepository
import com.alimsrepo.vehicledetection.ui.components.VideoDisplay
import com.alimsrepo.vehicledetection.ui.components.StatsPanel
import com.alimsrepo.vehicledetection.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    repository: VehicleDetectionRepository
) {
    val viewModel = remember { MainViewModel(repository) }
    val detectionResponse by repository.detectionData.collectAsState()
    val statsResponse by viewModel.statsResponse.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val scope = rememberCoroutineScope()

    // Start periodic stats updates
    LaunchedEffect(Unit) {
        viewModel.startStatsUpdates()
    }

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Video display - takes 2/3 of the screen
        VideoDisplay(
            detectionResponse = detectionResponse,
            modifier = Modifier.weight(2f),
            onPlayPause = {
                scope.launch {
                    if (isPlaying) {
                        viewModel.pausePlayback()
                    } else {
                        viewModel.resumePlayback()
                    }
                }
            },
            onRestart = {
                scope.launch {
                    viewModel.restartPlayback()
                }
            },
            isPlaying = isPlaying
        )

        // Stats panel - takes 1/3 of the screen
        StatsPanel(
            detectionResponse = detectionResponse,
            statsResponse = statsResponse,
            modifier = Modifier.weight(1f)
        )
    }
}