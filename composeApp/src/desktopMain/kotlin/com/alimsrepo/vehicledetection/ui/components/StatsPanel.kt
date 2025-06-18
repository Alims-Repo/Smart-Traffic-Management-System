/**
 * Statistics panel component
 * Author: Alims-Repo
 * Date: 2025-06-17
 */
package com.alimsrepo.vehicledetection.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alimsrepo.vehicledetection.data.model.DetectionResponse
import com.alimsrepo.vehicledetection.data.model.StatsResponse
import com.alimsrepo.vehicledetection.ui.theme.*

@Composable
fun StatsPanel(
    detectionResponse: DetectionResponse?,
    statsResponse: StatsResponse?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Performance Statistics",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Real-time stats from detection response
                detectionResponse?.let { response ->
                    item {
                        StatCard(
                            title = "Live Detection",
                            icon = Icons.Default.Visibility,
                            iconColor = SuccessGreen,
                            stats = listOf(
                                StatItem("Vehicles Detected", "${response.vehicleCount}"),
                                StatItem("Frame Count", "${response.frameCount ?: 0}"),
                                StatItem("Device", response.device.uppercase()),
                                StatItem("Timestamp", java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Date(response.timestamp.toLong() * 1000)))
                            )
                        )
                    }
                    
                    response.performance?.let { performance ->
                        item {
                            StatCard(
                                title = "Performance Metrics",
                                icon = Icons.Default.Speed,
                                iconColor = InfoBlue,
                                stats = listOf(
                                    StatItem("Detection FPS", String.format("%.1f", performance.detectionFps)),
                                    StatItem("Broadcast FPS", String.format("%.1f", performance.broadcastFps)),
                                    StatItem("Avg Detection Time", String.format("%.3f ms", performance.avgDetectionTime * 1000))
                                )
                            )
                        }
                    }
                }
                
                // Server stats
                statsResponse?.let { stats ->
                    item {
                        StatCard(
                            title = "Video Information",
                            icon = Icons.Default.VideoFile,
                            iconColor = PrimaryBlue,
                            stats = listOf(
                                StatItem("Video Path", stats.video.path.substringAfterLast("/")),
                                StatItem("Total Frames", "${stats.video.frame_count}"),
                                StatItem("Current Vehicles", "${stats.video.current_vehicles}"),
                                StatItem("Status", if (stats.video.is_running) "Running" else "Stopped")
                            )
                        )
                    }
                    
                    item {
                        StatCard(
                            title = "Client Connections",
                            icon = Icons.Default.People,
                            iconColor = SecondaryGreen,
                            stats = listOf(
                                StatItem("Connected Clients", "${stats.clients.connected}"),
                                StatItem("Total Connected", "${stats.clients.total_connected}")
                            )
                        )
                    }
                    
                    item {
                        StatCard(
                            title = "Configuration",
                            icon = Icons.Default.Settings,
                            iconColor = WarningOrange,
                            stats = listOf(
                                StatItem("Target FPS", "${stats.configuration.target_fps}"),
                                StatItem("JPEG Quality", "${stats.configuration.jpeg_quality}%"),
                                StatItem("Input Size", "${stats.configuration.input_size}px"),
                                StatItem("Confidence Threshold", String.format("%.2f", stats.configuration.confidence_threshold))
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    icon: ImageVector,
    iconColor: Color,
    stats: List<StatItem>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                stats.forEach { stat ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stat.label,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                        Text(
                            text = stat.value,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

data class StatItem(
    val label: String,
    val value: String
)