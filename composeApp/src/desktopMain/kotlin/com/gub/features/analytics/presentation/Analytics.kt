package com.gub.features.analytics.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gub.core.domain.Response
import com.gub.core.ui.components.PulsingDot
import com.gub.features.analytics.domain.model.PeakHourData
import com.gub.features.analytics.presentation.components.InteractiveTrafficVolumeChartCard
import com.gub.features.analytics.presentation.components.PredictiveAnalyticsCard
import com.gub.features.analytics.presentation.components.TopBarAnalytics
import com.gub.features.analytics.viewModel.ViewModelAnalytics
import com.gub.models.analytics.*
import com.gub.utils.UiCalculations.toDp
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.delay

@Composable
fun Analytics() {
    val hazeState = rememberHazeState()
    var height by remember { mutableStateOf(0) }

    val viewModelAnalytics: ViewModelAnalytics = remember { ViewModelAnalytics() }
    val filterOptions by viewModelAnalytics.filterOptions.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .hazeSource(hazeState),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(
            top = height.toDp() + 24.dp,
            start = 24.dp,
            end = 24.dp,
            bottom = 24.dp
        )
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InteractiveTrafficVolumeChartCard(
                    selectedTimeRange = filterOptions.timeRange.displayName,
                    modifier = Modifier.weight(1.4f),
                    viewModel = viewModelAnalytics
                )
                PredictiveAnalyticsCard(
                    modifier = Modifier.weight(1f),
                    viewModel = viewModelAnalytics
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AdaptivePeakHoursCard(
                    modifier = Modifier.weight(1f),
                    viewModel = viewModelAnalytics
                )
            }
        }

        item {
            EnhancedExportDataCard(
                modifier = Modifier.fillMaxWidth(),
                viewModel = viewModelAnalytics
            )
        }
    }

    TopBarAnalytics(
        hazeState = hazeState,
        viewModel = viewModelAnalytics,
        topBarHeight = { height = it }
    )
}

@Composable
fun AutoRefreshIndicator(viewModel: ViewModelAnalytics) {
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    Row(verticalAlignment = Alignment.CenterVertically) {
        if (isRefreshing) {
            CircularProgressIndicator(
                modifier = Modifier.size(8.dp),
                strokeWidth = 1.dp,
                color = Color(0xFF4CAF50)
            )
        } else {
            PulsingDot()
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            if (isRefreshing) "Refreshing..." else "Live",
            color = Color(0xFF4CAF50),
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun AdaptivePeakHoursCard(modifier: Modifier = Modifier, viewModel: ViewModelAnalytics) {
    val peakHourData by viewModel.peakHourData.collectAsState()
    var selectedDay by remember { mutableStateOf(0) }
    val days = listOf("Today", "Tomorrow", "This Week")

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Adaptive Peak Analysis",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    days.forEachIndexed { index, day ->
                        FilterChip(
                            onClick = { selectedDay = index },
                            label = {
                                Text(
                                    day,
                                    fontSize = 9.sp,
                                    color = if (selectedDay == index) Color.White else Color.Gray
                                )
                            },
                            selected = selectedDay == index,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF2E7D32),
                                containerColor = Color(0xFF0D1117)
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (val response = peakHourData) {
                is Response.Error -> {
                    ErrorCard(error = response.error) {
                        viewModel.refreshData()
                    }
                }
                Response.Loading -> {
                    LoadingCard()
                }
                is Response.Success -> {
                    response.data.forEach { peakData ->
                        AdaptivePeakHourItem(peakData = peakData)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "AI Adaptation Status",
                            color = Color.Gray,
                            fontSize = 10.sp
                        )
                        Text(
                            "Signals auto-adjusting",
                            color = Color(0xFF4CAF50),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50)
                    )
                }
            }
        }
    }
}

@Composable
fun AdaptivePeakHourItem(peakData: PeakHourData) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (peakData.isActive)
                Color(0xFF1B5E20).copy(alpha = 0.3f)
            else MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (peakData.isActive) {
                            PulsingDot()
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                        Text(
                            peakData.period,
                            color = if (peakData.isActive) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                            fontWeight = if (peakData.isActive) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                    Text(
                        peakData.timeRange,
                        color = Color.Gray,
                        fontSize = 10.sp
                    )
                }
                Text(
                    "${peakData.intensity}%",
                    color = when {
                        peakData.intensity > 80 -> Color(0xFFF44336)
                        peakData.intensity > 60 -> Color(0xFFFF9800)
                        else -> Color(0xFF4CAF50)
                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(Color(0xFF30363D), RoundedCornerShape(2.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(peakData.intensity / 100f)
                        .background(
                            when {
                                peakData.intensity > 80 -> Color(0xFFF44336)
                                peakData.intensity > 60 -> Color(0xFFFF9800)
                                else -> Color(0xFF4CAF50)
                            },
                            RoundedCornerShape(2.dp)
                        )
                )
            }
        }
    }
}

@Composable
fun EnhancedExportDataCard(modifier: Modifier = Modifier, viewModel: ViewModelAnalytics) {
    val exportInfo by viewModel.exportInfo.collectAsState()

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Data Export & Reports",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (val response = exportInfo) {
                is Response.Error -> {
                    ErrorCard(error = response.error) {
                        viewModel.refreshData()
                    }
                }
                Response.Loading -> {
                    LoadingCard()
                }
                is Response.Success -> {
                    val info = response.data

                    EnhancedExportButton(
                        text = "Export Real-time CSV",
                        icon = Icons.Default.FileDownload,
                        description = "Live traffic data",
                        isExporting = info.isExporting,
                        onClick = { viewModel.exportData("csv") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    EnhancedExportButton(
                        text = "Generate Analytics PDF",
                        icon = Icons.Default.PictureAsPdf,
                        description = "Comprehensive analysis report",
                        isExporting = false,
                        onClick = { viewModel.exportData("pdf") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    EnhancedExportButton(
                        text = "Share Live Dashboard",
                        icon = Icons.Default.Share,
                        description = "Real-time view link",
                        isExporting = false,
                        onClick = { viewModel.exportData("share") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    EnhancedExportButton(
                        text = "API Data Access",
                        icon = Icons.Default.Api,
                        description = "RESTful endpoints",
                        isExporting = false,
                        onClick = { viewModel.exportData("api") }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Last export:",
                                    color = MaterialTheme.colorScheme.onSurface.copy(0.8f),
                                    fontSize = 10.sp
                                )
                                Text(
                                    info.lastExportTime,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Auto-export:",
                                    color = MaterialTheme.colorScheme.onSurface.copy(0.8f),
                                    fontSize = 10.sp
                                )
                                Text(
                                    if (info.autoExportEnabled) "Enabled (Daily at 00:00 UTC)" else "Disabled",
                                    color = if (info.autoExportEnabled) Color(0xFF4CAF50) else Color.Gray,
                                    fontSize = 10.sp
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
fun EnhancedExportButton(
    text: String,
    icon: ImageVector,
    description: String,
    isExporting: Boolean,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        enabled = !isExporting,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = if (isExporting) Color.Gray else Color(0xFF2196F3)
        ),
        border = BorderStroke(
            1.dp,
            if (isExporting) Color.Gray.copy(alpha = 0.3f) else Color(0xFF2196F3).copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isExporting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = Color.Gray
                )
            } else {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    if (isExporting) "Exporting..." else text,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    description,
                    fontSize = 9.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
private fun ErrorCard(error: String, onRetry: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Error loading data",
                color = MaterialTheme.colorScheme.onErrorContainer,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                error,
                color = MaterialTheme.colorScheme.onErrorContainer.copy(0.8f),
                fontSize = 10.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = onRetry) {
                Text("Retry", fontSize = 10.sp)
            }
        }
    }
}

@Composable
private fun LoadingCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color(0xFF2196F3),
                strokeWidth = 2.dp
            )
        }
    }
}