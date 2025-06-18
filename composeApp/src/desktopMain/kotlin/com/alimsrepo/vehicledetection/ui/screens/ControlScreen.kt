/**
 * Fixed Control screen with message display
 * Author: Alims-Repo
 * Date: 2025-06-17
 */
package com.alimsrepo.vehicledetection.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    val isLoading by viewModel.isLoading.collectAsState()
    val message by viewModel.message.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.loadCurrentConfig()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Message display
        message?.let { msg ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (msg.contains("success", ignoreCase = true)) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.errorContainer
                    }
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = msg,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { viewModel.clearMessage() }) {
                        Icon(Icons.Default.Close, contentDescription = "Dismiss")
                    }
                }
            }
        }

        // Loading indicator
        if (isLoading) {
            Card {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                    Text("Processing...")
                }
            }
        }

        // Control panel
        LazyColumn(
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
}