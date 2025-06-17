import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrafficManagementApp() {
    var selectedTab by remember { mutableStateOf(0) }
    var isAiEnabled by remember { mutableStateOf(true) }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF4CAF50),
            secondary = Color(0xFF2196F3),
            background = Color(0xFF0D1117),
            surface = Color(0xFF161B22),
            onSurface = Color.White,
            onBackground = Color.White
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0D1117),
                            Color(0xFF161B22)
                        )
                    )
                )
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                // Sidebar Navigation
                NavigationSidebar(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    isAiEnabled = isAiEnabled,
                    onAiToggle = { isAiEnabled = it }
                )

                // Main Content
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    when (selectedTab) {
                        0 -> DashboardScreen()
                        1 -> TrafficMonitoringScreen()
                        2 -> AnalyticsScreen()
                        3 -> SettingsScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun NavigationSidebar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    isAiEnabled: Boolean,
    onAiToggle: (Boolean) -> Unit
) {
    val navItems = listOf(
        NavItem("Dashboard", Icons.Default.Dashboard, 0),
        NavItem("Monitoring", Icons.Default.Traffic, 1),
        NavItem("Analytics", Icons.Default.Analytics, 2),
        NavItem("Settings", Icons.Default.Settings, 3)
    )

    Column(
        modifier = Modifier
            .width(280.dp)
            .fillMaxHeight()
            .background(Color(0xFF21262D))
            .padding(16.dp)
    ) {
        // Logo/Title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFF4CAF50), Color(0xFF2E7D32))
                        ),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    Icons.Default.Traffic,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "Traffic AI",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // AI Status Toggle
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isAiEnabled) Color(0xFF1B5E20) else Color(0xFF424242)
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Psychology,
                    contentDescription = null,
                    tint = if (isAiEnabled) Color(0xFF4CAF50) else Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "AI Control",
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = isAiEnabled,
                    onCheckedChange = onAiToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF4CAF50),
                        checkedTrackColor = Color(0xFF1B5E20)
                    )
                )
            }
        }

        // Navigation Items
        navItems.forEach { item ->
            NavigationItem(
                item = item,
                selected = selectedTab == item.index,
                onClick = { onTabSelected(item.index) }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // System Status
        SystemStatusCard()
    }
}

@Composable
fun NavigationItem(
    item: NavItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (selected) Color(0xFF2E7D32) else Color.Transparent
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                item.icon,
                contentDescription = item.title,
                tint = if (selected) Color.White else Color.Gray
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                item.title,
                color = if (selected) Color.White else Color.Gray,
                fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
            )
        }
    }
}

@Composable
fun SystemStatusCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1117))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "System Status",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            StatusItem("CPU", "23%", Color(0xFF4CAF50))
            StatusItem("Memory", "67%", Color(0xFFFF9800))
            StatusItem("Network", "Active", Color(0xFF4CAF50))
        }
    }
}

@Composable
fun StatusItem(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color.Gray, fontSize = 12.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, CircleShape)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(value, color = Color.White, fontSize = 12.sp)
        }
    }
}

@Composable
fun DashboardScreen() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                "Traffic Management Dashboard",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MetricCard(
                    title = "Active Intersections",
                    value = "247",
                    change = "+5.2%",
                    positive = true,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Average Wait Time",
                    value = "32s",
                    change = "-12.4%",
                    positive = true,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Traffic Flow",
                    value = "98.5%",
                    change = "+2.1%",
                    positive = true,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                RealtimeTrafficCard(modifier = Modifier.weight(2f))
                AlertsCard(modifier = Modifier.weight(1f))
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AIInsightsCard(modifier = Modifier.weight(1f))
                QuickActionsCard(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    change: String,
    positive: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                title,
                color = Color.Gray,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                value,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    if (positive) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                    contentDescription = null,
                    tint = if (positive) Color(0xFF4CAF50) else Color(0xFFF44336),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    change,
                    color = if (positive) Color(0xFF4CAF50) else Color(0xFFF44336),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun RealtimeTrafficCard(modifier: Modifier = Modifier) {
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
                    "Real-time Traffic Flow",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                PulsingDot()
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Simulated traffic visualization
            repeat(5) { index ->
                TrafficLane(
                    name = "Lane ${index + 1}",
                    density = (30..95).random(),
                    speed = (25..60).random()
                )
                if (index < 4) Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun TrafficLane(name: String, density: Int, speed: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            name,
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.width(60.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))

        // Density bar
        Box(
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .background(Color(0xFF30363D), RoundedCornerShape(4.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(density / 100f)
                    .background(
                        when {
                            density > 80 -> Color(0xFFF44336)
                            density > 60 -> Color(0xFFFF9800)
                            else -> Color(0xFF4CAF50)
                        },
                        RoundedCornerShape(4.dp)
                    )
            )
        }

        Spacer(modifier = Modifier.width(8.dp))
        Text(
            "${speed}mph",
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier.width(50.dp)
        )
    }
}

@Composable
fun PulsingDot() {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .size(12.dp)
            .background(
                Color(0xFF4CAF50).copy(alpha = alpha),
                CircleShape
            )
    )
}

@Composable
fun AlertsCard(modifier: Modifier = Modifier) {
    val alerts = listOf(
        Alert("High congestion detected", "Main St & 5th Ave", AlertLevel.HIGH),
        Alert("Signal malfunction", "Park Rd & Oak St", AlertLevel.CRITICAL),
        Alert("Maintenance scheduled", "Downtown Loop", AlertLevel.INFO),
        Alert("Weather impact", "Highway 101", AlertLevel.MEDIUM)
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
                "Active Alerts",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(200.dp)
            ) {
                items(alerts) { alert ->
                    AlertItem(alert)
                }
            }
        }
    }
}

@Composable
fun AlertItem(alert: Alert) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(alert.level.color, CircleShape)
                .offset(y = 4.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                alert.message,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                alert.location,
                color = Color.Gray,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun AIInsightsCard(modifier: Modifier = Modifier) {
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
                    "AI Insights",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            InsightItem(
                title = "Optimal Timing Adjustment",
                description = "Suggested 15% increase in green time for Main St during peak hours",
                impact = "+23% flow improvement"
            )

            Spacer(modifier = Modifier.height(12.dp))

            InsightItem(
                title = "Predictive Maintenance",
                description = "Traffic signal at 2nd Ave showing early wear patterns",
                impact = "Schedule in 2 weeks"
            )
        }
    }
}

@Composable
fun InsightItem(title: String, description: String, impact: String) {
    Column {
        Text(
            title,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            description,
            color = Color.Gray,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            impact,
            color = Color(0xFF4CAF50),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun QuickActionsCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Quick Actions",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            ActionButton(
                text = "Emergency Override",
                icon = Icons.Default.Warning,
                color = Color(0xFFF44336)
            )

            Spacer(modifier = Modifier.height(8.dp))

            ActionButton(
                text = "Optimize Routes",
                icon = Icons.Default.Route,
                color = Color(0xFF2196F3)
            )

            Spacer(modifier = Modifier.height(8.dp))

            ActionButton(
                text = "Generate Report",
                icon = Icons.Default.Assessment,
                color = Color(0xFF4CAF50)
            )
        }
    }
}

@Composable
fun ActionButton(text: String, icon: ImageVector, color: Color) {
    OutlinedButton(
        onClick = { },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = color
        ),
        border = BorderStroke(1.dp, color.copy(alpha = 0.5f))
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, fontSize = 12.sp)
    }
}

@Composable
fun TrafficMonitoringScreen() {
    Text(
        "Traffic Monitoring - Real-time intersection views and controls would be implemented here",
        color = Color.White,
        fontSize = 16.sp
    )
}

@Composable
fun AnalyticsScreen() {
    Text(
        "Analytics - Detailed charts and performance metrics would be displayed here",
        color = Color.White,
        fontSize = 16.sp
    )
}

@Composable
fun SettingsScreen() {
    Text(
        "Settings - System configuration options would be available here",
        color = Color.White,
        fontSize = 16.sp
    )
}

// Data classes
data class NavItem(val title: String, val icon: ImageVector, val index: Int)
data class Alert(val message: String, val location: String, val level: AlertLevel)

enum class AlertLevel(val color: Color) {
    INFO(Color(0xFF2196F3)),
    MEDIUM(Color(0xFFFF9800)),
    HIGH(Color(0xFFF44336)),
    CRITICAL(Color(0xFFD32F2F))
}

@Preview
@Composable
fun PreviewApp() {
    TrafficManagementApp()
}