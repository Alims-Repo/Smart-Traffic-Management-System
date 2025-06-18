package com.gub.features.dashboard.presentation.liveStatus.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gub.core.ui.components.PulsingDot

@Composable
fun LiveSystemStatusCard() {
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
                    "Live System Status",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    PulsingDot()
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "All Systems Operational",
                        color = Color(0xFF4CAF50),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                LiveStatusIndicator("Traffic Signals", "247/250", Color(0xFF4CAF50), "98.8%")
                LiveStatusIndicator("AI Systems", "Online", Color(0xFF4CAF50), "100%")
                LiveStatusIndicator("Data Streams", "Active", Color(0xFF4CAF50), "99.2%")
                LiveStatusIndicator("Network", "Stable", Color(0xFF4CAF50), "Low Latency")
                LiveStatusIndicator("Storage", "Available", Color(0xFF2196F3), "73% Free")
            }
        }
    }
}