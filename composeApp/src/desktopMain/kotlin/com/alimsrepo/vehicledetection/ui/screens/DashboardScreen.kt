/**
 * Dashboard screen with detailed analytics
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
import com.alimsrepo.vehicledetection.ui.components.PerformanceChart
import com.alimsrepo.vehicledetection.ui.viewmodel.DashboardViewModel
import kotlinx.coroutines.launch

@Composable
fun DashboardScreen(
    repository: VehicleDetectionRepository
) {
    val viewModel = remember { DashboardViewModel(repository) }
    val healthStatus by viewModel.healthStatus.collectAsState()
    val performanceStats by viewModel.performanceStats.collectAsState()
    val systemInfo by viewModel.systemInfo.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.startUpdates()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Health Status Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.HealthAndSafety,
                            contentDescription = "Health",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "System Health",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Button(
                            onClick = {
                                scope.launch {
                                    viewModel.refreshData()
                                }
                            }
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Refresh")
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    healthStatus?.let { health ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            HealthMetric(
                                label = "Status",
                                value = health.status.uppercase(),
                                isGood = health.status == "healthy"
                            )
                            HealthMetric(
                                label = "Device",
                                value = health.device.uppercase(),
                                isGood = true
                            )
                            HealthMetric(
                                label = "Clients",
                                value = "${health.clients}",
                                isGood = health.clients > 0
                            )
                            HealthMetric(
                                label = "MPS",
                                value = if (health.mpsAvailable) "Available" else "N/A",
                                isGood = health.mpsAvailable
                            )
                        }
                    }
                }
            }
        }

        // Performance Chart
        item {
            performanceStats?.let { stats ->
                PerformanceChart(
                    detectionStats = stats,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
            }
        }

        // System Information
        item {
            systemInfo?.let { info ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "System Info",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            Text(
                                text = "System Information",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        SystemInfoRow("Device", info.device.uppercase())
                        SystemInfoRow("Input Size", "${info.inputSize}px")
                        SystemInfoRow("Confidence", String.format("%.2f", info.confidence))
                        SystemInfoRow("Vehicle Classes", info.vehicleClasses.joinToString(", "))
                        info.version?.let { SystemInfoRow("Version", it) }
                        info.author?.let { SystemInfoRow("Author", it) }
                    }
                }
            }
        }

        // Vehicle Classes
        item {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = "Vehicle Classes",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                        Text(
                            text = "Detected Vehicle Classes",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    systemInfo?.vehicleClasses?.let { classes ->
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            classes.forEach { vehicleClass ->
                                AssistChip(
                                    onClick = { },
                                    label = { Text(vehicleClass.uppercase()) },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = getVehicleIcon(vehicleClass),
                                            contentDescription = vehicleClass,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HealthMetric(
    label: String,
    value: String,
    isGood: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = if (isGood) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SystemInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun getVehicleIcon(vehicleClass: String) = when (vehicleClass.lowercase()) {
    "car" -> Icons.Default.DirectionsCar
    "truck" -> Icons.Default.LocalShipping
    "bus" -> Icons.Default.DirectionsBus
    "motorcycle" -> Icons.Default.TwoWheeler
    "bicycle" -> Icons.Default.DirectionsBike
    else -> Icons.Default.DirectionsCar
}