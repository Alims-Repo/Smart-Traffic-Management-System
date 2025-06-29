package com.gub.features.monitoring.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingFlat
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Traffic
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingFlat
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.TurnLeft
import androidx.compose.material.icons.filled.TurnRight
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.gub.core.ui.components.PulsingDot
import com.gub.features.monitoring.presentation.TrafficLightState
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun IntersectionOverviewCard(modifier: Modifier = Modifier) {

    var selectedView by remember { mutableStateOf("Live") }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0B1426)
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            // Modern Header
            ModernHeader()

            Spacer(modifier = Modifier.height(20.dp))

            // View Selector
            ViewSelector(selectedView) { selectedView = it }

            Spacer(modifier = Modifier.height(24.dp))

            // Main Intersection Display
            when (selectedView) {
                "Live" -> LiveIntersectionView()
                "Stats" -> StatsView()
                "History" -> HistoryView()
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Quick Metrics
            QuickMetrics()
        }
    }
}

@Composable
private fun ModernHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF00D4FF), Color(0xFF0099CC))
                            ),
                            RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Traffic,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        "Broadway & 42nd Street",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Times Square District • User: Alims-Repo",
                        color = Color(0xFF94A3B8),
                        fontSize = 11.sp
                    )
                }
            }
        }

        StatusBadge()
    }
}

@Composable
private fun StatusBadge() {
    var pulseAlpha by remember { mutableStateOf(0.8f) }

    LaunchedEffect(Unit) {
        while (true) {
            pulseAlpha = 1f
            delay(600)
            pulseAlpha = 0.4f
            delay(600)
        }
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF059669).copy(alpha = 0.2f)
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFF059669).copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp, 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        Color(0xFF10B981).copy(alpha = pulseAlpha),
                        CircleShape
                    )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    "ACTIVE",
                    color = Color(0xFF10B981),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Text(
                    "2025-06-23 09:18:36",
                    color = Color(0xFF6B7280),
                    fontSize = 8.sp
                )
            }
        }
    }
}

@Composable
private fun ViewSelector(selectedView: String, onViewChange: (String) -> Unit) {
    val views = listOf("Live", "Stats", "History")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFF1F2937).copy(alpha = 0.5f),
                RoundedCornerShape(12.dp)
            )
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        views.forEach { view ->
            val isSelected = view == selectedView

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        if (isSelected) {
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF3B82F6), Color(0xFF1D4ED8))
                            )
                        } else {
                            Brush.linearGradient(
                                colors = listOf(Color.Transparent, Color.Transparent)
                            )
                        },
                        RoundedCornerShape(8.dp)
                    )
                    .clickable { onViewChange(view) }
                    .padding(12.dp, 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    view,
                    color = if (isSelected) Color.White else Color(0xFF9CA3AF),
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun LiveIntersectionView() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF1E3A8A).copy(alpha = 0.15f),
                        Color.Transparent
                    ),
                    radius = 400f
                ),
                RoundedCornerShape(16.dp)
            )
    ) {
        // Modern Road Network
        ModernRoadNetwork()

        // Smart Traffic Lights
        SmartTrafficLights()

        // Dynamic Vehicles
        DynamicVehicleFlow()

        // Traffic Info Overlay
        TrafficInfoOverlay()
    }
}

@Composable
private fun BoxScope.ModernRoadNetwork() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val roadWidth = 80.dp.toPx()
        val laneWidth = roadWidth / 3

        // Broadway (horizontal) - main thoroughfare
        drawRoundRect(
            color = Color(0xFF1E293B),
            topLeft = Offset(0f, centerY - roadWidth / 2),
            size = Size(size.width, roadWidth),
            cornerRadius = CornerRadius(8.dp.toPx())
        )

        // Lane dividers for Broadway
        for (i in 1..2) {
            drawLine(
                color = Color(0xFF374151),
                start = Offset(0f, centerY - roadWidth / 2 + i * laneWidth),
                end = Offset(size.width, centerY - roadWidth / 2 + i * laneWidth),
                strokeWidth = 2.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
            )
        }

        // 42nd Street (vertical)
        drawRoundRect(
            color = Color(0xFF1E293B),
            topLeft = Offset(centerX - roadWidth / 2, 0f),
            size = Size(roadWidth, size.height),
            cornerRadius = CornerRadius(8.dp.toPx())
        )

        // Lane dividers for 42nd Street
        for (i in 1..2) {
            drawLine(
                color = Color(0xFF374151),
                start = Offset(centerX - roadWidth / 2 + i * laneWidth, 0f),
                end = Offset(centerX - roadWidth / 2 + i * laneWidth, size.height),
                strokeWidth = 2.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
            )
        }

        // Intersection center with crosswalk patterns
        drawRoundRect(
            color = Color(0xFF374151),
            topLeft = Offset(centerX - roadWidth / 2, centerY - roadWidth / 2),
            size = Size(roadWidth, roadWidth),
            cornerRadius = CornerRadius(8.dp.toPx())
        )

        // Crosswalk stripes
        for (i in 0..6) {
            val stripeWidth = 4.dp.toPx()
            val spacing = 8.dp.toPx()

            // Horizontal crosswalks
            drawRect(
                color = Color.White.copy(alpha = 0.6f),
                topLeft = Offset(centerX - roadWidth / 2 + i * spacing, centerY - roadWidth / 2),
                size = Size(stripeWidth, roadWidth)
            )

            // Vertical crosswalks
            drawRect(
                color = Color.White.copy(alpha = 0.6f),
                topLeft = Offset(centerX - roadWidth / 2, centerY - roadWidth / 2 + i * spacing),
                size = Size(roadWidth, stripeWidth)
            )
        }
    }

    // Street name labels with modern styling
    Card(
        modifier = Modifier
            .align(Alignment.CenterStart)
            .padding(start = 20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.8f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            "BROADWAY",
            color = Color(0xFF00D4FF),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(8.dp, 4.dp)
        )
    }

    Card(
        modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(top = 20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.8f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            "42ND ST",
            color = Color(0xFF00D4FF),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(8.dp, 4.dp)
        )
    }
}

@Composable
private fun SmartTrafficLights() {
    var currentPhase by remember { mutableStateOf(TrafficPhase.NS_GREEN) }
    var timeRemaining by remember { mutableStateOf(45) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            timeRemaining--
            if (timeRemaining <= 0) {
                currentPhase = when (currentPhase) {
                    TrafficPhase.NS_GREEN -> {
                        timeRemaining = 4
                        TrafficPhase.NS_YELLOW
                    }
                    TrafficPhase.NS_YELLOW -> {
                        timeRemaining = 35
                        TrafficPhase.EW_GREEN
                    }
                    TrafficPhase.EW_GREEN -> {
                        timeRemaining = 4
                        TrafficPhase.EW_YELLOW
                    }
                    TrafficPhase.EW_YELLOW -> {
                        timeRemaining = 45
                        TrafficPhase.NS_GREEN
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Corrected signal positions at intersection corners

        // North Signal (for southbound traffic from 42nd St)
        SmartSignalBox(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(x = 25.dp, y = 50.dp),
            direction = "N",
            state = when (currentPhase) {
                TrafficPhase.NS_GREEN -> TrafficLightState.GREEN
                TrafficPhase.NS_YELLOW -> TrafficLightState.YELLOW
                else -> TrafficLightState.RED
            },
            timeRemaining = if (currentPhase == TrafficPhase.NS_GREEN || currentPhase == TrafficPhase.NS_YELLOW) timeRemaining else 0
        )

        // East Signal (for westbound traffic from Broadway)
        SmartSignalBox(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = (-50).dp, y = 25.dp),
            direction = "E",
            state = when (currentPhase) {
                TrafficPhase.EW_GREEN -> TrafficLightState.GREEN
                TrafficPhase.EW_YELLOW -> TrafficLightState.YELLOW
                else -> TrafficLightState.RED
            },
            timeRemaining = if (currentPhase == TrafficPhase.EW_GREEN || currentPhase == TrafficPhase.EW_YELLOW) timeRemaining else 0
        )

        // South Signal (for northbound traffic from 42nd St)
        SmartSignalBox(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(x = (-25).dp, y = (-50).dp),
            direction = "S",
            state = when (currentPhase) {
                TrafficPhase.NS_GREEN -> TrafficLightState.GREEN
                TrafficPhase.NS_YELLOW -> TrafficLightState.YELLOW
                else -> TrafficLightState.RED
            },
            timeRemaining = if (currentPhase == TrafficPhase.NS_GREEN || currentPhase == TrafficPhase.NS_YELLOW) timeRemaining else 0
        )

        // West Signal (for eastbound traffic from Broadway)
        SmartSignalBox(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = 50.dp, y = (-25).dp),
            direction = "W",
            state = when (currentPhase) {
                TrafficPhase.EW_GREEN -> TrafficLightState.GREEN
                TrafficPhase.EW_YELLOW -> TrafficLightState.YELLOW
                else -> TrafficLightState.RED
            },
            timeRemaining = if (currentPhase == TrafficPhase.EW_GREEN || currentPhase == TrafficPhase.EW_YELLOW) timeRemaining else 0
        )
    }
}

@Composable
private fun SmartSignalBox(
    modifier: Modifier = Modifier,
    direction: String,
    state: TrafficLightState,
    timeRemaining: Int
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.9f)
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            1.dp,
            Brush.linearGradient(
                colors = listOf(
                    state.color.copy(alpha = 0.6f),
                    state.color.copy(alpha = 0.2f)
                )
            )
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                direction,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Modern traffic light with glow effect
            Box(
                modifier = Modifier.size(20.dp),
                contentAlignment = Alignment.Center
            ) {
                // Outer glow
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    state.color.copy(alpha = 0.4f),
                                    Color.Transparent
                                ),
                                radius = 30f
                            ),
                            CircleShape
                        )
                )

                // Inner light
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    state.color,
                                    state.color.copy(alpha = 0.7f)
                                )
                            ),
                            CircleShape
                        )
                )
            }

            if (timeRemaining > 0) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "$timeRemaining",
                    color = state.color,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun DynamicVehicleFlow() {
    var vehicles by remember {
        mutableStateOf(
            generateRandomVehicles()
        )
    }

    LaunchedEffect(Unit) {
        while (true) {
            vehicles = vehicles.map { vehicle ->
                updateVehiclePosition(vehicle)
            }.filter { it.position < 1.2f } +
                    if (Random.nextFloat() < 0.08f) {
                        listOf(generateRandomVehicle())
                    } else emptyList()

            delay(100)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        vehicles.forEach { vehicle ->
            val (x, y) = getVehicleScreenPosition(vehicle)

            ModernVehicle(
                modifier = Modifier.offset(x.dp, y.dp),
                vehicle = vehicle
            )
        }
    }
}

data class Vehicle(
    val id: Int,
    val lane: VehicleLane,
    val type: VehicleType,
    val position: Float,
    val speed: Float,
    val color: Color
)

enum class VehicleLane {
    BROADWAY_EAST_1, BROADWAY_EAST_2, BROADWAY_EAST_3,  // Left to right movement
    BROADWAY_WEST_1, BROADWAY_WEST_2, BROADWAY_WEST_3,  // Right to left movement
    STREET_42_SOUTH_1, STREET_42_SOUTH_2, STREET_42_SOUTH_3,  // Top to bottom movement
    STREET_42_NORTH_1, STREET_42_NORTH_2, STREET_42_NORTH_3   // Bottom to top movement
}

enum class VehicleType(val color: Color, val icon: ImageVector) {
    CAR(Color(0xFF3B82F6), Icons.Default.DirectionsCar),
    TAXI(Color(0xFFFBBF24), Icons.Default.LocalTaxi),
    BUS(Color(0xFF059669), Icons.Default.DirectionsBus),
    TRUCK(Color(0xFFDC2626), Icons.Default.LocalShipping),
    BIKE(Color(0xFF8B5CF6), Icons.Default.DirectionsBike)
}

private fun generateRandomVehicles(): List<Vehicle> {
    return (1..10).map { generateRandomVehicle() }
}

private fun generateRandomVehicle(): Vehicle {
    val lanes = VehicleLane.values()
    val types = VehicleType.values()
    val selectedType = types.random()

    return Vehicle(
        id = Random.nextInt(),
        lane = lanes.random(),
        type = selectedType,
        position = Random.nextFloat() * 0.2f,
        speed = Random.nextFloat() * 0.015f + 0.008f,
        color = selectedType.color
    )
}

private fun updateVehiclePosition(vehicle: Vehicle): Vehicle {
    return vehicle.copy(position = vehicle.position + vehicle.speed)
}

private fun getVehicleScreenPosition(vehicle: Vehicle): Pair<Float, Float> {
    val progress = vehicle.position
    val roadCenter = 140f // Center of the intersection area
    val laneOffset = 27f // Distance between lanes

    return when (vehicle.lane) {
        // Broadway Eastbound (Left to Right)
        VehicleLane.BROADWAY_EAST_1 -> Pair(-30f + progress * 400f, roadCenter - laneOffset)
        VehicleLane.BROADWAY_EAST_2 -> Pair(-30f + progress * 400f, roadCenter)
        VehicleLane.BROADWAY_EAST_3 -> Pair(-30f + progress * 400f, roadCenter + laneOffset)

        // Broadway Westbound (Right to Left)
        VehicleLane.BROADWAY_WEST_1 -> Pair(370f - progress * 400f, roadCenter + laneOffset + 15f)
        VehicleLane.BROADWAY_WEST_2 -> Pair(370f - progress * 400f, roadCenter + 15f)
        VehicleLane.BROADWAY_WEST_3 -> Pair(370f - progress * 400f, roadCenter - laneOffset + 15f)

        // 42nd Street Southbound (Top to Bottom)
        VehicleLane.STREET_42_SOUTH_1 -> Pair(roadCenter - laneOffset, -30f + progress * 340f)
        VehicleLane.STREET_42_SOUTH_2 -> Pair(roadCenter, -30f + progress * 340f)
        VehicleLane.STREET_42_SOUTH_3 -> Pair(roadCenter + laneOffset, -30f + progress * 340f)

        // 42nd Street Northbound (Bottom to Top)
        VehicleLane.STREET_42_NORTH_1 -> Pair(roadCenter + laneOffset + 15f, 310f - progress * 340f)
        VehicleLane.STREET_42_NORTH_2 -> Pair(roadCenter + 15f, 310f - progress * 340f)
        VehicleLane.STREET_42_NORTH_3 -> Pair(roadCenter - laneOffset + 15f, 310f - progress * 340f)
    }
}

@Composable
private fun ModernVehicle(
    modifier: Modifier = Modifier,
    vehicle: Vehicle
) {
    Box(modifier = modifier) {
        // Vehicle shadow/glow
        Box(
            modifier = Modifier
                .size(14.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            vehicle.color.copy(alpha = 0.5f),
                            Color.Transparent
                        ),
                        radius = 25f
                    ),
                    RoundedCornerShape(3.dp)
                )
        )

        // Vehicle body
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(10.dp, 6.dp)
                .background(
                    vehicle.color,
                    RoundedCornerShape(2.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                vehicle.type.icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(5.dp)
            )
        }

        // Direction indicator based on lane
        val directionColor = when (vehicle.lane) {
            VehicleLane.BROADWAY_EAST_1, VehicleLane.BROADWAY_EAST_2, VehicleLane.BROADWAY_EAST_3 -> Color(0xFF00FF88)
            VehicleLane.BROADWAY_WEST_1, VehicleLane.BROADWAY_WEST_2, VehicleLane.BROADWAY_WEST_3 -> Color(0xFFFF6B6B)
            VehicleLane.STREET_42_SOUTH_1, VehicleLane.STREET_42_SOUTH_2, VehicleLane.STREET_42_SOUTH_3 -> Color(0xFF4ECDC4)
            VehicleLane.STREET_42_NORTH_1, VehicleLane.STREET_42_NORTH_2, VehicleLane.STREET_42_NORTH_3 -> Color(0xFFFFE66D)
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(3.dp)
                .background(directionColor, CircleShape)
        )
    }
}

@Composable
private fun BoxScope.TrafficInfoOverlay() {
    // Current phase indicator in center
    Card(
        modifier = Modifier
            .align(Alignment.Center)
            .size(60.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.85f)
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFF3B82F6).copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.Traffic,
                contentDescription = null,
                tint = Color(0xFF10B981),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                "N-S",
                color = Color(0xFF10B981),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "ACTIVE",
                color = Color.White,
                fontSize = 8.sp
            )
        }
    }
}

@Composable
private fun StatsView() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            StatCard(
                title = "Peak Hour Analysis",
                value = "8:00-9:00 AM",
                detail = "Highest traffic volume: 2,847 vehicles/hour",
                color = Color(0xFFEF4444)
            )
        }
        item {
            StatCard(
                title = "Average Wait Time",
                value = "3.2 minutes",
                detail = "Down 15% from last week",
                color = Color(0xFF10B981)
            )
        }
        item {
            StatCard(
                title = "Traffic Efficiency",
                value = "87%",
                detail = "Optimal signal timing achieved",
                color = Color(0xFF3B82F6)
            )
        }
    }
}

@Composable
private fun HistoryView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            "Recent Events",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(5) { index ->
                HistoryItem(
                    time = "09:${18-index}:${36-index*6}",
                    event = when (index) {
                        0 -> "Signal optimization applied"
                        1 -> "Heavy traffic detected on Broadway"
                        2 -> "Emergency vehicle priority activated"
                        3 -> "Pedestrian crossing extended"
                        else -> "Normal traffic flow resumed"
                    },
                    type = when (index) {
                        0 -> "SYSTEM"
                        1 -> "ALERT"
                        2 -> "PRIORITY"
                        3 -> "PEDESTRIAN"
                        else -> "INFO"
                    }
                )
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    detail: String,
    color: Color
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                title,
                color = color,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                value,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                detail,
                color = Color(0xFF9CA3AF),
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun HistoryItem(
    time: String,
    event: String,
    type: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFF1F2937).copy(alpha = 0.5f),
                RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Badge(
            containerColor = when (type) {
                "ALERT" -> Color(0xFFEF4444).copy(alpha = 0.2f)
                "PRIORITY" -> Color(0xFFF59E0B).copy(alpha = 0.2f)
                "SYSTEM" -> Color(0xFF3B82F6).copy(alpha = 0.2f)
                else -> Color(0xFF6B7280).copy(alpha = 0.2f)
            },
            contentColor = when (type) {
                "ALERT" -> Color(0xFFEF4444)
                "PRIORITY" -> Color(0xFFF59E0B)
                "SYSTEM" -> Color(0xFF3B82F6)
                else -> Color(0xFF9CA3AF)
            }
        ) {
            Text(
                type,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                event,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                time,
                color = Color(0xFF6B7280),
                fontSize = 9.sp
            )
        }
    }
}

@Composable
private fun QuickMetrics() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        QuickMetric(
            modifier = Modifier.weight(1f),
            label = "VEHICLES",
            value = "1,247",
            trend = "+12%",
            color = Color(0xFF00D4FF)
        )

        QuickMetric(
            modifier = Modifier.weight(1f),
            label = "WAIT TIME",
            value = "3.2m",
            trend = "-8%",
            color = Color(0xFF10B981)
        )

        QuickMetric(
            modifier = Modifier.weight(1f),
            label = "EFFICIENCY",
            value = "87%",
            trend = "+5%",
            color = Color(0xFFFBBF24)
        )
    }
}

@Composable
private fun QuickMetric(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    trend: String,
    color: Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1F2937).copy(alpha = 0.6f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                label,
                color = Color(0xFF9CA3AF),
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                value,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                trend,
                color = color,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

enum class TrafficPhase {
    NS_GREEN, NS_YELLOW, EW_GREEN, EW_YELLOW
}

enum class TrafficLightState(val color: Color) {
    RED(Color(0xFFEF4444)),
    YELLOW(Color(0xFFF59E0B)),
    GREEN(Color(0xFF10B981))
}