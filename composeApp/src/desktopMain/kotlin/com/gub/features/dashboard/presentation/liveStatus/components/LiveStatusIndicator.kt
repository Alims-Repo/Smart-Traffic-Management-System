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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LiveStatusIndicator(title: String, status: String, color: Color, detail: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            title,
            color = Color.Gray,
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
        Text(
            status,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            detail,
            color = color,
            fontSize = 9.sp,
            textAlign = TextAlign.Center
        )
    }
}