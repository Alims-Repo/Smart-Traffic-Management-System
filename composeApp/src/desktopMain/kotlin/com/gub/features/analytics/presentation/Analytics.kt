package com.gub.features.analytics.presentation

import androidx.compose.runtime.Composable
import androidx.compose.foundation.*
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

@Composable
fun Analytics() {
    var selectedTimeRange by remember { mutableStateOf(0) }
    var selectedMetric by remember { mutableStateOf("traffic_flow") }

    val timeRanges = listOf("Last Hour", "Last 24H", "Last Week", "Last Month")

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Traffic Analytics",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                // Time Range Selector
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
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TrafficFlowMetricsCard(modifier = Modifier.weight(1f))
                PerformanceOverviewCard(modifier = Modifier.weight(1f))
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TrafficVolumeChartCard(
                    selectedTimeRange = timeRanges[selectedTimeRange],
                    modifier = Modifier.weight(2f)
                )
                IntersectionRankingCard(modifier = Modifier.weight(1f))
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                WaitTimeAnalysisCard(modifier = Modifier.weight(1f))
                EmissionDataCard(modifier = Modifier.weight(1f))
                PeakHoursCard(modifier = Modifier.weight(1f))
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AIOptimizationResultsCard(modifier = Modifier.weight(2f))
                ExportDataCard(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun TrafficFlowMetricsCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Traffic Flow Metrics",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            FlowMetricItem("Total Vehicles", "127,584", "+8.2%", true)
            FlowMetricItem("Average Speed", "28.5 mph", "+5.1%", true)
            FlowMetricItem("Congestion Level", "23%", "-15.3%", true)
            FlowMetricItem("Signal Efficiency", "94.2%", "+3.7%", true)
        }
    }
}

@Composable
fun FlowMetricItem(label: String, value: String, change: String, positive: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
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
    }
}

@Composable
fun PerformanceOverviewCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Performance Overview",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Performance Score Circle
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF4CAF50).copy(alpha = 0.3f),
                                    Color.Transparent
                                )
                            ),
                            shape = CircleShape
                        )
                        .border(3.dp, Color(0xFF4CAF50), CircleShape)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "89%",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Overall Score",
                            color = Color.Gray,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PerformanceIndicator("Efficiency", 92, Color(0xFF4CAF50))
                PerformanceIndicator("Safety", 88, Color(0xFF2196F3))
                PerformanceIndicator("Reliability", 85, Color(0xFFFF9800))
            }
        }
    }
}

@Composable
fun PerformanceIndicator(label: String, score: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "${score}%",
            color = color,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            label,
            color = Color.Gray,
            fontSize = 10.sp
        )
    }
}

@Composable
fun TrafficVolumeChartCard(selectedTimeRange: String, modifier: Modifier = Modifier) {
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
                    "Traffic Volume - $selectedTimeRange",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    Icons.Default.ShowChart,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Simulated Chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(0xFF0D1117), RoundedCornerShape(8.dp))
            ) {
                // Chart bars simulation
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    repeat(12) { index ->
                        val height = (40..180).random()
                        Box(
                            modifier = Modifier
                                .width(12.dp)
                                .height(height.dp)
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
                    }
                }

                // Chart overlay info
                Column(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    ChartLegendItem("Peak", Color(0xFF4CAF50))
                    ChartLegendItem("Average", Color(0xFF2196F3))
                    ChartLegendItem("Low", Color(0xFFFF9800))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Peak: 2,847 vehicles/hr", color = Color.Gray, fontSize = 11.sp)
                Text("Average: 1,523 vehicles/hr", color = Color.Gray, fontSize = 11.sp)
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
fun IntersectionRankingCard(modifier: Modifier = Modifier) {
    val intersections = listOf(
        IntersectionRanking("Main St & 5th Ave", 98.5f, 1),
        IntersectionRanking("Park Rd & Oak St", 94.2f, 2),
        IntersectionRanking("1st Ave & Broadway", 91.8f, 3),
        IntersectionRanking("Highway 101 & Center", 89.3f, 4),
        IntersectionRanking("Market St & Pine", 87.1f, 5)
    )

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Top Performing Intersections",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.height(240.dp)
            ) {
                items(intersections.size) { index ->
                    IntersectionRankingItem(intersections[index])
                }
            }
        }
    }
}

@Composable
fun IntersectionRankingItem(ranking: IntersectionRanking) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ranking badge
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    when (ranking.rank) {
                        1 -> Color(0xFFFFD700)
                        2 -> Color(0xFFC0C0C0)
                        3 -> Color(0xFFCD7F32)
                        else -> Color(0xFF424242)
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
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                "Efficiency: ${ranking.efficiency}%",
                color = Color.Gray,
                fontSize = 10.sp
            )
        }

        Text(
            "${ranking.efficiency}%",
            color = Color(0xFF4CAF50),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun WaitTimeAnalysisCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Wait Time Analysis",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            WaitTimeItem("Average Wait", "32.5s", Color(0xFF4CAF50))
            WaitTimeItem("Peak Wait", "1m 24s", Color(0xFFFF9800))
            WaitTimeItem("Longest Wait", "3m 12s", Color(0xFFF44336))

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Improvement vs Last Week",
                color = Color.Gray,
                fontSize = 12.sp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Icon(
                    Icons.Default.TrendingDown,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "12.4% reduction",
                    color = Color(0xFF4CAF50),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun WaitTimeItem(label: String, time: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color.Gray, fontSize = 12.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(color, CircleShape)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                time,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun EmissionDataCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Eco,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Emission Impact",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            EmissionItem("CO2 Saved", "2.3 tons", Color(0xFF4CAF50))
            EmissionItem("Fuel Saved", "847 gallons", Color(0xFF2196F3))
            EmissionItem("Idle Time Reduced", "156 hours", Color(0xFFFF9800))

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B5E20)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Environmental Score",
                        color = Color.White,
                        fontSize = 11.sp
                    )
                    Text(
                        "A+",
                        color = Color(0xFF4CAF50),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun EmissionItem(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color.Gray, fontSize = 12.sp)
        Text(
            value,
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun PeakHoursCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Peak Hours",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            PeakHourItem("Morning Rush", "7:30 - 9:00 AM", 89)
            PeakHourItem("Lunch Hour", "12:00 - 1:00 PM", 67)
            PeakHourItem("Evening Rush", "5:00 - 6:30 PM", 94)
            PeakHourItem("Late Evening", "8:00 - 9:00 PM", 45)

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Next predicted peak in 2h 15m",
                color = Color(0xFF4CAF50),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun PeakHourItem(period: String, time: String, intensity: Int) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    period,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
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
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
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

@Composable
fun AIOptimizationResultsCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
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
                    "AI Optimization Results",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OptimizationMetric("Flow Improvement", "+23.4%", Color(0xFF4CAF50), Modifier.weight(1f))
                OptimizationMetric("Wait Time Reduction", "-18.2%", Color(0xFF4CAF50), Modifier.weight(1f))
                OptimizationMetric("Energy Savings", "+31.7%", Color(0xFF4CAF50), Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Recent Optimizations",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))

            OptimizationItem("Signal timing adjusted at Main St & 5th Ave", "2 hours ago")
            OptimizationItem("Route optimization for downtown corridor", "4 hours ago")
            OptimizationItem("Predictive maintenance scheduled for Oak St", "6 hours ago")
        }
    }
}

@Composable
fun OptimizationMetric(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
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
                color = color,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                label,
                color = Color.Gray,
                fontSize = 10.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun OptimizationItem(description: String, time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(Color(0xFF4CAF50), CircleShape)
                .offset(y = 6.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                description,
                color = Color.White,
                fontSize = 12.sp
            )
            Text(
                time,
                color = Color.Gray,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun ExportDataCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Export & Reports",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            ExportButton(
                text = "Export CSV",
                icon = Icons.Default.FileDownload,
                description = "Raw traffic data"
            )

            Spacer(modifier = Modifier.height(8.dp))

            ExportButton(
                text = "Generate PDF Report",
                icon = Icons.Default.PictureAsPdf,
                description = "Comprehensive analysis"
            )

            Spacer(modifier = Modifier.height(8.dp))

            ExportButton(
                text = "Share Dashboard",
                icon = Icons.Default.Share,
                description = "Live view link"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Last export: 2025-06-17 14:30 UTC",
                color = Color.Gray,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun ExportButton(text: String, icon: ImageVector, description: String) {
    OutlinedButton(
        onClick = { },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2196F3)),
        border = BorderStroke(1.dp, Color(0xFF2196F3).copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text,
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

// Data classes for Analytics
data class IntersectionRanking(
    val name: String,
    val efficiency: Float,
    val rank: Int
)