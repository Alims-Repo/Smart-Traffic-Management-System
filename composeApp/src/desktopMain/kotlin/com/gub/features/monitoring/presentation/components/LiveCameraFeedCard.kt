//package com.gub.features.monitoring.presentation.components
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Videocam
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.ImageBitmap
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.gub.features.monitoring.data.network.ApiService
//import com.gub.features.monitoring.data.network.WebSocketManager
//import com.gub.features.monitoring.domain.repository.VehicleDetectionRepository
//
//@Composable
//fun LiveCameraFeedCard(modifier: Modifier = Modifier) {
//    val repository by remember {
//        mutableStateOf(
//            VehicleDetectionRepository(
//                ApiService(), WebSocketManager()
//            )
//        )
//    }
//
//    Card(
//        modifier = modifier,
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
//        shape = RoundedCornerShape(12.dp)
//    ) {
//        Column(
//            modifier = Modifier.padding(20.dp)
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    "Camera Feed",
//                    color = MaterialTheme.colorScheme.onSurfaceVariant,
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold
//                )
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Icon(
//                        Icons.Default.Videocam,
//                        contentDescription = null,
//                        tint = Color(0xFF4CAF50),
//                        modifier = Modifier.size(16.dp)
//                    )
//                    Spacer(modifier = Modifier.width(4.dp))
//                    Text(
//                        "HD",
//                        color = Color(0xFF4CAF50),
//                        fontSize = 10.sp
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(200.dp)
//                    .background(Color(0xFF0D1117), RoundedCornerShape(8.dp))
//            ) {
////                Column(
////                    modifier = Modifier.fillMaxSize(),
////                    verticalArrangement = Arrangement.Center,
////                    horizontalAlignment = Alignment.CenterHorizontally
////                ) {
////                    Icon(
////                        Icons.Default.Videocam,
////                        contentDescription = null,
////                        tint = Color.Gray,
////                        modifier = Modifier.size(32.dp)
////                    )
////                    Spacer(modifier = Modifier.height(8.dp))
////                    Text(
////                        "Live Feed Active",
////                        color = Color.Gray,
////                        fontSize = 12.sp
////                    )
////                }
//            }
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text("Vehicles: 23", color = Color.Gray, fontSize = 10.sp)
//                Text("Pedestrians: 7", color = Color.Gray, fontSize = 10.sp)
//                Text("Queue: 12m", color = Color.Gray, fontSize = 10.sp)
//            }
//        }
//    }
//}



package com.gub.features.monitoring.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gub.features.monitoring.data.network.ApiService
import com.gub.features.monitoring.data.network.WebSocketManager
import com.gub.features.monitoring.domain.repository.VehicleDetectionRepository
import org.jetbrains.skia.Image
import java.util.Base64

@Composable
fun LiveCameraFeedCard(
    modifier: Modifier = Modifier,
    isExpanded: Boolean = false,
    onToggleExpand: () -> Unit = {}
) {
    val webSocketManager by remember { mutableStateOf(WebSocketManager()) }
    val repository by remember {
        mutableStateOf(
            VehicleDetectionRepository(
                ApiService(), webSocketManager
            )
        )
    }

    // Collect WebSocket states
    val connectionState by webSocketManager.connectionState.collectAsState()
    val detectionData by webSocketManager.detectionData.collectAsState()

    // State for the current frame
    var currentFrame by remember { mutableStateOf<ImageBitmap?>(null) }
    var frameError by remember { mutableStateOf<String?>(null) }

    // Connect to WebSocket when component starts
    LaunchedEffect(Unit) {
        webSocketManager.connect()
    }

    // Clean up when component is disposed
    DisposableEffect(Unit) {
        onDispose {
            webSocketManager.disconnect()
        }
    }

    // Process incoming detection data
    LaunchedEffect(detectionData) {
        detectionData?.let { response ->
            try {
                // Remove data URL prefix if present (data:image/jpeg;base64,)
                val base64Data = response.image.let { img ->
                    if (img.startsWith("data:image")) {
                        img.substringAfter("base64,")
                    } else {
                        img
                    }
                }

                // Decode base64 to byte array using Java's Base64 decoder
                val imageBytes = Base64.getDecoder().decode(base64Data)

                // Convert to Skia Image and then to Compose ImageBitmap
                val skiaImage = Image.makeFromEncoded(imageBytes)
                if (skiaImage != null) {
                    currentFrame = skiaImage.toComposeImageBitmap()
                    frameError = null
                } else {
                    frameError = "Failed to decode image"
                }
            } catch (e: Exception) {
                frameError = "Error processing frame: ${e.message}"
                println("❌ Error processing video frame: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                    if (isExpanded) "Camera Feed - Expanded View" else "Camera Feed",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        when (connectionState) {
                            WebSocketManager.ConnectionState.CONNECTED -> Icons.Default.Videocam
                            WebSocketManager.ConnectionState.ERROR -> Icons.Default.Warning
                            else -> Icons.Default.Videocam
                        },
                        contentDescription = null,
                        tint = when (connectionState) {
                            WebSocketManager.ConnectionState.CONNECTED -> Color(0xFF4CAF50)
                            WebSocketManager.ConnectionState.CONNECTING -> Color(0xFFFFA726)
                            WebSocketManager.ConnectionState.ERROR -> Color(0xFFF44336)
                            else -> Color.Gray
                        },
                        modifier = Modifier.size(if (isExpanded) 20.dp else 16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        when (connectionState) {
                            WebSocketManager.ConnectionState.CONNECTED -> "LIVE"
                            WebSocketManager.ConnectionState.CONNECTING -> "..."
                            WebSocketManager.ConnectionState.ERROR -> "ERR"
                            else -> "OFF"
                        },
                        color = when (connectionState) {
                            WebSocketManager.ConnectionState.CONNECTED -> Color(0xFF4CAF50)
                            WebSocketManager.ConnectionState.CONNECTING -> Color(0xFFFFA726)
                            WebSocketManager.ConnectionState.ERROR -> Color(0xFFF44336)
                            else -> Color.Gray
                        },
                        fontSize = if (isExpanded) 12.sp else 10.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Toggle expand/collapse button
                    IconButton(
                        onClick = onToggleExpand,
                        modifier = Modifier.size(if (isExpanded) 32.dp else 24.dp)
                    ) {
                        Icon(
                            if (isExpanded) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                            contentDescription = if (isExpanded) "Collapse" else "Expand",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(if (isExpanded) 20.dp else 16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Video feed container - adjusted height based on expansion state
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isExpanded) 500.dp else 200.dp)
                    .background(Color(0xFF0D1117), RoundedCornerShape(8.dp))
                    .clickable { onToggleExpand() }
            ) {
                when {
                    currentFrame != null -> {
                        // Display the live video frame
                        Image(
                            bitmap = currentFrame!!,
                            contentDescription = "Live camera feed",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = if (isExpanded) ContentScale.Fit else ContentScale.Crop
                        )

                        // Overlay vehicle count if available
                        detectionData?.let { data ->
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(if (isExpanded) 16.dp else 8.dp)
                                    .background(
                                        Color.Black.copy(alpha = 0.7f),
                                        RoundedCornerShape(6.dp)
                                    )
                                    .padding(
                                        horizontal = if (isExpanded) 12.dp else 8.dp,
                                        vertical = if (isExpanded) 8.dp else 4.dp
                                    )
                            ) {
                                Text(
                                    "Vehicles: ${data.vehicleCount}",
                                    color = Color.White,
                                    fontSize = if (isExpanded) 14.sp else 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Add frame count overlay when expanded
                        if (isExpanded) {
                            detectionData?.let { data ->
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(16.dp)
                                        .background(
                                            Color.Black.copy(alpha = 0.7f),
                                            RoundedCornerShape(6.dp)
                                        )
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Column {
                                        Text(
                                            "Frame: ${data.frameCount ?: 0}",
                                            color = Color.White,
                                            fontSize = 12.sp
                                        )
                                        Text(
                                            "Device: ${data.device}",
                                            color = Color.Gray,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    connectionState == WebSocketManager.ConnectionState.CONNECTING -> {
                        // Show loading state
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = Color(0xFF4CAF50),
                                modifier = Modifier.size(if (isExpanded) 48.dp else 24.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Connecting to camera...",
                                color = Color.Gray,
                                fontSize = if (isExpanded) 16.sp else 12.sp
                            )
                        }
                    }

                    connectionState == WebSocketManager.ConnectionState.ERROR || frameError != null -> {
                        // Show error state
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color(0xFFF44336),
                                modifier = Modifier.size(if (isExpanded) 48.dp else 32.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                frameError ?: "Connection Error",
                                color = Color(0xFFF44336),
                                fontSize = if (isExpanded) 16.sp else 12.sp
                            )
                        }
                    }

                    else -> {
                        // Show disconnected state
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Videocam,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(if (isExpanded) 48.dp else 32.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Camera Offline",
                                color = Color.Gray,
                                fontSize = if (isExpanded) 16.sp else 12.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Stats row - enhanced when expanded
            if (isExpanded) {
                // Enhanced stats for expanded view
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Vehicles: ${detectionData?.vehicleCount ?: "--"}",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            "Detection FPS: ${detectionData?.performance?.detectionFps?.let { "%.1f".format(it) } ?: "--"}",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Broadcast FPS: ${detectionData?.performance?.broadcastFps?.let { "%.1f".format(it) } ?: "--"}",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            "Device: ${detectionData?.device ?: "--"}",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    if (detectionData?.m1ProOptimized == true) {
                        Text(
                            "M1 Pro Optimized: Enabled",
                            color = Color(0xFF4CAF50),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else {
                // Compact stats for normal view
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Vehicles: ${detectionData?.vehicleCount ?: "--"}",
                        color = Color.Gray,
                        fontSize = 10.sp
                    )
                    Text(
                        "FPS: ${detectionData?.performance?.detectionFps?.let { "%.1f".format(it) } ?: "--"}",
                        color = Color.Gray,
                        fontSize = 10.sp
                    )
                    Text(
                        "Device: ${detectionData?.device ?: "--"}",
                        color = Color.Gray,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}