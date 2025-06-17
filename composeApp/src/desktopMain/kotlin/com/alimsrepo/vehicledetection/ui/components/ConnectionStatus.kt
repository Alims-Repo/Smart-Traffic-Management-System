/**
 * Connection status indicator component
 * Author: Alims-Repo
 * Date: 2025-06-17
 */
package com.alimsrepo.vehicledetection.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.alimsrepo.vehicledetection.data.network.WebSocketManager
import com.alimsrepo.vehicledetection.ui.theme.*

@Composable
fun ConnectionStatus(
    connectionState: WebSocketManager.ConnectionState,
    modifier: Modifier = Modifier
) {
    val (color, text, icon) = when (connectionState) {
        WebSocketManager.ConnectionState.CONNECTED -> Triple(
            SuccessGreen, "Connected", Icons.Default.CheckCircle
        )
        WebSocketManager.ConnectionState.CONNECTING -> Triple(
            WarningOrange, "Connecting", Icons.Default.HourglassEmpty
        )
        WebSocketManager.ConnectionState.DISCONNECTED -> Triple(
            Color.Gray, "Disconnected", Icons.Default.Cancel
        )
        WebSocketManager.ConnectionState.ERROR -> Triple(
            ErrorRed, "Error", Icons.Default.Error
        )
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        shape = CircleShape
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )

            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = color,
                modifier = Modifier.size(16.dp)
            )

            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = color
            )
        }
    }
}