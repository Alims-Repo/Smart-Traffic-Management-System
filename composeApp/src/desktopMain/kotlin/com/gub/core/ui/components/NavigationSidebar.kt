package com.gub.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gub.core.ui.model.NavItem
import com.gub.core.ui.model.Navigation

@Composable
fun NavigationSidebar(
    selectedTab: Navigation,
    onTabSelected: (Navigation) -> Unit,
    isAiEnabled: Boolean,
    onAiToggle: (Boolean) -> Unit
) {
    val navItems = listOf(
        NavItem("Dashboard", Icons.Default.Dashboard, Navigation.DASHBOARD),
        NavItem("Monitoring", Icons.Default.Traffic, Navigation.MONITORING),
        NavItem("Analytics", Icons.Default.Analytics, Navigation.ANALYTICS),
        NavItem("Settings", Icons.Default.Settings, Navigation.SETTINGS)
    )

    Column(
        modifier = Modifier
            .width(260.dp)
            .fillMaxHeight()
            .background(Color(0xFF21262D))
            .padding(16.dp)
    ) {
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFF4CAF50), Color(0xFF2E7D32))
                        ),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    Icons.Default.Traffic,
                    contentDescription = null,
                    tint = Color.White,
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
                color = Color.White
            )
        }

        // AI Status Toggle
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isAiEnabled) Color(0xFF1B5E20) else Color(0xFF424242)
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
                        Color(0xFF4CAF50)
                    else Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "AI Control",
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = isAiEnabled,
                    onCheckedChange = onAiToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF4CAF50),
                        checkedTrackColor = Color(0xFF1B5E20)
                    )
                )
            }
        }

        // Navigation Items
        navItems.forEach { item ->
            NavigationItem(
                item = item,
                selected = selectedTab == item.index,
                onClick = { onTabSelected(item.index) }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // System Status
        SystemStatusCard()
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
            containerColor = if (selected) Color(0xFF2E7D32) else Color.Transparent
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
                tint = if (selected) Color.White else Color.Gray
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                item.title,
                color = if (selected) Color.White else Color.Gray,
                fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
            )
        }
    }
}