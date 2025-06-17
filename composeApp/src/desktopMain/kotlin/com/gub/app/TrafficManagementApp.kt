package com.gub.app

import androidx.compose.animation.AnimatedContent
import com.gub.core.ui.components.NavigationSidebar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gub.core.ui.model.Navigation
import com.gub.features.dashboard.presentation.Dashboard

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
                    .padding(24.dp)
            ) {
                AnimatedContent(
                    targetState = selectedTab
                ) {
                    when(it) {
                        Navigation.DASHBOARD -> Dashboard()
                        Navigation.MONITORING -> TODO()
                        Navigation.ANALYTICS -> TODO()
                        Navigation.SETTINGS -> TODO()
                    }
                }
            }
        }
    }
}