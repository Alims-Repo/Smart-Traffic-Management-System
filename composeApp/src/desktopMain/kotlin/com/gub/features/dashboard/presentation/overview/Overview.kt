package com.gub.features.dashboard.presentation.overview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gub.features.dashboard.presentation.LiveTrafficMetricsCard
import com.gub.features.dashboard.presentation.overview.components.AIStatusCard
import com.gub.features.dashboard.presentation.overview.components.RecentAlertsCard
import com.gub.features.dashboard.presentation.overview.components.TopPerformingIntersectionsCard
import com.gub.features.dashboard.presentation.overview.components.SystemOverviewCard
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource

@Composable
fun Overview(hazeState: HazeState, top: Dp) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .hazeSource(hazeState),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = top + 24.dp, start = 24.dp, end = 24.dp, bottom = 24.dp)
    ) {
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
    }
}