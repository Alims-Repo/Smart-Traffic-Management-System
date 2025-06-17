/**
 * Control screen for managing server settings
 * Author: Alims-Repo
 * Date: 2025-06-17
 */
package com.alimsrepo.vehicledetection.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alimsrepo.vehicledetection.data.repository.VehicleDetectionRepository
import com.alimsrepo.vehicledetection.ui.components.ControlPanel
import com.alimsrepo.vehicledetection.ui.viewmodel.ControlViewModel
import kotlinx.coroutines.launch

@Composable
fun ControlScreen(
    repository: VehicleDetectionRepository
) {
    val viewModel = remember { ControlViewModel(repository) }
    val config by viewModel.currentConfig.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.loadCurrentConfig()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ControlPanel(
                currentConfig = config,
                onConfigUpdate = { newConfig ->
                    scope.launch {
                        viewModel.updateConfig(newConfig)
                    }
                },
                onPlaybackControl = { action ->
                    scope.launch {
                        viewModel.controlPlayback(action)
                    }
                },
                onDeviceSwitch = { device ->
                    scope.launch {
                        viewModel.switchDevice(device)
                    }
                },
                onBroadcastControl = { action ->
                    scope.launch {
                        viewModel.controlBroadcast(action)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}