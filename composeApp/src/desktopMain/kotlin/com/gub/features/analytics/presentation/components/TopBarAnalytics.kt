package com.gub.features.analytics.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gub.core.ui.components.UserProfileChip
import com.gub.features.analytics.domain.model.IntersectionFilter
import com.gub.features.analytics.domain.model.TimeRange
import com.gub.features.analytics.presentation.AutoRefreshIndicator
import com.gub.features.analytics.viewModel.ViewModelAnalytics
import dev.chrisbanes.haze.HazeInputScale
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TopBarAnalytics(
    hazeState: HazeState,
    viewModel: ViewModelAnalytics,
    topBarHeight: (Int) -> Unit
) {
    val filterOptions by viewModel.filterOptions.collectAsState()
    val currentTime = remember {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss 'UTC'", Locale.getDefault())
        dateFormat.format(Date())
    }

    Column(
        modifier = Modifier
            .hazeEffect(state = hazeState) {
                inputScale = HazeInputScale.Fixed(0.5F)
                progressive = HazeProgressive.verticalGradient(
                    startIntensity = 1f,
                    endIntensity = 0.25f,
                    preferPerformance = true
                )
            }
            .background(color = MaterialTheme.colorScheme.surface.copy(0.5F))
            .onGloballyPositioned { coordinates ->
                topBarHeight(coordinates.size.height)
            }
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Analytics Dashboard",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
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
                    AutoRefreshIndicator(viewModel = viewModel)
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = { viewModel.toggleAdvancedFilters() }
                ) {
                    Icon(
                        Icons.Default.FilterList,
                        contentDescription = "Advanced Filters",
                        tint = if (filterOptions.showAdvancedFilters) Color(0xFF4CAF50) else Color.Gray
                    )
                }

                UserProfileChip()
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TimeRange.values().forEach { timeRange ->
                    FilterChip(
                        onClick = { viewModel.updateTimeRange(timeRange) },
                        label = {
                            Text(
                                timeRange.displayName,
                                fontSize = 12.sp,
                                color = if (filterOptions.timeRange == timeRange) Color.White else Color.Gray
                            )
                        },
                        selected = filterOptions.timeRange == timeRange,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF2E7D32),
                            containerColor = Color(0xFF161B22)
                        )
                    )
                }
            }

            AnimatedVisibility(visible = filterOptions.showAdvancedFilters) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IntersectionFilter.values().forEach { filter ->
                        FilterChip(
                            onClick = { viewModel.updateIntersectionFilter(filter) },
                            label = {
                                Text(
                                    filter.displayName,
                                    fontSize = 10.sp,
                                    color = if (filterOptions.intersectionFilter == filter) Color.White else Color.Gray
                                )
                            },
                            selected = filterOptions.intersectionFilter == filter,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF1976D2),
                                containerColor = Color(0xFF161B22)
                            )
                        )
                    }
                }
            }
        }
    }
}