package com.gub.features.dashboard.presentation.quickAction

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource

@Composable
fun QuickActionsGrid(hazeState: HazeState, top: Dp) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .hazeSource(hazeState)
            .verticalScroll(rememberScrollState())
            .padding(
                top = top + 8.dp, start = 24.dp, end = 24.dp, bottom = 24.dp
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Quick Actions",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        // Emergency Actions
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    "Emergency Controls",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionButton(
                        title = "Emergency Mode",
                        icon = Icons.Default.Warning,
                        color = Color(0xFFF44336),
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButton(
                        title = "All Red Signals",
                        icon = Icons.Default.StopCircle,
                        color = Color(0xFFF44336),
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButton(
                        title = "Priority Route",
                        icon = Icons.Default.LocalHospital,
                        color = Color(0xFFFF9800),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // System Controls
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    "System Controls",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionButton(
                        title = "AI Optimization",
                        icon = Icons.Default.Psychology,
                        color = Color(0xFF4CAF50),
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButton(
                        title = "Manual Override",
                        icon = Icons.Default.Settings,
                        color = Color(0xFF2196F3),
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButton(
                        title = "System Reset",
                        icon = Icons.Default.RestartAlt,
                        color = Color(0xFFFF9800),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Reports & Analytics
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    "Reports & Analytics",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionButton(
                        title = "Generate Report",
                        icon = Icons.Default.Assessment,
                        color = Color(0xFF9C27B0),
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButton(
                        title = "Export Data",
                        icon = Icons.Default.FileDownload,
                        color = Color(0xFF607D8B),
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButton(
                        title = "Schedule Analysis",
                        icon = Icons.Default.Schedule,
                        color = Color(0xFF795548),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}