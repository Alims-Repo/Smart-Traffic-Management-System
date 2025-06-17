package com.gub.features.dashboard.presentation.overview.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AIStatusCard(modifier: Modifier = Modifier) {
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Psychology,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "AI Control System",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B5E20))
                ) {
                    Text(
                        "ACTIVE",
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // AI Performance Gauge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .height(160.dp),
                contentAlignment = Alignment.Center
            ) {

                Box(
                    modifier = Modifier.size(140.dp)
                ) {
                    CircularProgressIndicator(
                        progress = { 0.9F },
                        modifier = Modifier.size(140.dp),
                        strokeWidth = 8.dp,
                        trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
                    )
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "97.1%",
                            color = Color.White,
                            fontSize = 16.sp,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        Text(
                            "Efficiency",
                            color = Color.Gray,
                            fontSize = 10.sp,
                            lineHeight = 10.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            AIStatusItem("Models Running", "12/12", Color(0xFF4CAF50))
            AIStatusItem("Decision Speed", "0.28ms", Color(0xFF4CAF50))
            AIStatusItem("Learning Rate", "97.8%", Color(0xFF2196F3))

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Last optimization: 3 minutes ago",
                color = Color.Gray,
                fontSize = 10.sp,
                style = androidx.compose.ui.text.TextStyle(
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            )
        }
    }
}

@Composable
fun AIStatusItem(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color.Gray, fontSize = 10.sp)
        Text(
            value,
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}