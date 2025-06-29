package com.gub.features.monitoring.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gub.core.ui.components.PulsingDot
import com.gub.features.dashboard.presentation.components.TopBarDashboard
import com.gub.features.monitoring.presentation.components.IntersectionOverviewCard
import com.gub.features.monitoring.presentation.components.LiveCameraFeedCard
import com.gub.features.monitoring.presentation.components.TopBarMonitoring
import com.gub.utils.UiCalculations.toDp
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState

@Composable
fun Monitoring() {
    var selectedIntersection by remember { mutableStateOf(0) }
    var isLiveView by remember { mutableStateOf(true) }
    var isCameraExpanded by remember { mutableStateOf(false) }

    val hazeState = rememberHazeState()
    var height by remember { mutableStateOf(0) }

//    AnimatedContent(
//        targetState = isCameraExpanded
//    ) {
//        if (it) {
//            if (isCameraExpanded)
//                // Full width camera feed when expanded
//                LiveCameraFeedCard(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .animateContentSize(
//                            animationSpec = tween(300)
//                        ),
//                    isExpanded = true,
//                    onToggleExpand = { isCameraExpanded = !isCameraExpanded }
//                )
//        } else {
//
//        }
//    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .hazeSource(hazeState),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = height.toDp() + 24.dp, start = 24.dp, end = 24.dp, bottom = 24.dp)
    ) {

        if (isCameraExpanded) {
            item {
                LiveCameraFeedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(
                            animationSpec = tween(300)
                        ),
                    isExpanded = true,
                    onToggleExpand = { isCameraExpanded = !isCameraExpanded }
                )
            }
        } else {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    IntersectionOverviewCard(modifier = Modifier.weight(2f))
                    TrafficControlPanelCard(modifier = Modifier.weight(1f))
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    IntersectionSelectorCard(
                        selectedIntersection = selectedIntersection,
                        onIntersectionSelected = { selectedIntersection = it },
                        modifier = Modifier.weight(1f)
                    )
                    LiveCameraFeedCard(
                        modifier = Modifier
                            .weight(1f)
                            .animateContentSize(
                                animationSpec = tween(300)
                            ),
                        isExpanded = false,
                        onToggleExpand = { isCameraExpanded = !isCameraExpanded }
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    VehicleCountCard(modifier = Modifier.weight(1f))
                    SignalTimingCard(modifier = Modifier.weight(1f))
                    EnvironmentalDataCard(modifier = Modifier.weight(1f))
                }
            }
        }
    }

    TopBarMonitoring(
        hazeState,
        topBarHeight = { height = it }
    )
}

// ... rest of your existing composables remain the same ...

@Composable
fun TrafficControlPanelCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Traffic Control",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            ControlButton(
                text = "Emergency Override",
                icon = Icons.Default.Warning,
                color = Color(0xFFF44336),
                description = "Clear all lanes immediately"
            )

            Spacer(modifier = Modifier.height(12.dp))

            ControlButton(
                text = "Extend Green Time",
                icon = Icons.Default.Timer,
                color = Color(0xFF4CAF50),
                description = "Add 30 seconds to current phase"
            )

            Spacer(modifier = Modifier.height(12.dp))

            ControlButton(
                text = "Manual Control",
                icon = Icons.Default.TouchApp,
                color = MaterialTheme.colorScheme.primary,
                description = "Take direct control of signals"
            )

            Spacer(modifier = Modifier.height(12.dp))

            ControlButton(
                text = "Reset to Auto",
                icon = Icons.Default.Autorenew,
                color = Color(0xFFFF9800),
                description = "Return to AI optimization"
            )
        }
    }
}

@Composable
fun ControlButton(text: String, icon: ImageVector, color: Color, description: String) {
    OutlinedButton(
        onClick = { },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = color),
        border = BorderStroke(1.dp, color.copy(alpha = 0.5f))
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            }
            Text(
                description,
                fontSize = 9.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun IntersectionSelectorCard(
    selectedIntersection: Int,
    onIntersectionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val intersections = listOf(
        "Main St & 5th Ave",
        "Park Rd & Oak St",
        "1st Ave & Broadway",
        "Highway 101 & Center St",
        "Market St & Pine Ave"
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
                "Select Intersection",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(160.dp)
            ) {
                items(intersections.size) { index ->
                    IntersectionItem(
                        name = intersections[index],
                        isSelected = selectedIntersection == index,
                        onClick = { onIntersectionSelected(index) },
                        status = when (index % 3) {
                            0 -> "Normal"
                            1 -> "Congested"
                            else -> "Alert"
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun IntersectionItem(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    status: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF2E7D32) else Color(0xFF0D1117)
        ),
        shape = RoundedCornerShape(6.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    name,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    status,
                    color = when (status) {
                        "Normal" -> Color(0xFF4CAF50)
                        "Congested" -> Color(0xFFFF9800)
                        else -> Color(0xFFF44336)
                    },
                    fontSize = 10.sp
                )
            }

            if (isSelected) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun VehicleCountCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Vehicle Count",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            VehicleCountItem("Cars", 156, Color(0xFF2196F3))
            VehicleCountItem("Trucks", 23, Color(0xFFFF9800))
            VehicleCountItem("Buses", 8, Color(0xFF4CAF50))
            VehicleCountItem("Motorcycles", 12, Color(0xFF9C27B0))
        }
    }
}

@Composable
fun VehicleCountItem(type: String, count: Int, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(type, color = Color.Gray, fontSize = 12.sp)
        }
        Text(
            count.toString(),
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun SignalTimingCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Signal Timing",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            SignalPhase("North-South", 45, 12, TrafficLightState.GREEN)
            SignalPhase("East-West", 30, 0, TrafficLightState.RED)
            SignalPhase("Left Turn", 15, 0, TrafficLightState.RED)
        }
    }
}

@Composable
fun SignalPhase(direction: String, totalTime: Int, remaining: Int, state: TrafficLightState) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(state.color, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(direction, color = Color.Gray, fontSize = 12.sp)
            }
            Text(
                "${remaining}s",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
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
                    .fillMaxWidth(if (totalTime > 0) remaining.toFloat() / totalTime else 0f)
                    .background(state.color, RoundedCornerShape(2.dp))
            )
        }
    }
}

@Composable
fun EnvironmentalDataCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Environmental",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            EnvironmentalItem("Visibility", "Clear", Color(0xFF4CAF50))
            EnvironmentalItem("Weather", "Sunny", Color(0xFF4CAF50))
            EnvironmentalItem("Temperature", "72°F", Color(0xFF4CAF50))
            EnvironmentalItem("Wind", "5 mph", Color(0xFF4CAF50))
        }
    }
}

@Composable
fun EnvironmentalItem(label: String, value: String, color: Color) {
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
                value,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// Additional data classes for the monitoring screen
enum class TrafficLightState(val color: Color) {
    RED(Color(0xFFF44336)),
    YELLOW(Color(0xFFFF9800)),
    GREEN(Color(0xFF4CAF50))
}