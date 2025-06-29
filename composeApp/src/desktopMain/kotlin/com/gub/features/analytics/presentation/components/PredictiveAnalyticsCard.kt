package com.gub.features.analytics.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gub.core.domain.Response
import com.gub.core.ui.components.PulsingDot
import com.gub.features.analytics.viewModel.ViewModelAnalytics
import com.gub.models.analytics.ModelPrediction

@Composable
fun PredictiveAnalyticsCard(modifier: Modifier = Modifier, viewModel: ViewModelAnalytics) {
    val predictiveAnalytics by viewModel.predictiveAnalytics.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    Card(
        modifier = modifier.height(380.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(20.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = Color(0xFF2196F3),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Predictive Analytics",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                AnimatedVisibility(
                    visible = isRefreshing,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = Color(0xFF2196F3)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "Updating...",
                            color = Color(0xFF2196F3),
                            fontSize = 10.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when(val response = predictiveAnalytics) {
                is Response.Error -> {
                    ErrorState(
                        error = response.error,
                        onRetry = { viewModel.refreshData() }
                    )
                }
                Response.Loading -> {
                    LoadingState()
                }
                is Response.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(response.data) { prediction ->
                            PredictionItem(prediction = prediction)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorState(error: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Failed to load predictions",
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = error,
                color = Color.Gray,
                fontSize = 12.sp
            )
            IconButton(onClick = onRetry) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = "Retry",
                    tint = Color(0xFF2196F3)
                )
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CircularProgressIndicator(
                color = Color(0xFF2196F3),
                strokeWidth = 2.dp
            )
            Text(
                "Loading predictions...",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun PredictionItem(prediction: ModelPrediction) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (prediction.isActive)
                Color(0xFF1B5E20).copy(alpha = 0.2f)
            else MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (prediction.isActive) {
                        PulsingDot()
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                    Text(
                        prediction.title,
                        color = if (prediction.isActive)
                            Color(0xFF4CAF50)
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        fontWeight = if (prediction.isActive) FontWeight.Bold else FontWeight.Medium
                    )
                }
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            prediction.confidence >= 90 -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                            prediction.confidence >= 75 -> Color(0xFFFF9800).copy(alpha = 0.2f)
                            else -> Color(0xFFF44336).copy(alpha = 0.2f)
                        }
                    )
                ) {
                    Text(
                        "${prediction.confidence}%",
                        color = when {
                            prediction.confidence >= 90 -> Color(0xFF4CAF50)
                            prediction.confidence >= 75 -> Color(0xFFFF9800)
                            else -> Color(0xFFF44336)
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                prediction.prediction,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp
            )

            Text(
                prediction.impact,
                color = Color(0xFF4CAF50),
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Enhanced confidence bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(Color(0xFF30363D), RoundedCornerShape(2.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(prediction.confidence / 100f)
                        .background(
                            when {
                                prediction.confidence >= 90 -> Color(0xFF4CAF50)
                                prediction.confidence >= 75 -> Color(0xFFFF9800)
                                else -> Color(0xFFF44336)
                            },
                            RoundedCornerShape(2.dp)
                        )
                )
            }
        }
    }
}