package com.gub.features.monitoring.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gub.core.ui.components.PulsingDot
import dev.chrisbanes.haze.HazeInputScale
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect

@Composable
fun TopBarMonitoring(hazeState: HazeState, topBarHeight: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().hazeEffect(state = hazeState) {
            inputScale = HazeInputScale.Fixed(0.5F)
            progressive = HazeProgressive.verticalGradient(
                startIntensity = 1f,
                endIntensity = 0.25f,
                preferPerformance = true
            )
        }.background(
            color = MaterialTheme.colorScheme.surface.copy(0.5F)
        ).onGloballyPositioned { coordinates ->
            topBarHeight(coordinates.size.height)
        }.padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Traffic Monitoring",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            PulsingDot()
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Live",
                color = Color(0xFF4CAF50),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}