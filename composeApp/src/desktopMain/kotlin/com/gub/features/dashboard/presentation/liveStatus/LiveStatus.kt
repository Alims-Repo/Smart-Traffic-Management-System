package com.gub.features.dashboard.presentation.liveStatus

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gub.features.dashboard.presentation.liveStatus.components.ActiveIncidentsCard
import com.gub.features.dashboard.presentation.liveStatus.components.LiveSystemStatusCard
import com.gub.features.dashboard.presentation.liveStatus.components.RealTimeTrafficFlowCard
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource

@Composable
fun LiveStatus(hazeState: HazeState, top: Dp) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .hazeSource(hazeState),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = top + 8.dp, start = 24.dp, end = 24.dp, bottom = 24.dp)
    ) {
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
    }
}