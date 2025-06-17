package com.gub.features.dashboard.presentation

import androidx.compose.runtime.Composable
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.animation.core.*
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gub.core.ui.components.PulsingDot
import com.gub.core.ui.components.UserProfileChip
import com.gub.features.analytics.presentation.LiveIntersectionRankingCard

@Composable
fun Dashboard() {
    var selectedTab by remember { mutableStateOf(0) }
    var showQuickActions by remember { mutableStateOf(false) }
    var selectedTimeFrame by remember { mutableStateOf(0) }
    
    val currentTime = "2025-06-17 16:28:43 UTC"
    val dashboardTabs = listOf("Overview", "Live Status", "Quick Actions", "Insights")
    val timeFrames = listOf("Real-time", "Last Hour", "Last 6H", "Today")

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                // Dashboard Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Traffic Control Dashboard",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B5E20))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    PulsingDot()
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        "LIVE",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Text(
                                "Welcome back, Alims-Repo",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "•",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                currentTime,
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Time Frame Selector
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            timeFrames.forEachIndexed { index, frame ->
                                FilterChip(
                                    onClick = { selectedTimeFrame = index },
                                    label = {
                                        Text(
                                            frame,
                                            fontSize = 10.sp,
                                            color = if (selectedTimeFrame == index) Color.White else Color.Gray
                                        )
                                    },
                                    selected = selectedTimeFrame == index,
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFF2E7D32),
                                        containerColor = Color(0xFF161B22)
                                    )
                                )
                            }
                        }

                        UserProfileChip()
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Dashboard Navigation Tabs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    dashboardTabs.forEachIndexed { index, tab ->
                        FilterChip(
                            onClick = { selectedTab = index },
                            label = {
                                Text(
                                    tab,
                                    fontSize = 12.sp,
                                    color = if (selectedTab == index) Color.White else Color.Gray
                                )
                            },
                            selected = selectedTab == index,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF1976D2),
                                containerColor = Color(0xFF161B22)
                            )
                        )
                    }
                }
            }
        }

        // Dashboard Content based on selected tab
        when (selectedTab) {
            0 -> { // Overview
                item { SystemOverviewCard() }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        LiveTrafficMetricsCard(modifier = Modifier.weight(1f))
                        AIStatusCard(modifier = Modifier.weight(1f))
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        RecentAlertsCard(modifier = Modifier.weight(1f))
                        TopPerformingIntersectionsCard(modifier = Modifier.weight(1f))
                    }
                }
                item { TodaysActivitySummaryCard() }
            }
            1 -> { // Live Status
                item { LiveSystemStatusCard() }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        RealTimeTrafficFlowCard(modifier = Modifier.weight(0.6f))
                        ActiveIncidentsCard(modifier = Modifier.weight(0.4f))
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        LiveIntersectionRankingCard()
                        LiveIntersectionMapCard(modifier = Modifier.weight(1f))
                        CurrentOptimizationsCard(modifier = Modifier.weight(1f))
                    }
                }
            }
            2 -> { // Quick Actions
                item { QuickActionsGrid() }
            }
            3 -> { // Insights
                item { UserInsightsDashboardCard() }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        PredictiveInsightsCard(modifier = Modifier.weight(1f))
                        PerformanceTrendsCard(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun SystemOverviewCard() {
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
                    "System Overview",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Dashboard,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "16:28:43",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SystemOverviewMetric(
                    title = "System Health",
                    value = "98.7%",
                    subtitle = "Excellent",
                    color = Color(0xFF4CAF50),
                    icon = Icons.Default.CheckCircle
                )

                SystemOverviewMetric(
                    title = "Active Intersections",
                    value = "247/250",
                    subtitle = "99.2% Online",
                    color = Color(0xFF4CAF50),
                    icon = Icons.Default.Traffic
                )

                SystemOverviewMetric(
                    title = "AI Performance",
                    value = "97.1%",
                    subtitle = "Optimal",
                    color = Color(0xFF4CAF50),
                    icon = Icons.Default.Psychology
                )

                SystemOverviewMetric(
                    title = "Current Flow",
                    value = "2,847",
                    subtitle = "vehicles/hr",
                    color = Color(0xFF2196F3),
                    icon = Icons.Default.DirectionsCar
                )

                SystemOverviewMetric(
                    title = "Avg Wait Time",
                    value = "31.2s",
                    subtitle = "↓ 18% today",
                    color = Color(0xFF4CAF50),
                    icon = Icons.Default.Timer
                )
            }
        }
    }
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
                    "Live Traffic Metrics",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B5E20))
                ) {
                    Text(
                        "REAL-TIME",
                        color = Color.White,
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
fun AIStatusCard(modifier: Modifier = Modifier) {
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Psychology,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "AI Control System",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B5E20))
                ) {
                    Text(
                        "ACTIVE",
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // AI Performance Gauge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
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
                        .padding(2.dp)
                        .background(Color(0xFF161B22), CircleShape)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "97.1%",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Efficiency",
                            color = Color.Gray,
                            fontSize = 8.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            AIStatusItem("Models Running", "12/12", Color(0xFF4CAF50))
            AIStatusItem("Decision Speed", "0.28ms", Color(0xFF4CAF50))
            AIStatusItem("Learning Rate", "97.8%", Color(0xFF2196F3))

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Last optimization: 3 minutes ago",
                color = Color.Gray,
                fontSize = 10.sp,
                style = androidx.compose.ui.text.TextStyle(
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            )
        }
    }
}

@Composable
fun AIStatusItem(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color.Gray, fontSize = 10.sp)
        Text(
            value,
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun RecentAlertsCard(modifier: Modifier = Modifier) {
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
                    "Recent Alerts",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "3 Active",
                    color = Color(0xFFFF9800),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AlertItem(
                title = "High Traffic Volume",
                location = "Main St & 5th Ave",
                time = "2 min ago",
                severity = "Medium",
                isActive = true
            )

            AlertItem(
                title = "Signal Malfunction",
                location = "Oak St & 3rd Ave",
                time = "8 min ago",
                severity = "High",
                isActive = true
            )

            AlertItem(
                title = "Congestion Detected",
                location = "Highway 101 Corridor",
                time = "15 min ago",
                severity = "Low",
                isActive = false
            )

            AlertItem(
                title = "AI Optimization Complete",
                location = "Downtown District",
                time = "23 min ago",
                severity = "Info",
                isActive = false
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2196F3)),
                border = BorderStroke(1.dp, Color(0xFF2196F3).copy(alpha = 0.5f))
            ) {
                Text("View All Alerts", fontSize = 12.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun AlertItem(title: String, location: String, time: String, severity: String, isActive: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(
                    when (severity) {
                        "High" -> Color(0xFFF44336)
                        "Medium" -> Color(0xFFFF9800)
                        "Low" -> Color(0xFF2196F3)
                        else -> Color(0xFF4CAF50)
                    },
                    CircleShape
                )
        )

        if (isActive) {
            Spacer(modifier = Modifier.width(4.dp))
            PulsingDot()
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                color = if (isActive) Color.White else Color.Gray,
                fontSize = 11.sp,
                fontWeight = if (isActive) FontWeight.Medium else FontWeight.Normal
            )
            Text(
                location,
                color = Color.Gray,
                fontSize = 9.sp
            )
        }

        Text(
            time,
            color = Color.Gray,
            fontSize = 9.sp
        )
    }
}

@Composable
fun TopPerformingIntersectionsCard(modifier: Modifier = Modifier) {
    val intersections = listOf(
        IntersectionPerformance("Main St & 5th Ave", 98.7f, "+2.3%"),
        IntersectionPerformance("Park Rd & Oak St", 94.8f, "+1.1%"),
        IntersectionPerformance("1st Ave & Broadway", 92.4f, "+0.8%"),
        IntersectionPerformance("Highway 101 & Center", 90.1f, "-0.5%")
    )

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
                    "Top Performing Intersections",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            intersections.forEachIndexed { index, intersection ->
                TopIntersectionItem(intersection, index + 1)
                if (index < intersections.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF4CAF50)),
                border = BorderStroke(1.dp, Color(0xFF4CAF50).copy(alpha = 0.5f))
            ) {
                Text("View Full Rankings", fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun TopIntersectionItem(intersection: IntersectionPerformance, rank: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(
                    when (rank) {
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
                rank.toString(),
                color = if (rank <= 3) Color.Black else Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                intersection.name,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                "Efficiency: ${String.format("%.1f", intersection.efficiency)}%",
                color = Color.Gray,
                fontSize = 9.sp
            )
        }

        Text(
            intersection.change,
            color = if (intersection.change.startsWith("+")) Color(0xFF4CAF50) else Color(0xFFF44336),
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
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
fun LiveSystemStatusCard() {
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
                    "Live System Status",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    PulsingDot()
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "All Systems Operational",
                        color = Color(0xFF4CAF50),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                LiveStatusIndicator("Traffic Signals", "247/250", Color(0xFF4CAF50), "98.8%")
                LiveStatusIndicator("AI Systems", "Online", Color(0xFF4CAF50), "100%")
                LiveStatusIndicator("Data Streams", "Active", Color(0xFF4CAF50), "99.2%")
                LiveStatusIndicator("Network", "Stable", Color(0xFF4CAF50), "Low Latency")
                LiveStatusIndicator("Storage", "Available", Color(0xFF2196F3), "73% Free")
            }
        }
    }
}

@Composable
fun LiveStatusIndicator(title: String, status: String, color: Color, detail: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            title,
            color = Color.Gray,
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
        Text(
            status,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            detail,
            color = color,
            fontSize = 9.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun RealTimeTrafficFlowCard(modifier: Modifier = Modifier) {
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
                    "Real-Time Traffic Flow",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B5E20))
                ) {
                    Text(
                        text = "LIVE DATA",
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Simulated real-time chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color(0xFF0D1117), RoundedCornerShape(8.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    repeat(12) { index ->
                        val height = when (index) {
                            in 7..9 -> (120..150).random() // Current high traffic
                            in 4..6 -> (80..110).random()
                            else -> (40..80).random()
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(8.dp)
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
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "${(index + 9) % 24}:00",
                                color = Color.Gray,
                                fontSize = 8.sp
                            )
                        }
                    }
                }

                // Current time indicator
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF21262D))
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Current",
                                color = Color.Gray,
                                fontSize = 9.sp
                            )
                            Text(
                                "2,847 veh/hr",
                                color = Color(0xFF4CAF50),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Peak: 18:15 (3,142 vehicles/hr)", color = Color.Gray, fontSize = 10.sp)
                Text("Efficiency: 95.3%", color = Color(0xFF4CAF50), fontSize = 10.sp)
            }
        }
    }
}

@Composable
fun ActiveIncidentsCard(modifier: Modifier = Modifier) {
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
                    "Active Incidents",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFF9800).copy(alpha = 0.2f)
                    )
                ) {
                    Text(
                        "2 ACTIVE",
                        color = Color(0xFFFF9800),
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            IncidentItem(
                type = "Signal Malfunction",
                location = "Oak St & 3rd Ave",
                duration = "8 min",
                severity = "High",
                status = "Technician Dispatched"
            )

            Spacer(modifier = Modifier.height(8.dp))

            IncidentItem(
                type = "Heavy Traffic",
                location = "Main St Corridor",
                duration = "15 min",
                severity = "Medium",
                status = "AI Rerouting Active"
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1117))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "Recent Resolution",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                    Text(
                        "Congestion at Highway 101 - Cleared 23 min ago",
                        color = Color(0xFF4CAF50),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF9800)),
                border = BorderStroke(1.dp, Color(0xFFFF9800).copy(alpha = 0.5f))
            ) {
                Text("Incident Management", fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun IncidentItem(type: String, location: String, duration: String, severity: String, status: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(
                    when (severity) {
                        "High" -> Color(0xFFF44336)
                        "Medium" -> Color(0xFFFF9800)
                        else -> Color(0xFF2196F3)
                    },
                    CircleShape
                )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                type,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                location,
                color = Color.Gray,
                fontSize = 9.sp
            )
            Text(
                status,
                color = Color(0xFF2196F3),
                fontSize = 9.sp
            )
        }

        Text(
            duration,
            color = Color.Gray,
            fontSize = 9.sp
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
fun QuickActionsGrid() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            "Quick Actions",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        // Emergency Actions
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    "Emergency Controls",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionButton(
                        title = "Emergency Mode",
                        icon = Icons.Default.Warning,
                        color = Color(0xFFF44336),
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButton(
                        title = "All Red Signals",
                        icon = Icons.Default.StopCircle,
                        color = Color(0xFFF44336),
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButton(
                        title = "Priority Route",
                        icon = Icons.Default.LocalHospital,
                        color = Color(0xFFFF9800),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // System Controls
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    "System Controls",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionButton(
                        title = "AI Optimization",
                        icon = Icons.Default.Psychology,
                        color = Color(0xFF4CAF50),
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButton(
                        title = "Manual Override",
                        icon = Icons.Default.Settings,
                        color = Color(0xFF2196F3),
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButton(
                        title = "System Reset",
                        icon = Icons.Default.RestartAlt,
                        color = Color(0xFFFF9800),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Reports & Analytics
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    "Reports & Analytics",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionButton(
                        title = "Generate Report",
                        icon = Icons.Default.Assessment,
                        color = Color(0xFF9C27B0),
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButton(
                        title = "Export Data",
                        icon = Icons.Default.FileDownload,
                        color = Color(0xFF607D8B),
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButton(
                        title = "Schedule Analysis",
                        icon = Icons.Default.Schedule,
                        color = Color(0xFF795548),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun QuickActionButton(title: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable { },
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
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
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun UserInsightsDashboardCard() {
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
                    "Personal Insights - Alims-Repo",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1976D2))
                ) {
                    Text(
                        "LEVEL 3",
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                UserInsightMetric("Session Time", "3h 31m", "Today", Color(0xFF2196F3))
                UserInsightMetric("Actions Taken", "17", "This Session", Color(0xFF4CAF50))
                UserInsightMetric("Reports Generated", "24", "This Month", Color(0xFF9C27B0))
                UserInsightMetric("Optimizations", "13", "Requested", Color(0xFF32519))
                UserInsightMetric("Efficiency Gain", "19.4%", "Your Impact", Color(0xFF4CAF50))
            }
        }
    }
}

@Composable
fun UserInsightMetric(title: String, value: String, subtitle: String, color: Color) {
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

data class IntersectionPerformance(
    val name: String,
    val efficiency: Float,
    val change:String
)

// Data classes for Dashboard
//data class IntersectionPerformance(
//    val name: String,
//    val efficiency: Float,
//    val change: String
//)