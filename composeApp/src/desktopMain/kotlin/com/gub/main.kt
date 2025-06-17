/**
 * Main entry point for Vehicle Detection Desktop Client
 * Author: Alims-Repo
 * Date: 2025-06-17
 */
package com.gub

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.alimsrepo.vehicledetection.VehicleDetectionApp
import com.alimsrepo.vehicledetection.ui.theme.VehicleDetectionTheme
import com.alimsrepo.vehicledetection.data.network.ApiService
import com.alimsrepo.vehicledetection.data.network.WebSocketManager
import com.alimsrepo.vehicledetection.data.repository.VehicleDetectionRepository
import com.alimsrepo.vehicledetection.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
fun main() = application {
    // Remember repository instance
    val repository = remember {
        VehicleDetectionRepository(
            apiService = ApiService(),
            webSocketManager = WebSocketManager()
        )
    }

    // Cleanup on exit
    DisposableEffect(Unit) {
        onDispose {
            repository.cleanup()
        }
    }

    Window(
        onCloseRequest = {
            repository.cleanup()
            exitApplication()
        },
        title = "M1 Vehicle Detection Desktop Client - Alims-Repo - 2025-06-17 08:27:32",
        state = rememberWindowState(
            width = Constants.DEFAULT_WINDOW_WIDTH.dp,
            height = Constants.DEFAULT_WINDOW_HEIGHT.dp,
            position = WindowPosition(Alignment.Center)
        ),
        resizable = true
    ) {
        VehicleDetectionTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                VehicleDetectionApp(repository = repository)
            }
        }
    }
}