package com.gub.features.analytics.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gub.core.ui.components.PulsingDot
import com.gub.core.ui.components.UserProfileChip
import kotlinx.coroutines.delay

@Composable
fun Analytics() {
    var selectedTimeRange by remember { mutableStateOf(0) }
    var selectedMetric by remember { mutableStateOf("traffic_flow") }
    var showAdvancedFilters by remember { mutableStateOf(false) }
    var selectedIntersectionFilter by remember { mutableStateOf("All") }

    val timeRanges = listOf("Last Hour", "Last 6H", "Last 24H", "Last Week", "Last Month")
    val currentTime = "2025-06-17 16:05:53 UTC"

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Traffic Analytics Dashboard",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Text(
                                "Last updated: $currentTime",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            AutoRefreshIndicator()
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = { showAdvancedFilters = !showAdvancedFilters }
                        ) {
                            Icon(
                                Icons.Default.FilterList,
                                contentDescription = "Advanced Filters",
                                tint = if (showAdvancedFilters) Color(0xFF4CAF50) else Color.Gray
                            )
                        }

                        UserProfileChip()
                    }
                }

                // Time Range and Filters
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        timeRanges.forEachIndexed { index, range ->
                            FilterChip(
                                onClick = { selectedTimeRange = index },
                                label = {
                                    Text(
                                        range,
                                        fontSize = 12.sp,
                                        color = if (selectedTimeRange == index) Color.White else Color.Gray
                                    )
                                },
                                selected = selectedTimeRange == index,
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF2E7D32),
                                    containerColor = Color(0xFF161B22)
                                )
                            )
                        }
                    }

                    AnimatedVisibility(visible = showAdvancedFilters) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AdvancedFilterChip("All Intersections", selectedIntersectionFilter == "All") {
                                selectedIntersectionFilter = "All"
                            }
                            AdvancedFilterChip("Critical Only", selectedIntersectionFilter == "Critical") {
                                selectedIntersectionFilter = "Critical"
                            }
                            AdvancedFilterChip("AI Optimized", selectedIntersectionFilter == "AI") {
                                selectedIntersectionFilter = "AI"
                            }
                        }
                    }
                }
            }
        }

        // Real-time Status Overview - Full Width
        item {
            RealTimeStatusOverview()
        }

        // Row 1: Two equal cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                EnhancedTrafficFlowMetricsCard(modifier = Modifier.weight(1f))
                AIPerformanceCard(modifier = Modifier.weight(1f))
            }
        }

        // Row 2: Chart takes 60%, Ranking takes 40%
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InteractiveTrafficVolumeChartCard(
                    selectedTimeRange = timeRanges[selectedTimeRange],
                    modifier = Modifier.weight(0.6f)
                )
                LiveIntersectionRankingCard(modifier = Modifier.weight(0.4f))
            }
        }

        // Row 3: Two equal cards (Removed Emission card)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PredictiveAnalyticsCard(modifier = Modifier.weight(1f))
                AdaptivePeakHoursCard(modifier = Modifier.weight(1f))
            }
        }

        // Row 4: AI Optimization takes 65%, Export takes 35%
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ComprehensiveAIOptimizationCard(modifier = Modifier.weight(0.65f))
                EnhancedExportDataCard(modifier = Modifier.weight(0.35f))
            }
        }

        // User-specific insights - Full Width
        item {
            UserInsightsCard()
        }
    }
}

@Composable
fun AutoRefreshIndicator() {
    var isRefreshing by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(30000) // Refresh every 30 seconds
            isRefreshing = true
            delay(1000)
            isRefreshing = false
        }
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        if (isRefreshing) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(Color(0xFF4CAF50), CircleShape)
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
fun AdvancedFilterChip(text: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        onClick = onClick,
        label = {
            Text(
                text,
                fontSize = 10.sp,
                color = if (selected) Color.White else Color.Gray
            )
        },
        selected = selected,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color(0xFF1976D2),
            containerColor = Color(0xFF161B22)
        )
    )
}

@Composable
fun RealTimeStatusOverview() {
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
                    "System Status Overview",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "16:05 UTC",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                RealTimeStatusItem("Active Intersections", "247/250", Color(0xFF4CAF50), "98.8%")
                RealTimeStatusItem("AI Systems", "Online", Color(0xFF4CAF50), "100%")
                RealTimeStatusItem("Data Streams", "Normal", Color(0xFF4CAF50), "99.2%")
                RealTimeStatusItem("Alerts", "3 Active", Color(0xFFFF9800), "Low")
                RealTimeStatusItem("Performance", "Optimal", Color(0xFF4CAF50), "94.2%")
            }
        }
    }
}

@Composable
fun RealTimeStatusItem(label: String, value: String, color: Color, subValue: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            label,
            color = Color.Gray,
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
        Text(
            value,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            subValue,
            color = color,
            fontSize = 9.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun EnhancedTrafficFlowMetricsCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(280.dp), // Fixed height for consistency
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
                    "Traffic Flow Metrics",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B5E20))
                ) {
                    Text(
                        "LIVE",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            EnhancedFlowMetricItem("Total Vehicles", "127,584", "+8.2%", true, "vs last hour")
            EnhancedFlowMetricItem("Average Speed", "28.5 mph", "+5.1%", true, "city-wide")
            EnhancedFlowMetricItem("Congestion Level", "23%", "-15.3%", true, "below threshold")
            EnhancedFlowMetricItem("Signal Efficiency", "94.2%", "+3.7%", true, "AI optimized")
            EnhancedFlowMetricItem("Incident Rate", "0.03/km", "-45.2%", true, "significant drop")
        }
    }
}

@Composable
fun EnhancedFlowMetricItem(
    label: String,
    value: String,
    change: String,
    positive: Boolean,
    context: String
) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    label,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                Text(
                    value,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        if (positive) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                        contentDescription = null,
                        tint = if (positive) Color(0xFF4CAF50) else Color(0xFFF44336),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        change,
                        color = if (positive) Color(0xFF4CAF50) else Color(0xFFF44336),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(
                    context,
                    color = Color.Gray,
                    fontSize = 9.sp
                )
            }
        }
    }
}

@Composable
fun AIPerformanceCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(280.dp), // Fixed height for consistency
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Psychology,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "AI Performance",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // AI Performance Gauge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .background(
                            brush = Brush.sweepGradient(
                                colors = listOf(
                                    Color(0xFF4CAF50),
                                    Color(0xFF8BC34A),
                                    Color(0xFF4CAF50)
                                )
                            ),
                            shape = CircleShape
                        )
                        .padding(3.dp)
                        .background(Color(0xFF161B22), CircleShape)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "96.8%",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Efficiency",
                            color = Color.Gray,
                            fontSize = 9.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            AIMetricItem("Decision Speed", "0.3ms", Color(0xFF4CAF50))
            AIMetricItem("Prediction Accuracy", "97.2%", Color(0xFF4CAF50))
            AIMetricItem("Models Active", "12/12", Color(0xFF4CAF50))

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
                        "Next optimization in:",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                    Text(
                        "4m 32s",
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
fun AIMetricItem(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color.Gray, fontSize = 11.sp)
        Text(
            value,
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun InteractiveTrafficVolumeChartCard(selectedTimeRange: String, modifier: Modifier = Modifier) {
    var selectedDataPoint by remember { mutableStateOf(-1) }

    Card(
        modifier = modifier.height(320.dp), // Fixed height
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
                Column {
                    Text(
                        "Traffic Volume Analysis",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        selectedTimeRange,
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.ShowChart,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Real-time",
                        color = Color(0xFF4CAF50),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Enhanced Chart with interaction
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(0xFF0D1117), RoundedCornerShape(8.dp))
            ) {
                val dataPoints = remember {
                    (0..23).map { hour ->
                        ChartDataPoint(
                            hour = hour,
                            value = when (hour) {
                                7, 8 -> (800..950).random() // Morning rush
                                12, 13 -> (600..750).random() // Lunch
                                17, 18, 19 -> (850..1000).random() // Evening rush
                                else -> (200..500).random() // Regular hours
                            },
                            prediction = (hour >= 16) // Show predictions for future hours
                        )
                    }
                }

                // Chart visualization
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    dataPoints.take(12).forEachIndexed { index, dataPoint ->
                        val heightRatio = dataPoint.value / 1000f
                        val height = (heightRatio * 160).dp

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { selectedDataPoint = index }
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(12.dp)
                                    .height(height)
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = if (dataPoint.prediction) {
                                                listOf(
                                                    Color(0xFF2196F3).copy(alpha = 0.7f),
                                                    Color(0xFF1976D2).copy(alpha = 0.7f)
                                                )
                                            } else {
                                                listOf(
                                                    Color(0xFF4CAF50),
                                                    Color(0xFF2E7D32)
                                                )
                                            }
                                        ),
                                        shape = RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp)
                                    )
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                "${dataPoint.hour}:00",
                                color = if (selectedDataPoint == index) Color.White else Color.Gray,
                                fontSize = 8.sp
                            )
                        }
                    }
                }

                // Selected data point info
                if (selectedDataPoint >= 0) {
                    val selectedPoint = dataPoints[selectedDataPoint]
                    Card(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF21262D))
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                "${selectedPoint.hour}:00",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "${selectedPoint.value} vehicles",
                                color = Color(0xFF4CAF50),
                                fontSize = 10.sp
                            )
                            if (selectedPoint.prediction) {
                                Text(
                                    "Predicted",
                                    color = Color(0xFF2196F3),
                                    fontSize = 9.sp
                                )
                            }
                        }
                    }
                }

                // Chart legend
                Column(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                ) {
                    ChartLegendItem("Actual", Color(0xFF4CAF50))
                    ChartLegendItem("Predicted", Color(0xFF2196F3))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Current: 847 vehicles/hr", color = Color.Gray, fontSize = 11.sp)
                Text("Predicted Peak: 18:30 (982 vehicles/hr)", color = Color.Gray, fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun ChartLegendItem(label: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            label,
            color = Color.Gray,
            fontSize = 10.sp
        )
    }
}

@Composable
fun LiveIntersectionRankingCard(modifier: Modifier = Modifier) {
    var autoRefresh by remember { mutableStateOf(true) }
    var lastUpdate by remember { mutableStateOf("16:05:53") }

    val intersections = remember {
        mutableStateListOf(
            IntersectionRanking("Main St & 5th Ave", 98.5f, 1),
            IntersectionRanking("Park Rd & Oak St", 94.2f, 2),
            IntersectionRanking("1st Ave & Broadway", 91.8f, 3),
            IntersectionRanking("Highway 101 & Center", 89.3f, 4),
            IntersectionRanking("Market St & Pine", 87.1f, 5),
            IntersectionRanking("Elm St & 3rd Ave", 85.7f, 6)
        )
    }

    LaunchedEffect(autoRefresh) {
        if (autoRefresh) {
            while (true) {
                delay(15000) // Update every 15 seconds
                // Simulate ranking changes
                intersections.forEach { intersection ->
                    intersection.efficiency = (intersection.efficiency + 1f).coerceIn(70f, 100f)
                }
                intersections.sortByDescending { it.efficiency }
                intersections.forEachIndexed { index, intersection ->
                    intersection.rank = index + 1
                }
                lastUpdate = "16:05:${(30..59).random()}"
            }
        }
    }

    Card(
        modifier = modifier.height(320.dp), // Fixed height to match chart
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
                    "Live Intersection Ranking",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    PulsingDot()
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        lastUpdate,
                        color = Color.Gray,
                        fontSize = 9.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(220.dp) // Adjusted for content
            ) {
                items(intersections.size) { index ->
                    EnhancedIntersectionRankingItem(intersections[index])
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Auto-refresh: ${if (autoRefresh) "ON" else "OFF"}",
                    color = Color.Gray,
                    fontSize = 10.sp
                )

                Switch(
                    checked = autoRefresh,
                    onCheckedChange = { autoRefresh = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF4CAF50),
                        checkedTrackColor = Color(0xFF1B5E20)
                    ),
                    modifier = Modifier.scale(0.7f)
                )
            }
        }
    }
}

@Composable
fun EnhancedIntersectionRankingItem(ranking: IntersectionRanking) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = when (ranking.rank) {
                1 -> Color(0xFF1B5E20).copy(alpha = 0.3f)
                2, 3 -> Color(0xFF0D1117)
                else -> Color(0xFF0D1117)
            }
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Enhanced ranking badge
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        when (ranking.rank) {
                            1 -> Brush.radialGradient(listOf(Color(0xFFFFD700), Color(0xFFB8860B)))
                            2 -> Brush.radialGradient(listOf(Color(0xFFC0C0C0), Color(0xFF708090)))
                            3 -> Brush.radialGradient(listOf(Color(0xFFCD7F32), Color(0xFF8B4513)))
                            else -> Brush.radialGradient(listOf(Color(0xFF424242), Color(0xFF212121)))
                        },
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    ranking.rank.toString(),
                    color = if (ranking.rank <= 3) Color.Black else Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    ranking.name,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Efficiency: ${String.format("%.1f", ranking.efficiency)}%",
                        color = Color.Gray,
                        fontSize = 9.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    // Performance indicator
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(
                                when {
                                    ranking.efficiency > 95f -> Color(0xFF4CAF50)
                                    ranking.efficiency > 90f -> Color(0xFF8BC34A)
                                    ranking.efficiency > 85f -> Color(0xFFFF9800)
                                    else -> Color(0xFFF44336)
                                },
                                CircleShape
                            )
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${String.format("%.1f", ranking.efficiency)}%",
                    color = when {
                        ranking.efficiency > 95f -> Color(0xFF4CAF50)
                        ranking.efficiency > 90f -> Color(0xFF8BC34A)
                        ranking.efficiency > 85f -> Color(0xFFFF9800)
                        else -> Color(0xFFF44336)
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                if (ranking.rank == 1) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PredictiveAnalyticsCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(300.dp), // Fixed height
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.TrendingUp,
                    contentDescription = null,
                    tint = Color(0xFF2196F3)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Predictive Analytics",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            PredictionItem(
                title = "Traffic Peak Prediction",
                prediction = "Next peak: 18:30 (+15min)",
                confidence = 94,
                impact = "High volume expected"
            )

            Spacer(modifier = Modifier.height(12.dp))

            PredictionItem(
                title = "Weather Impact",
                prediction = "Rain at 19:00 (70% chance)",
                confidence = 87,
                impact = "15% speed reduction"
            )

            Spacer(modifier = Modifier.height(12.dp))

            PredictionItem(
                title = "Incident Probability",
                prediction = "Low risk next 2 hours",
                confidence = 91,
                impact = "Normal operations"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1117))
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "AI Prediction Accuracy",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                    Text(
                        "94.7%",
                        color = Color(0xFF2196F3),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Last 30 days",
                        color = Color.Gray,
                        fontSize = 9.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PredictionItem(title: String, prediction: String, confidence: Int, impact: String) {
    Column {
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

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            prediction,
            color = Color.Gray,
            fontSize = 11.sp
        )

        Text(
            impact,
            color = Color(0xFF4CAF50),
            fontSize = 10.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Confidence bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(Color(0xFF30363D), RoundedCornerShape(2.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(confidence / 100f)
                    .background(Color(0xFF2196F3), RoundedCornerShape(2.dp))
            )
        }
    }
}

@Composable
fun AdaptivePeakHoursCard(modifier: Modifier = Modifier) {
    var selectedDay by remember { mutableStateOf(0) }
    val days = listOf("Today", "Tomorrow", "This Week")

    Card(
        modifier = modifier.height(300.dp), // Fixed height to match predictive analytics
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
                    "Adaptive Peak Analysis",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                // Day selector
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

            when (selectedDay) {
                0 -> { // Today
                    AdaptivePeakHourItem("Current Peak", "15:30 - 17:00", 87, true)
                    AdaptivePeakHourItem("Evening Rush", "17:30 - 19:00", 94, false)
                    AdaptivePeakHourItem("Late Traffic", "20:00 - 21:00", 45, false)
                }
                1 -> { // Tomorrow
                    AdaptivePeakHourItem("Morning Rush", "7:30 - 9:00", 89, false)
                    AdaptivePeakHourItem("Lunch Hour", "12:00 - 13:30", 67, false)
                    AdaptivePeakHourItem("Evening Rush", "17:00 - 19:00", 92, false)
                }
                2 -> { // This Week
                    AdaptivePeakHourItem("Peak Day", "Wednesday", 94, false)
                    AdaptivePeakHourItem("Light Day", "Sunday", 35, false)
                    AdaptivePeakHourItem("Average", "Weekdays", 78, false)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

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
fun AdaptivePeakHourItem(period: String, time: String, intensity: Int, isActive: Boolean) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) Color(0xFF1B5E20).copy(alpha = 0.3f) else Color(0xFF0D1117)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isActive) {
                            PulsingDot()
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                        Text(
                            period,
                            color = if (isActive) Color(0xFF4CAF50) else Color.White,
                            fontSize = 12.sp,
                            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                    Text(
                        time,
                        color = Color.Gray,
                        fontSize = 10.sp
                    )
                }
                Text(
                    "${intensity}%",
                    color = when {
                        intensity > 80 -> Color(0xFFF44336)
                        intensity > 60 -> Color(0xFFFF9800)
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
                        .fillMaxWidth(intensity / 100f)
                        .background(
                            when {
                                intensity > 80 -> Color(0xFFF44336)
                                intensity > 60 -> Color(0xFFFF9800)
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
fun ComprehensiveAIOptimizationCard(modifier: Modifier = Modifier) {
    var showDetails by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.height(320.dp), // Fixed height
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Psychology,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "AI Optimization Suite",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(
                    onClick = { showDetails = !showDetails }
                ) {
                    Icon(
                        if (showDetails) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Toggle details",
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ComprehensiveOptimizationMetric("Flow Improvement", "+23.4%", Color(0xFF4CAF50), Modifier.weight(1f))
                ComprehensiveOptimizationMetric("Wait Reduction", "-18.2%", Color(0xFF4CAF50), Modifier.weight(1f))
                ComprehensiveOptimizationMetric("Energy Savings", "+31.7%", Color(0xFF4CAF50), Modifier.weight(1f))
                ComprehensiveOptimizationMetric("Safety Score", "98.2%", Color(0xFF2196F3), Modifier.weight(1f))
            }

            AnimatedVisibility(visible = showDetails) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "Recent AI Optimizations",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(
                        modifier = Modifier.height(120.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(5) { index ->
                            val optimizations = listOf(
                                OptimizationEvent("Signal timing adjusted at Main St & 5th Ave", "2 min ago", "Flow +8%"),
                                OptimizationEvent("Route optimization for downtown corridor", "15 min ago", "Congestion -12%"),
                                OptimizationEvent("Predictive maintenance scheduled for Oak St", "1 hour ago", "Reliability +5%"),
                                OptimizationEvent("Emergency response route optimized", "2 hours ago", "Response -30s"),
                                OptimizationEvent("Traffic light coordination improved", "3 hours ago", "Efficiency +6%")
                            )

                            ComprehensiveOptimizationItem(optimizations[index])
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1117))
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "Models Running",
                                    color = Color.Gray,
                                    fontSize = 9.sp
                                )
                                Text(
                                    "12/12",
                                    color = Color(0xFF4CAF50),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1117))
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "Learning Rate",
                                    color = Color.Gray,
                                    fontSize = 9.sp
                                )
                                Text(
                                    "97.3%",
                                    color = Color(0xFF2196F3),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1117))
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "Next Update",
                                    color = Color.Gray,
                                    fontSize = 9.sp
                                )
                                Text(
                                    "3m 45s",
                                    color = Color(0xFFFF9800),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
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
fun ComprehensiveOptimizationMetric(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1117))
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                value,
                color = color,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                label,
                color = Color.Gray,
                fontSize = 8.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ComprehensiveOptimizationItem(event: OptimizationEvent) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(Color(0xFF4CAF50), CircleShape)
                .offset(y = 6.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                event.description,
                color = Color.White,
                fontSize = 11.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    event.timestamp,
                    color = Color.Gray,
                    fontSize = 9.sp
                )
                Text(
                    event.impact,
                    color = Color(0xFF4CAF50),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun EnhancedExportDataCard(modifier: Modifier = Modifier) {
    var isExporting by remember { mutableStateOf(false) }
    var lastExportTime by remember { mutableStateOf("2025-06-17 14:30 UTC") }

    Card(
        modifier = modifier.height(320.dp), // Fixed height to match AI optimization
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
                    "Data Export & Reports",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "for Alims-Repo",
                    color = Color.Gray,
                    fontSize = 10.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            EnhancedExportButton(
                text = "Export Real-time CSV",
                icon = Icons.Default.FileDownload,
                description = "Live traffic data",
                isExporting = isExporting,
                onClick = {
                    isExporting = true
                    lastExportTime = "2025-06-17 16:05 UTC"
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            EnhancedExportButton(
                text = "Generate Analytics PDF",
                icon = Icons.Default.PictureAsPdf,
                description = "Comprehensive analysis report",
                isExporting = false,
                onClick = { }
            )

            Spacer(modifier = Modifier.height(8.dp))

            EnhancedExportButton(
                text = "Share Live Dashboard",
                icon = Icons.Default.Share,
                description = "Real-time view link",
                isExporting = false,
                onClick = { }
            )

            Spacer(modifier = Modifier.height(8.dp))

            EnhancedExportButton(
                text = "API Data Access",
                icon = Icons.Default.Api,
                description = "RESTful endpoints",
                isExporting = false,
                onClick = { }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1117))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Last export:",
                            color = Color.Gray,
                            fontSize = 10.sp
                        )
                        Text(
                            lastExportTime,
                            color = Color.White,
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
                            color = Color.Gray,
                            fontSize = 10.sp
                        )
                        Text(
                            "Enabled (Daily at 00:00 UTC)",
                            color = Color(0xFF4CAF50),
                            fontSize = 10.sp
                        )
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
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(Color.Gray, CircleShape)
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
fun UserInsightsCard() {
    var showPersonalizedInsights by remember { mutableStateOf(true) }

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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.PersonalVideo,
                        contentDescription = null,
                        tint = Color(0xFF2196F3)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Personalized Insights for Alims-Repo",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Switch(
                    checked = showPersonalizedInsights,
                    onCheckedChange = { showPersonalizedInsights = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF2196F3),
                        checkedTrackColor = Color(0xFF1565C0)
                    ),
                    modifier = Modifier.scale(0.8f)
                )
            }

            AnimatedVisibility(visible = showPersonalizedInsights) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "Last Updated: 2025-06-17 16:05:53 UTC",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        UserMetricCard(
                            title = "Your Reports Generated",
                            value = "23",
                            period = "This Month",
                            modifier = Modifier.weight(1f)
                        )
                        UserMetricCard(
                            title = "Dashboard Access",
                            value = "187",
                            period = "Times This Week",
                            modifier = Modifier.weight(1f)
                        )
                        UserMetricCard(
                            title = "Optimization Requests",
                            value = "12",
                            period = "Submitted",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1117))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Recommended Actions",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            UserRecommendationItem(
                                title = "Review Downtown Corridor Performance",
                                description = "Traffic efficiency dropped 5% - investigate causes",
                                priority = "High",
                                timeEstimate = "15 min"
                            )

                            UserRecommendationItem(
                                title = "Export Weekly Analytics Report",
                                description = "Scheduled for your review - contains 7 new insights",
                                priority = "Medium",
                                timeEstimate = "5 min"
                            )

                                                        UserRecommendationItem(
                                title = "Update Alert Preferences",
                                description = "Customize notifications for better workflow",
                                priority = "Low",
                                timeEstimate = "3 min"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1565C0).copy(alpha = 0.2f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Default.TrendingUp,
                                    contentDescription = null,
                                    tint = Color(0xFF2196F3)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    "Your Impact",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    "18% improvement",
                                    color = Color(0xFF2196F3),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "in managed areas",
                                    color = Color.Gray,
                                    fontSize = 9.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF2E7D32).copy(alpha = 0.2f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Default.EmojiEvents,
                                    contentDescription = null,
                                    tint = Color(0xFFFFD700)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    "Achievement",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    "Traffic Expert",
                                    color = Color(0xFFFFD700),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "Level 3 unlocked",
                                    color = Color.Gray,
                                    fontSize = 9.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Session Duration: 3h 12m",
                            color = Color.Gray,
                            fontSize = 10.sp
                        )
                        Text(
                            "Next scheduled update: 17:00 UTC",
                            color = Color.Gray,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserMetricCard(title: String, value: String, period: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1117))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                value,
                color = Color(0xFF2196F3),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                title,
                color = Color.White,
                fontSize = 10.sp,
                textAlign = TextAlign.Center
            )
            Text(
                period,
                color = Color.Gray,
                fontSize = 9.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun UserRecommendationItem(title: String, description: String, priority: String, timeEstimate: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(
                    when (priority) {
                        "High" -> Color(0xFFF44336)
                        "Medium" -> Color(0xFFFF9800)
                        else -> Color(0xFF4CAF50)
                    },
                    CircleShape
                )
                .offset(y = 4.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
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

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = when (priority) {
                            "High" -> Color(0xFFF44336).copy(alpha = 0.2f)
                            "Medium" -> Color(0xFFFF9800).copy(alpha = 0.2f)
                            else -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                        }
                    )
                ) {
                    Text(
                        priority,
                        color = when (priority) {
                            "High" -> Color(0xFFF44336)
                            "Medium" -> Color(0xFFFF9800)
                            else -> Color(0xFF4CAF50)
                        },
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Text(
                description,
                color = Color.Gray,
                fontSize = 11.sp
            )

            Text(
                "Est. time: $timeEstimate",
                color = Color(0xFF2196F3),
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// Additional data classes for enhanced Analytics
data class ChartDataPoint(
    val hour: Int,
    val value: Int,
    val prediction: Boolean = false
)

data class OptimizationEvent(
    val description: String,
    val timestamp: String,
    val impact: String
)

// Enhanced IntersectionRanking to be mutable
data class IntersectionRanking(
    val name: String,
    var efficiency: Float,
    var rank: Int
)