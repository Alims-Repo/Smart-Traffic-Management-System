package com.gub.app

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import com.gub.core.ui.components.NavigationSidebar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gub.core.ui.model.Navigation
import com.gub.features.analytics.presentation.Analytics
import com.gub.features.dashboard.presentation.Dashboard
import com.gub.features.monitoring.presentation.Monitoring
import com.gub.features.settings.presentation.Settings

@Composable
fun TrafficManagementApp() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background
            )
    ) {
        var aiEnabled  by remember { mutableStateOf(true) }
        var selectedTab by remember { mutableStateOf(Navigation.DASHBOARD) }

        Row(modifier = Modifier.fillMaxSize()) {
            NavigationSidebar(
                selectedTab = selectedTab,
                isAiEnabled = aiEnabled,
                onTabSelected = { selectedTab = it },
                onAiToggle = { aiEnabled = it }
            )

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

                AnimatedContent(
                    targetState = selectedTab,
                    transitionSpec = {
                        // Slide left/right based on ordinal comparison
                        val direction = targetState.ordinal - initialState.ordinal
                        val slideIn = if (direction > 0) slideInHorizontally(
                            animationSpec = tween(300),
                            initialOffsetX = { fullWidth -> fullWidth }
                        ) else slideInHorizontally(
                            animationSpec = tween(300),
                            initialOffsetX = { fullWidth -> -fullWidth }
                        )

                        val slideOut = if (direction > 0) slideOutHorizontally(
                            animationSpec = tween(300),
                            targetOffsetX = { fullWidth -> -fullWidth }
                        ) else slideOutHorizontally(
                            animationSpec = tween(300),
                            targetOffsetX = { fullWidth -> fullWidth }
                        )

                        slideIn + fadeIn(animationSpec = tween(300)) togetherWith
                        slideOut + fadeOut(animationSpec = tween(300))
                    },
                    label = "TabPageTransition"
                ) {
                    when(it) {
                        Navigation.DASHBOARD -> Dashboard()
                        Navigation.MONITORING -> Monitoring()
                        Navigation.ANALYTICS -> Analytics()
                        Navigation.SETTINGS -> Settings()
                    }
                }
            }
        }
    }
}

@Composable
fun TitleBar(selectedTab: Navigation) {
    AnimatedContent(
        targetState = selectedTab,
        transitionSpec = {
            slideInVertically(
                animationSpec = tween(300),
                initialOffsetY = { fullHeight -> fullHeight }
            ) togetherWith slideOutVertically(
                animationSpec = tween(300),
                targetOffsetY = { fullHeight -> -fullHeight }
            )
        },
        label = "TitleTransition"
    ) {
        Text(
            text = it.title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}