package com.gub.features.dashboard.presentation.screens.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SystemOverviewCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "System Overview",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Dashboard,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "16:28:43",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SystemOverviewMetric(
                    title = "System Health",
                    value = "98.7%",
                    subtitle = "Excellent",
                    color = Color(0xFF4CAF50),
                    icon = Icons.Default.CheckCircle
                )

                SystemOverviewMetric(
                    title = "Active Intersections",
                    value = "247/250",
                    subtitle = "99.2% Online",
                    color = Color(0xFF4CAF50),
                    icon = Icons.Default.Traffic
                )

                SystemOverviewMetric(
                    title = "AI Performance",
                    value = "97.1%",
                    subtitle = "Optimal",
                    color = Color(0xFF4CAF50),
                    icon = Icons.Default.Psychology
                )

                SystemOverviewMetric(
                    title = "Current Flow",
                    value = "2,847",
                    subtitle = "vehicles/hr",
                    color = Color(0xFF2196F3),
                    icon = Icons.Default.DirectionsCar
                )

                SystemOverviewMetric(
                    title = "Avg Wait Time",
                    value = "31.2s",
                    subtitle = "↓ 18% today",
                    color = Color(0xFF4CAF50),
                    icon = Icons.Default.Timer
                )
            }
        }
    }
}
