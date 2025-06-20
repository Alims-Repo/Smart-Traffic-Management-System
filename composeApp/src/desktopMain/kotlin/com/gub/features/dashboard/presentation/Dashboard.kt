package com.gub.features.dashboard.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.gub.features.dashboard.presentation.components.TopBar
import com.gub.features.dashboard.presentation.insights.UserInsightsDashboardCard
import com.gub.features.dashboard.presentation.liveStatus.LiveStatus
import com.gub.features.dashboard.presentation.overview.Overview
import com.gub.features.dashboard.presentation.quickAction.QuickActionsGrid
import com.gub.features.dashboard.viewmodel.ViewModelDashboard
import com.gub.utils.UiCalculations.toDp
import dev.chrisbanes.haze.rememberHazeState

@Composable
fun Dashboard() {

    val hazeState = rememberHazeState()
    var height by remember { mutableStateOf(0) }
    var selectedTab by remember { mutableStateOf(0) }

//    val viewModelDashboard by remember { mutableStateOf(ViewModelDashboard()) }

    AnimatedContent(
        targetState = selectedTab
    ) {
        when(it) {
            0 -> Overview(hazeState, height.toDp())
            1 -> LiveStatus(hazeState, height.toDp())
            2 -> QuickActionsGrid(hazeState, height.toDp())
            3 -> UserInsightsDashboardCard(hazeState, height.toDp())
        }
    }

    TopBar(
        hazeState,
        selectedTab,
        onSelect = { selectedTab = it },
        topBarHeight = { height = it }
    )
}


@Composable
fun SystemOverviewMetric(
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    icon: ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            title,
            color = Color.Gray,
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
        Text(
            value,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            subtitle,
            color = color,
            fontSize = 9.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LiveTrafficMetricsCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp)
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
                    "Live Traffic Metrics",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        "REAL-TIME",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LiveMetricItem("Total Vehicle Count", "128,569", "+892 from last hour", true)
            LiveMetricItem("Average Speed", "29.4 mph", "+1.2 mph improvement", true)
            LiveMetricItem("Congestion Index", "23.7%", "-4.3% reduction", true)
            LiveMetricItem("Signal Efficiency", "95.3%", "+2.1% optimization", true)

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(0.1F))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Next peak prediction:",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                    Text(
                        "18:15 (High volume expected)",
                        color = Color(0xFF2196F3),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun LiveMetricItem(label: String, value: String, change: String, positive: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                label,
                color = Color.Gray,
                fontSize = 11.sp
            )
            Text(
                value,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    if (positive) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                    contentDescription = null,
                    tint = if (positive) Color(0xFF4CAF50) else Color(0xFFF44336),
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                change,
                color = if (positive) Color(0xFF4CAF50) else Color(0xFFF44336),
                fontSize = 9.sp,
                textAlign = TextAlign.End
            )
        }
    }
}



@Composable
fun TodaysActivitySummaryCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(12.dp)
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
                    "Today's Activity Summary",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "June 17, 2025",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TodayActivityMetric(
                    title = "AI Optimizations",
                    value = "47",
                    subtitle = "Completed",
                    color = Color(0xFF4CAF50)
                )

                TodayActivityMetric(
                    title = "Alerts Resolved",
                    value = "23",
                    subtitle = "Auto + Manual",
                    color = Color(0xFF2196F3)
                )

                TodayActivityMetric(
                    title = "Traffic Improved",
                    value = "18.3%",
                    subtitle = "vs Yesterday",
                    color = Color(0xFF4CAF50)
                )

                TodayActivityMetric(
                    title = "User Sessions",
                    value = "14",
                    subtitle = "Alims-Repo",
                    color = Color(0xFFFF9800)
                )

                TodayActivityMetric(
                    title = "System Uptime",
                    value = "100%",
                    subtitle = "No Downtime",
                    color = Color(0xFF4CAF50)
                )
            }
        }
    }
}

@Composable
fun TodayActivityMetric(title: String, value: String, subtitle: String, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Text(
            title,
            color = Color.Gray,
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            value,
            color = color,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            subtitle,
            color = color,
            fontSize = 9.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LiveIntersectionMapCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Live Intersection Map",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Simulated map view
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(0xFF0D1117), RoundedCornerShape(8.dp))
            ) {
                // Simulated intersection points
                repeat(8) { index ->
                    val x = (0.1f + (index % 3) * 0.4f)
                    val y = (0.2f + (index / 3) * 0.3f)

                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .offset(
                                x = (200 * x).dp,
                                y = (160 * y).dp
                            )
                            .background(
                                when (index) {
                                    0, 1, 4 -> Color(0xFF4CAF50) // Good performance
                                    2, 6 -> Color(0xFFFF9800) // Medium performance
                                    else -> Color(0xFF2196F3) // Normal
                                },
                                CircleShape
                            )
                    )
                }

                // Map legend
                Column(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                ) {
                    MapLegendItem("Optimal", Color(0xFF4CAF50))
                    MapLegendItem("Moderate", Color(0xFFFF9800))
                    MapLegendItem("Normal", Color(0xFF2196F3))
                }

                Text(
                    "Downtown District",
                    color = Color.Gray,
                    fontSize = 10.sp,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "247 intersections monitored • Updated 5 seconds ago",
                color = Color.Gray,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun MapLegendItem(label: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 1.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            label,
            color = Color.Gray,
            fontSize = 8.sp
        )
    }
}

@Composable
fun CurrentOptimizationsCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(12.dp)
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
                    "Current Optimizations",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B5E20))
                ) {
                    Text(
                        "RUNNING",
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OptimizationItem(
                title = "Signal Timing Adjustment",
                location = "Main St & 5th Ave",
                progress = 85,
                eta = "2 min remaining"
            )

            OptimizationItem(
                title = "Route Optimization",
                location = "Downtown Corridor",
                progress = 45,
                eta = "8 min remaining"
            )

            OptimizationItem(
                title = "Congestion Mitigation",
                location = "Highway 101",
                progress = 92,
                eta = "1 min remaining"
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1117))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Next optimization cycle:",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                    Text(
                        "4m 23s",
                        color = Color(0xFF4CAF50),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun OptimizationItem(title: String, location: String, progress: Int, eta: String) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    title,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    location,
                    color = Color.Gray,
                    fontSize = 9.sp
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${progress}%",
                    color = Color(0xFF4CAF50),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    eta,
                    color = Color.Gray,
                    fontSize = 9.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(Color(0xFF30363D), RoundedCornerShape(2.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress / 100f)
                    .background(Color(0xFF4CAF50), RoundedCornerShape(2.dp))
            )
        }
    }
}

@Composable
fun PredictiveInsightsCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(12.dp)
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
                    "Predictive Insights",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    Icons.Default.TrendingUp,
                    contentDescription = null,
                    tint = Color(0xFF2196F3)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            PredictiveInsightItem(
                title = "Peak Traffic Prediction",
                prediction = "18:15 - High volume expected",
                confidence = 94,
                timeframe = "47 minutes"
            )

            PredictiveInsightItem(
                title = "Optimal Route Suggestion",
                prediction = "Redirect to Main St alternate",
                confidence = 87,
                timeframe = "Next 2 hours"
            )

            PredictiveInsightItem(
                title = "Maintenance Window",
                prediction = "Low traffic: 2:00-4:00 AM",
                confidence = 91,
                timeframe = "Tomorrow"
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1117))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "AI Prediction Accuracy:",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                    Text(
                        "95.7% (Last 30 days)",
                        color = Color(0xFF4CAF50),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun PredictiveInsightItem(title: String, prediction: String, confidence: Int, timeframe: String) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                title,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                "${confidence}%",
                color = Color(0xFF2196F3),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            prediction,
            color = Color.Gray,
            fontSize = 11.sp
        )

        Text(
            timeframe,
            color = Color(0xFF4CAF50),
            fontSize = 10.sp
        )
    }
}

@Composable
fun PerformanceTrendsCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Performance Trends",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))
                        // Performance trend chart simulation
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color(0xFF0D1117), RoundedCornerShape(8.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    val trendData = listOf(78, 82, 85, 88, 91, 94, 97, 95, 93, 96, 98, 97)

                    trendData.forEachIndexed { index, value ->
                        val height = (value / 100f * 90).dp

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(6.dp)
                                    .height(height)
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color(0xFF4CAF50),
                                                Color(0xFF2E7D32)
                                            )
                                        ),
                                        shape = RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp)
                                    )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            if (index % 3 == 0) {
                                Text(
                                    "W${index/3 + 1}",
                                    color = Color.Gray,
                                    fontSize = 7.sp
                                )
                            }
                        }
                    }
                }

                Text(
                    "System Efficiency %",
                    color = Color.Gray,
                    fontSize = 9.sp,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            PerformanceTrendItem("Weekly Average", "95.8%", "+2.3%", true)
            PerformanceTrendItem("Best Day", "98.7%", "June 15", false)
            PerformanceTrendItem("Improvement Rate", "+0.4%", "Per week", true)

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2196F3)),
                border = BorderStroke(1.dp, Color(0xFF2196F3).copy(alpha = 0.5f))
            ) {
                Text("View Detailed Analytics", fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun PerformanceTrendItem(label: String, value: String, detail: String, showTrend: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color.Gray, fontSize = 11.sp)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                value,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                detail,
                color = if (showTrend && detail.startsWith("+")) Color(0xFF4CAF50) else Color.Gray,
                fontSize = 9.sp
            )
        }
    }
}

