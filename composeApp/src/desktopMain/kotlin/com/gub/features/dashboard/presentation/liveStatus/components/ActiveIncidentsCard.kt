package com.gub.features.dashboard.presentation.liveStatus.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ActiveIncidentsCard(modifier: Modifier = Modifier) {
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
                    "Active Incidents",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFF9800).copy(alpha = 0.2f)
                    )
                ) {
                    Text(
                        "2 ACTIVE",
                        color = Color(0xFFFF9800),
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            IncidentItem(
                type = "Signal Malfunction",
                location = "Oak St & 3rd Ave",
                duration = "8 min",
                severity = "High",
                status = "Technician Dispatched"
            )

            Spacer(modifier = Modifier.height(8.dp))

            IncidentItem(
                type = "Heavy Traffic",
                location = "Main St Corridor",
                duration = "15 min",
                severity = "Medium",
                status = "AI Rerouting Active"
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1117))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "Recent Resolution",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                    Text(
                        "Congestion at Highway 101 - Cleared 23 min ago",
                        color = Color(0xFF4CAF50),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF9800)),
                border = BorderStroke(1.dp, Color(0xFFFF9800).copy(alpha = 0.5f))
            ) {
                Text("Incident Management", fontSize = 11.sp)
            }
        }
    }
}