/**
 * Performance chart component
 * Author: Alims-Repo
 * Date: 2025-06-17
 */
package com.alimsrepo.vehicledetection.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alimsrepo.vehicledetection.data.model.DetectionStats
import com.alimsrepo.vehicledetection.ui.theme.*

@Composable
fun PerformanceChart(
    detectionStats: DetectionStats,
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
            // Chart header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Timeline,
                    contentDescription = "Performance Chart",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Performance Metrics",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            // Performance summary
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MetricCard(
                    label = "FPS",
                    value = String.format("%.1f", detectionStats.fps),
                    color = ChartPrimary
                )
                MetricCard(
                    label = "Avg Time",
                    value = String.format("%.3f ms", detectionStats.avg_time * 1000),
                    color = ChartSecondary
                )
                MetricCard(
                    label = "Samples",
                    value = "${detectionStats.samples}",
                    color = ChartTertiary
                )
                MetricCard(
                    label = "Avg Vehicles",
                    value = String.format("%.1f", detectionStats.avg_count),
                    color = VehicleCarColor
                )
            }

            // Performance range
            detectionStats.recent_times?.let { recentTimes ->
                if (recentTimes.isNotEmpty()) {
                    Column {
                        Text(
                            text = "Recent Detection Times",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Simple line chart
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(8.dp)
                        ) {
                            drawPerformanceChart(recentTimes)
                        }

                        // Chart legend
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Min: ${String.format("%.3f ms", detectionStats.min_time * 1000)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = SuccessGreen
                            )
                            Text(
                                text = "Max: ${String.format("%.3f ms", detectionStats.max_time * 1000)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = ErrorRed
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricCard(
    label: String,
    value: String,
    color: Color
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun DrawScope.drawPerformanceChart(data: List<Double>) {
    if (data.size < 2) return

    val maxValue = data.maxOrNull() ?: 1.0
    val minValue = data.minOrNull() ?: 0.0
    val range = maxValue - minValue

    val width = size.width
    val height = size.height
    val stepX = width / (data.size - 1)

    // Draw grid lines
    val gridColor = Color.Gray.copy(alpha = 0.3f)
    for (i in 0..4) {
        val y = height * i / 4
        drawLine(
            color = gridColor,
            start = Offset(0f, y),
            end = Offset(width, y),
            strokeWidth = 1.dp.toPx()
        )
    }

    // Draw chart line
    val path = Path()
    data.forEachIndexed { index, value ->
        val x = index * stepX
        val y = if (range > 0) {
            height - ((value - minValue) / range * height).toFloat()
        } else {
            height / 2
        }

        if (index == 0) {
            path.moveTo(x, y)
        } else {
            path.lineTo(x, y)
        }
    }

    drawPath(
        path = path,
        color = ChartPrimary,
        style = Stroke(width = 3.dp.toPx())
    )

    // Draw points
    data.forEachIndexed { index, value ->
        val x = index * stepX
        val y = if (range > 0) {
            height - ((value - minValue) / range * height).toFloat()
        } else {
            height / 2
        }

        drawCircle(
            color = ChartPrimary,
            radius = 4.dp.toPx(),
            center = Offset(x, y)
        )
    }
}