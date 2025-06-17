package com.gub.features.dashboard.presentation.liveStatus.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IncidentItem(type: String, location: String, duration: String, severity: String, status: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(
                    when (severity) {
                        "High" -> Color(0xFFF44336)
                        "Medium" -> Color(0xFFFF9800)
                        else -> Color(0xFF2196F3)
                    },
                    CircleShape
                )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                type,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                location,
                color = Color.Gray,
                fontSize = 9.sp
            )
            Text(
                status,
                color = Color(0xFF2196F3),
                fontSize = 9.sp
            )
        }

        Text(
            duration,
            color = Color.Gray,
            fontSize = 9.sp
        )
    }
}