package com.gub.features.analytics.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gub.core.domain.Response
import com.gub.features.analytics.domain.model.ChartDataPoint
import com.gub.features.analytics.viewModel.ViewModelAnalytics

@Composable
fun InteractiveTrafficVolumeChartCard(
    selectedTimeRange: String,
    modifier: Modifier = Modifier,
    viewModel: ViewModelAnalytics
) {
    val chartData by viewModel.chartData.collectAsState()
    val analyticsMetrics by viewModel.analyticsMetrics.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    Card(
        modifier = modifier.height(380.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.ShowChart,
                        contentDescription = null,
                        tint = Color(0xFF2196F3),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            "Traffic Volume",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            selectedTimeRange,
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                }

                AnimatedVisibility(
                    visible = isRefreshing,
                    enter = fadeIn(animationSpec = tween(300)),
                    exit = fadeOut(animationSpec = tween(300))
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = Color(0xFF2196F3)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Metrics Row
            when (val metrics = analyticsMetrics) {
                is Response.Success -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        MetricItem(
                            label = "Current Volume",
                            value = "${metrics.data.totalVehicles}",
                            trend = "+5.2%",
                            color = Color(0xFF4CAF50)
                        )
                        MetricItem(
                            label = "Avg Speed",
                            value = "${metrics.data.averageSpeed.toInt()} km/h",
                            trend = "-2.1%",
                            color = Color(0xFFFF9800)
                        )
                        MetricItem(
                            label = "Congestion",
                            value = "${(metrics.data.congestionLevel * 100).toInt()}%",
                            trend = "+8.7%",
                            color = Color(0xFFF44336)
                        )
                    }
                }
                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Chart
            when (val response = chartData) {
                is Response.Error -> {
                    ErrorChartState(
                        error = response.error,
                        onRetry = { viewModel.refreshData() }
                    )
                }
                Response.Loading -> {
                    LoadingChartState()
                }
                is Response.Success -> {
                    TrafficVolumeChart(
                        dataPoints = response.data,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricItem(
    label: String,
    value: String,
    trend: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            value,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            label,
            color = Color.Gray,
            fontSize = 10.sp
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.TrendingUp,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(10.dp)
            )
            Text(
                trend,
                color = color,
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun TrafficVolumeChart(
    dataPoints: List<ChartDataPoint>,
    modifier: Modifier = Modifier
) {
    if (dataPoints.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text("No data available", color = Color.Gray)
        }
        return
    }

    val textMeasurer = rememberTextMeasurer()

    Box(modifier = modifier) {
        val color = MaterialTheme.colorScheme.onSurface
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawTrafficChart(dataPoints, textMeasurer, color)
        }

        // Time labels overlay
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp, start = 40.dp, end = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            dataPoints.forEachIndexed { index, point ->
                if (index % 6 == 0) { // Show every 6th hour
                    Text(
                        text = point.label,
                        fontSize = 8.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawTrafficChart(
    dataPoints: List<ChartDataPoint>,
    textMeasurer: TextMeasurer,
    color: Color
) {
    val width = size.width
    val height = size.height
    val padding = 40f
    val bottomPadding = 60f // Extra space for labels

    // Find max value for scaling
    val maxValue = dataPoints.maxOfOrNull { it.value }?.toFloat() ?: 1f
    val minValue = dataPoints.minOfOrNull { it.value }?.toFloat() ?: 0f
    val valueRange = if (maxValue - minValue > 0) maxValue - minValue else 1f

    // Create path for the line chart
    val path = Path()
    val gradientPath = Path()

    dataPoints.forEachIndexed { index, point ->
        val x = padding + (index * (width - 2 * padding) / (dataPoints.size - 1))
        val y = bottomPadding + ((point.value - minValue) / valueRange * (height - bottomPadding - padding))
        val adjustedY = height - y

        if (index == 0) {
            path.moveTo(x, adjustedY)
            gradientPath.moveTo(x, height - bottomPadding)
            gradientPath.lineTo(x, adjustedY)
        } else {
            path.lineTo(x, adjustedY)
            gradientPath.lineTo(x, adjustedY)
        }

        // Draw data points
        val pointColor = if (point.prediction) Color(0xFFFF9800) else Color(0xFF2196F3)
        drawCircle(
            color = pointColor,
            radius = if (point.prediction) 6f else 4f,
            center = Offset(x, adjustedY)
        )

        // Draw value labels for peak points
        if (point.value > maxValue * 0.8f) {
            val textResult = textMeasurer.measure(
                text = "${point.value}",
                style = TextStyle(
                    fontSize = 8.sp,
                    color = color
                )
            )
            drawText(
                textLayoutResult = textResult,
                topLeft = Offset(
                    x - textResult.size.width / 2,
                    adjustedY - textResult.size.height - 8f
                )
            )
        }
    }

    // Close gradient path
    gradientPath.lineTo(width - padding, height - bottomPadding)
    gradientPath.close()

    // Draw gradient fill
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF2196F3).copy(alpha = 0.3f),
            Color(0xFF2196F3).copy(alpha = 0.1f),
            Color.Transparent
        ),
        startY = padding,
        endY = height - bottomPadding
    )

    drawPath(
        path = gradientPath,
        brush = gradient
    )

    // Draw the main line
    drawPath(
        path = path,
        color = Color(0xFF2196F3),
        style = androidx.compose.ui.graphics.drawscope.Stroke(
            width = 3f,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )

    // Draw grid lines
    for (i in 1..4) {
        val y = bottomPadding + (i * (height - bottomPadding - padding) / 5)
        val gridY = height - y

        drawLine(
            color = Color.Gray.copy(alpha = 0.2f),
            start = Offset(padding, gridY),
            end = Offset(width - padding, gridY),
            strokeWidth = 1f
        )

        // Draw value labels on Y-axis
        val value = (minValue + (i * valueRange / 5)).toInt()
        val textResult = textMeasurer.measure(
            text = "$value",
            style = TextStyle(
                fontSize = 8.sp,
                color = Color.Gray
            )
        )
        drawText(
            textLayoutResult = textResult,
            topLeft = Offset(
                8f,
                gridY - textResult.size.height / 2
            )
        )
    }

    // Draw prediction indicator
    val currentHour = 17 // Assuming current time is around 17:00
    if (currentHour < dataPoints.size) {
        val currentX = padding + (currentHour * (width - 2 * padding) / (dataPoints.size - 1))
        drawLine(
            color = Color(0xFFFF9800).copy(alpha = 0.6f),
            start = Offset(currentX, bottomPadding),
            end = Offset(currentX, height - padding),
            strokeWidth = 2f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 5f))
        )

        // Draw "Now" label
        val nowTextResult = textMeasurer.measure(
            text = "Now",
            style = TextStyle(
                fontSize = 8.sp,
                color = Color(0xFFFF9800),
                fontWeight = FontWeight.Bold
            )
        )
        drawText(
            textLayoutResult = nowTextResult,
            topLeft = Offset(
                currentX - nowTextResult.size.width / 2,
                bottomPadding - nowTextResult.size.height - 4f
            )
        )
    }
}

@Composable
private fun ErrorChartState(error: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Failed to load chart data",
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                error,
                color = Color.Gray,
                fontSize = 10.sp
            )
            TextButton(onClick = onRetry) {
                Text("Retry", fontSize = 10.sp)
            }
        }
    }
}

@Composable
private fun LoadingChartState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CircularProgressIndicator(
                color = Color(0xFF2196F3),
                strokeWidth = 2.dp
            )
            Text(
                "Loading chart...",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}