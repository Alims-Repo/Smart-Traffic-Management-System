package com.gub.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gub.app.ViewModelSystem
import com.gub.core.ui.model.NavItem
import com.gub.core.ui.model.Navigation

@Composable
fun NavigationSidebar(
    selectedTab: Navigation,
    onTabSelected: (Navigation) -> Unit,
    isAiEnabled: Boolean,
    onAiToggle: (Boolean) -> Unit,
    viewModel: ViewModelSystem
) {
    val navItems = listOf(
        NavItem("Dashboard", Icons.Default.Dashboard, Navigation.DASHBOARD),
        NavItem("Monitoring", Icons.Default.Traffic, Navigation.MONITORING),
        NavItem("Analytics", Icons.Default.Analytics, Navigation.ANALYTICS),
        NavItem("Settings", Icons.Default.Settings, Navigation.SETTINGS)
    )

    val systemStatus = viewModel.systemStatus.collectAsState()

    Column(
        modifier = Modifier
            .width(260.dp)
            .fillMaxHeight()
            .background(
                color = MaterialTheme.colorScheme.surface
            ).padding(16.dp)
    ) {
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    Icons.Default.Traffic,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
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
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // AI Status Toggle
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isAiEnabled)
                    MaterialTheme.colorScheme.primary.copy(0.5F)
                else MaterialTheme.colorScheme.primary.copy(0.1F)
            )
        ) {
            Row(
                modifier = Modifier.padding(
                    vertical = 8.dp,
                    horizontal = 16.dp
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Psychology,
                    contentDescription = null,
                    tint = if (isAiEnabled)
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.primary.copy(0.5F)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "AI Control",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = isAiEnabled,
                    onCheckedChange = onAiToggle
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
                .weight(1F)
        ) {
            items(navItems) { item->
                NavigationItem(
                    item = item,
                    selected = selectedTab == item.index,
                    onClick = { onTabSelected(item.index) }
                )
            }
        }

        SystemStatusCard(systemStatus.value)
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
            containerColor = if (selected)
                MaterialTheme.colorScheme.primary
            else Color.Transparent
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
                tint = if (selected) MaterialTheme.colorScheme.onPrimary else Color.Gray
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                item.title,
                color = if (selected) MaterialTheme.colorScheme.onPrimary else Color.Gray,
                fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
            )
        }
    }
}