package com.gub.features.dashboard.presentation.overview.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gub.core.ui.components.PulsingDot

@Composable
fun RecentAlertsCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
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
                    "Recent Alerts",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "3 Active",
                    color = Color(0xFFFF9800),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AlertItem(
                title = "High Traffic Volume",
                location = "Main St & 5th Ave",
                time = "2 min ago",
                severity = "Medium",
                isActive = true
            )

            AlertItem(
                title = "Signal Malfunction",
                location = "Oak St & 3rd Ave",
                time = "8 min ago",
                severity = "High",
                isActive = true
            )

            AlertItem(
                title = "Congestion Detected",
                location = "Highway 101 Corridor",
                time = "15 min ago",
                severity = "Low",
                isActive = false
            )

            AlertItem(
                title = "AI Optimization Complete",
                location = "Downtown District",
                time = "23 min ago",
                severity = "Info",
                isActive = false
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2196F3)),
                border = BorderStroke(1.dp, Color(0xFF2196F3).copy(alpha = 0.5f))
            ) {
                Text("View All Alerts", fontSize = 12.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun AlertItem(title: String, location: String, time: String, severity: String, isActive: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(
                    color = when (severity) {
                        "High" -> Color(0xFFF44336)
                        "Medium" -> Color(0xFFFF9800)
                        "Low" -> Color(0xFF2196F3)
                        else -> Color(0xFF4CAF50)
                    },
                    shape = CircleShape
                )
        )

        if (isActive) {
            Spacer(modifier = Modifier.width(4.dp))
            PulsingDot()
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                color = if (isActive) Color.White else Color.Gray,
                fontSize = 11.sp,
                fontWeight = if (isActive) FontWeight.Medium else FontWeight.Normal
            )
            Text(
                location,
                color = Color.Gray,
                fontSize = 9.sp
            )
        }

        Text(
            time,
            color = Color.Gray,
            fontSize = 9.sp
        )
    }
}