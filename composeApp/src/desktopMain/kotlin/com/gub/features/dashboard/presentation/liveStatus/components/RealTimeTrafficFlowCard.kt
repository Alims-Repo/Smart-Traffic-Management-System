package com.gub.features.dashboard.presentation.liveStatus.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RealTimeTrafficFlowCard(modifier: Modifier = Modifier) {
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
                    "Real-Time Traffic Flow",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B5E20))
                ) {
                    Text(
                        text = "LIVE DATA",
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Simulated real-time chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color(0xFF0D1117), RoundedCornerShape(8.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    repeat(12) { index ->
                        val height = when (index) {
                            in 7..9 -> (120..150).random() // Current high traffic
                            in 4..6 -> (80..110).random()
                            else -> (40..80).random()
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(8.dp)
                                    .height(height.dp)
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color(0xFF4CAF50),
                                                Color(0xFF2E7D32)
                                            )
                                        ),
                                        shape = RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp)
                                    )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "${(index + 9) % 24}:00",
                                color = Color.Gray,
                                fontSize = 8.sp
                            )
                        }
                    }
                }

                // Current time indicator
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF21262D))
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Current",
                                color = Color.Gray,
                                fontSize = 9.sp
                            )
                            Text(
                                "2,847 veh/hr",
                                color = Color(0xFF4CAF50),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Peak: 18:15 (3,142 vehicles/hr)", color = Color.Gray, fontSize = 10.sp)
                Text("Efficiency: 95.3%", color = Color(0xFF4CAF50), fontSize = 10.sp)
            }
        }
    }
}