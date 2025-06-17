package com.gub.features.dashboard.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gub.core.ui.components.UserProfileChip
import dev.chrisbanes.haze.HazeInputScale
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect

@Composable
fun TopBar(hazeState: HazeState, selectedTab: Int, onSelect: (Int) -> Unit, topBarHeight: (Int) -> Unit) {

    var showQuickActions by remember { mutableStateOf(false) }
    var selectedTimeFrame by remember { mutableStateOf(0) }

    val currentTime = "2025-06-17 16:28:43 UTC"
    val dashboardTabs = listOf("Overview", "Live Status", "Quick Actions", "Insights")
    val timeFrames = listOf("Real-time", "Last Hour", "Last 6H", "Today")

    Column(
        modifier = Modifier.hazeEffect(state = hazeState) {
            inputScale = HazeInputScale.Fixed(0.5F)
            progressive = HazeProgressive.verticalGradient(
                startIntensity = 1f,
                endIntensity = 0.25f,
                preferPerformance = true
            )
        }.background(
           color = MaterialTheme.colorScheme.background.copy(0.5F)
        ).onGloballyPositioned { coordinates ->
            topBarHeight(coordinates.size.height)
        }.padding(24.dp)
    ) {
        // Dashboard Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Traffic Control Dashboard",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

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
                    onClick = { onSelect(index) },
                    label = {
                        Text(
                            tab,
                            fontSize = 12.sp,
                            color = if (selectedTab == index) Color.White else Color.Gray
                        )
                            },
                    selected = selectedTab == index,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF4CAF50),
                        containerColor = Color(0xFF161B22)
                    )
                )
            }
        }
    }
}