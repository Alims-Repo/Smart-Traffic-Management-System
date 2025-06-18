package com.gub.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gub.core.domain.Response
import com.gub.models.ModelSystemStatus

@Composable
fun SystemStatusCard(response : Response<ModelSystemStatus>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(0.1F)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "System Status",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            when(response) {
                Response.Loading -> {
                    CircularProgressIndicator()
                }
                is Response.Error -> {
                    Text(response.error)
                }
                is Response.Success -> {
                    StatusItem("CPU", "${response.data.cpuUsage}%", Color(0xFF4CAF50))
//                    StatusItem("GPU", "${response.data.gpu}%", Color(0xFFFF9800))

//                    StatusItem("Network", "${response.data.network}%", Color(0xFF4CAF50))
                    val memoryUsages = (response.data.usedMemoryMb * 100) / response.data.totalMemoryMb
                    StatusItem("Memory", "$memoryUsages%", Color(0xFFFF9800))
                }
            }
        }
    }
}

@Composable
fun StatusItem(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color.Gray, fontSize = 12.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, CircleShape)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(value, color = MaterialTheme.colorScheme.onSurface, fontSize = 12.sp)
        }
    }
}