package com.gub.features.dashboard.presentation.overview.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gub.features.dashboard.domain.model.IntersectionPerformance

@Composable
fun TopPerformingIntersectionsCard(modifier: Modifier = Modifier) {
    val intersections = listOf(
        IntersectionPerformance("Main St & 5th Ave", 98.7f, "+2.3%"),
        IntersectionPerformance("Park Rd & Oak St", 94.8f, "+1.1%"),
        IntersectionPerformance("1st Ave & Broadway", 92.4f, "+0.8%"),
        IntersectionPerformance("Highway 101 & Center", 90.1f, "-0.5%")
    )

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
                    "Top Performing Intersections",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            intersections.forEachIndexed { index, intersection ->
                TopIntersectionItem(intersection, index + 1)
                if (index < intersections.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF4CAF50)),
                border = BorderStroke(1.dp, Color(0xFF4CAF50).copy(alpha = 0.5f))
            ) {
                Text("View Full Rankings", fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun TopIntersectionItem(intersection: IntersectionPerformance, rank: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(
                    when (rank) {
                        1 -> Color(0xFFFFD700)
                        2 -> Color(0xFFC0C0C0)
                        3 -> Color(0xFFCD7F32)
                        else -> Color(0xFF424242)
                    },
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                rank.toString(),
                color = if (rank <= 3) Color.Black else Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                intersection.name,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                "Efficiency: ${String.format("%.1f", intersection.efficiency)}%",
                color = Color.Gray,
                fontSize = 9.sp
            )
        }

        Text(
            intersection.change,
            color = if (intersection.change.startsWith("+")) Color(0xFF4CAF50) else Color(0xFFF44336),
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}