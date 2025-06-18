/**
 * Control panel component for server management
 * Author: Alims-Repo
 * Date: 2025-06-17
 */
package com.alimsrepo.vehicledetection.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.alimsrepo.vehicledetection.data.model.ConfigurationStats

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ControlPanel(
    currentConfig: ConfigurationStats?,
    onConfigUpdate: (Map<String, Any>) -> Unit,
    onPlaybackControl: (String) -> Unit,
    onDeviceSwitch: (String) -> Unit,
    onBroadcastControl: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var confidenceThreshold by remember { mutableStateOf("0.35") }
    var targetFps by remember { mutableStateOf("30") }
    var jpegQuality by remember { mutableStateOf("80") }

    // Update local state when config changes
    LaunchedEffect(currentConfig) {
        currentConfig?.let { config ->
            confidenceThreshold = config.confidence_threshold.toString()
            targetFps = config.target_fps.toString()
            jpegQuality = config.jpeg_quality.toString()
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Playback Controls
        Card {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayCircle,
                        contentDescription = "Playback Controls",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Playback Controls",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onPlaybackControl("pause") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Icon(Icons.Default.Pause, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Pause")
                    }

                    Button(
                        onClick = { onPlaybackControl("resume") }
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Resume")
                    }

                    Button(
                        onClick = { onPlaybackControl("restart") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Restart")
                    }
                }
            }
        }

        // Device Controls
        Card {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Memory,
                        contentDescription = "Device Controls",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = "Processing Device",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onDeviceSwitch("cpu") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Icon(Icons.Default.Computer, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Use CPU")
                    }

                    Button(
                        onClick = { onDeviceSwitch("mps") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(Icons.Default.GraphicEq, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Use GPU (MPS)")
                    }
                }
            }
        }

        // Configuration Controls
        Card {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Configuration",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                    Text(
                        text = "Detection Configuration",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Confidence Threshold
                    OutlinedTextField(
                        value = confidenceThreshold,
                        onValueChange = { confidenceThreshold = it },
                        label = { Text("Confidence Threshold") },
                        supportingText = { Text("Detection sensitivity (0.1 - 0.9)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Target FPS
                    OutlinedTextField(
                        value = targetFps,
                        onValueChange = { targetFps = it },
                        label = { Text("Target FPS") },
                        supportingText = { Text("Frames per second (1 - 60)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // JPEG Quality
                    OutlinedTextField(
                        value = jpegQuality,
                        onValueChange = { jpegQuality = it },
                        label = { Text("JPEG Quality") },
                        supportingText = { Text("Image compression quality (10 - 100)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Update button
                    Button(
                        onClick = {
                            val updates = mutableMapOf<String, Any>()

                            confidenceThreshold.toDoubleOrNull()?.let { conf ->
                                if (conf in 0.1..0.9) {
                                    updates["confidence_threshold"] = conf
                                }
                            }

                            targetFps.toIntOrNull()?.let { fps ->
                                if (fps in 1..60) {
                                    updates["target_fps"] = fps
                                }
                            }

                            jpegQuality.toIntOrNull()?.let { quality ->
                                if (quality in 10..100) {
                                    updates["jpeg_quality"] = quality
                                }
                            }

                            if (updates.isNotEmpty()) {
                                onConfigUpdate(updates)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Update Configuration")
                    }
                }
            }
        }

        // Broadcast Controls
        Card {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.BroadcastOnHome,
                        contentDescription = "Broadcast Controls",
                        tint = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = "Broadcast Management",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onBroadcastControl("start") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Start")
                    }

                    Button(
                        onClick = { onBroadcastControl("stop") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(Icons.Default.Stop, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Stop")
                    }

                    Button(
                        onClick = { onBroadcastControl("disconnect_all") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Icon(Icons.Default.WifiOff, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Disconnect All")
                    }
                }
            }
        }
    }
}