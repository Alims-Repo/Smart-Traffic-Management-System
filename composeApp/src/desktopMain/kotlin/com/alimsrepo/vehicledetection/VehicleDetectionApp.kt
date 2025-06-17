/**
 * Main application composable
 * Author: Alims-Repo
 * Date: 2025-06-17
 */
package com.alimsrepo.vehicledetection

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alimsrepo.vehicledetection.data.repository.VehicleDetectionRepository
import com.alimsrepo.vehicledetection.ui.components.ConnectionStatus
import com.alimsrepo.vehicledetection.ui.screens.ControlScreen
import com.alimsrepo.vehicledetection.ui.screens.DashboardScreen
import com.alimsrepo.vehicledetection.ui.screens.MainScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleDetectionApp(
    repository: VehicleDetectionRepository
) {
    var selectedTab by remember { mutableStateOf(0) }
    val connectionState by repository.connectionState.collectAsState()

    // Auto-connect on startup
    LaunchedEffect(Unit) {
        repository.connect()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top app bar
        TopAppBar(
            title = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("M1 Vehicle Detection Client")
                    Spacer(modifier = Modifier.weight(1f))
                    ConnectionStatus(connectionState = connectionState)
                }
            },
            actions = {
                IconButton(
                    onClick = {
                        if (connectionState == com.alimsrepo.vehicledetection.data.network.WebSocketManager.ConnectionState.CONNECTED) {
                            repository.disconnect()
                        } else {
                            repository.connect()
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (connectionState == com.alimsrepo.vehicledetection.data.network.WebSocketManager.ConnectionState.CONNECTED) {
                            Icons.Default.WifiOff
                        } else {
                            Icons.Default.Wifi
                        },
                        contentDescription = "Toggle Connection"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        )

        // Navigation tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Live Stream") },
                icon = { Icon(Icons.Default.VideoCall, contentDescription = null) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Dashboard") },
                icon = { Icon(Icons.Default.Dashboard, contentDescription = null) }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Controls") },
                icon = { Icon(Icons.Default.ControlCamera, contentDescription = null) }
            )
        }

        // Content based on selected tab
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when (selectedTab) {
                0 -> MainScreen(repository = repository)
                1 -> DashboardScreen(repository = repository)
                2 -> ControlScreen(repository = repository)
            }
        }
    }
}