package com.gub.features.settings.presentation

import androidx.compose.runtime.Composable
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import androidx.compose.animation.core.*
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gub.core.ui.components.PulsingDot
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.draw.scale
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun Settings() {
    var selectedSettingsTab by remember { mutableStateOf(0) }
    val currentTime = "2025-06-17 16:20:18 UTC"

    val settingsTabs = listOf("General", "Notifications", "AI & Automation", "Data & Privacy", "System")

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Settings & Configuration",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Text(
                                "Last updated: $currentTime",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                Icons.Default.Settings,
                                contentDescription = null,
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }

                    com.gub.core.ui.components.UserProfileChip()
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Settings Tab Navigation
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    settingsTabs.forEachIndexed { index, tab ->
                        FilterChip(
                            onClick = { selectedSettingsTab = index },
                            label = {
                                Text(
                                    tab,
                                    fontSize = 11.sp,
                                    color = if (selectedSettingsTab == index) Color.White else Color.Gray
                                )
                            },
                            selected = selectedSettingsTab == index,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF2E7D32),
                                containerColor = Color(0xFF161B22)
                            )
                        )
                    }
                }
            }
        }

        // Settings Content based on selected tab
        when (selectedSettingsTab) {
            0 -> { // General Settings
                item { GeneralSettingsSection() }
            }
            1 -> { // Notifications
                item { NotificationSettingsSection() }
            }
            2 -> { // AI & Automation
                item { AIAutomationSettingsSection() }
            }
            3 -> { // Data & Privacy
                item { DataPrivacySettingsSection() }
            }
            4 -> { // System
                item { SystemSettingsSection() }
            }
        }

        // Settings Footer
        item {
            SettingsFooter()
        }
    }
}

@Composable
fun GeneralSettingsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // User Profile Settings
        UserProfileSettingsCard()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DashboardPreferencesCard(modifier = Modifier.weight(1f))
            DisplaySettingsCard(modifier = Modifier.weight(1f))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LanguageRegionCard(modifier = Modifier.weight(1f))
            AccessibilityCard(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun UserProfileSettingsCard() {
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
                    "User Profile",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                OutlinedButton(
                    onClick = { },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2196F3)),
                    border = BorderStroke(1.dp, Color(0xFF2196F3).copy(alpha = 0.5f))
                ) {
                    Text("Edit Profile", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(Color(0xFF4CAF50), Color(0xFF2E7D32))
                            ),
                            shape = CircleShape
                        )
                ) {
                    Text(
                        "AR",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Alims-Repo",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Traffic Management Administrator",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                    Text(
                        "Member since: March 2024",
                        color = Color.Gray,
                        fontSize = 10.sp
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B5E20))
                    ) {
                        Text(
                            "Level 3",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Expert User",
                        color = Color(0xFF4CAF50),
                        fontSize = 9.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ProfileStatItem("Reports Generated", "24")
                ProfileStatItem("Optimizations", "13")
                ProfileStatItem("Session Time", "3h 27m")
                ProfileStatItem("Last Login", "Today")
            }
        }
    }
}

@Composable
fun ProfileStatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            value,
            color = Color(0xFF2196F3),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            label,
            color = Color.Gray,
            fontSize = 9.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun DashboardPreferencesCard(modifier: Modifier = Modifier) {
    var autoRefresh by remember { mutableStateOf(true) }
    var refreshInterval by remember { mutableStateOf(30) }
    var compactView by remember { mutableStateOf(false) }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Dashboard Preferences",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            SettingItem(
                title = "Auto-refresh Data",
                description = "Automatically update dashboard data",
                value = autoRefresh,
                onValueChange = { autoRefresh = it }
            )

            if (autoRefresh) {
                Spacer(modifier = Modifier.height(8.dp))
                RefreshIntervalSelector(
                    selectedInterval = refreshInterval,
                    onIntervalSelected = { refreshInterval = it }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            SettingItem(
                title = "Compact View",
                description = "Show more data in less space",
                value = compactView,
                onValueChange = { compactView = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Default Dashboard Tab",
                color = Color.Gray,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(4.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Overview", "Analytics", "Monitoring").forEach { tab ->
                    FilterChip(
                        onClick = { },
                        label = {
                            Text(
                                tab,
                                fontSize = 10.sp,
                                color = if (tab == "Overview") Color.White else Color.Gray
                            )
                        },
                        selected = tab == "Overview",
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF2E7D32),
                            containerColor = Color(0xFF0D1117)
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun RefreshIntervalSelector(selectedInterval: Int, onIntervalSelected: (Int) -> Unit) {
    val intervals = listOf(15, 30, 60, 120)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            "Refresh every:",
            color = Color.Gray,
            fontSize = 11.sp,
            modifier = Modifier.align(Alignment.CenterVertically)
        )

        intervals.forEach { interval ->
            FilterChip(
                onClick = { onIntervalSelected(interval) },
                label = {
                    Text(
                        "${interval}s",
                        fontSize = 9.sp,
                        color = if (selectedInterval == interval) Color.White else Color.Gray
                    )
                },
                selected = selectedInterval == interval,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF1976D2),
                    containerColor = Color(0xFF0D1117)
                )
            )
        }
    }
}

@Composable
fun DisplaySettingsCard(modifier: Modifier = Modifier) {
    var darkMode by remember { mutableStateOf(true) }
    var animationsEnabled by remember { mutableStateOf(true) }
    var highContrast by remember { mutableStateOf(false) }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Display Settings",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            SettingItem(
                title = "Dark Mode",
                description = "Use dark theme for better visibility",
                value = darkMode,
                onValueChange = { darkMode = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            SettingItem(
                title = "Animations",
                description = "Enable smooth transitions and effects",
                value = animationsEnabled,
                onValueChange = { animationsEnabled = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            SettingItem(
                title = "High Contrast",
                description = "Improve readability with enhanced contrast",
                value = highContrast,
                onValueChange = { highContrast = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Chart Style",
                color = Color.Gray,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(4.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Modern", "Classic", "Minimal").forEach { style ->
                    FilterChip(
                        onClick = { },
                        label = {
                            Text(
                                style,
                                fontSize = 10.sp,
                                color = if (style == "Modern") Color.White else Color.Gray
                            )
                        },
                        selected = style == "Modern",
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF2E7D32),
                            containerColor = Color(0xFF0D1117)
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun LanguageRegionCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Language & Region",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            LanguageRegionItem("Language", "English (US)", Icons.Default.Language)
            LanguageRegionItem("Time Zone", "UTC", Icons.Default.Schedule)
            LanguageRegionItem("Date Format", "YYYY-MM-DD", Icons.Default.DateRange)
            LanguageRegionItem("Number Format", "1,234.56", Icons.Default.Tag)

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2196F3)),
                border = BorderStroke(1.dp, Color(0xFF2196F3).copy(alpha = 0.5f))
            ) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Configure Regional Settings", fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun LanguageRegionItem(label: String, value: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(label, color = Color.Gray, fontSize = 12.sp)
        }
        Text(
            value,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun AccessibilityCard(modifier: Modifier = Modifier) {
    var screenReader by remember { mutableStateOf(false) }
    var keyboardNavigation by remember { mutableStateOf(true) }
    var reducedMotion by remember { mutableStateOf(false) }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Accessibility",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            SettingItem(
                title = "Screen Reader Support",
                description = "Enhanced compatibility with screen readers",
                value = screenReader,
                onValueChange = { screenReader = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            SettingItem(
                title = "Keyboard Navigation",
                description = "Enable full keyboard accessibility",
                value = keyboardNavigation,
                onValueChange = { keyboardNavigation = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            SettingItem(
                title = "Reduced Motion",
                description = "Minimize animations and transitions",
                value = reducedMotion,
                onValueChange = { reducedMotion = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Font Size",
                    color = Color.Gray,
                    fontSize = 12.sp
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Small", "Medium", "Large").forEach { size ->
                        FilterChip(
                            onClick = { },
                            label = {
                                Text(
                                    size,
                                    fontSize = 9.sp,
                                    color = if (size == "Medium") Color.White else Color.Gray
                                )
                            },
                            selected = size == "Medium",
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF2E7D32),
                                containerColor = Color(0xFF0D1117)
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationSettingsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AlertNotificationsCard(modifier = Modifier.weight(1f))
            SystemNotificationsCard(modifier = Modifier.weight(1f))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            NotificationChannelsCard(modifier = Modifier.weight(1f))
            QuietHoursCard(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun AlertNotificationsCard(modifier: Modifier = Modifier) {
    var criticalAlerts by remember { mutableStateOf(true) }
    var highPriorityAlerts by remember { mutableStateOf(true) }
    var mediumPriorityAlerts by remember { mutableStateOf(false) }
    var trafficUpdates by remember { mutableStateOf(true) }

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
                    "Alert Notifications",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    Icons.Default.NotificationsActive,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            NotificationTypeItem(
                title = "Critical Alerts",
                description = "System failures, emergencies",
                value = criticalAlerts,
                onValueChange = { criticalAlerts = it },
                priority = "Critical"
            )

            NotificationTypeItem(
                title = "High Priority",
                description = "Major congestion, incidents",
                value = highPriorityAlerts,
                onValueChange = { highPriorityAlerts = it },
                priority = "High"
            )

            NotificationTypeItem(
                title = "Medium Priority",
                description = "Traffic pattern changes",
                value = mediumPriorityAlerts,
                onValueChange = { mediumPriorityAlerts = it },
                priority = "Medium"
            )

            NotificationTypeItem(
                title = "Traffic Updates",
                description = "General traffic information",
                value = trafficUpdates,
                onValueChange = { trafficUpdates = it },
                priority = "Info"
            )
        }
    }
}

@Composable
fun NotificationTypeItem(
    title: String,
    description: String,
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
    priority: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        when (priority) {
                            "Critical" -> Color(0xFFF44336)
                            "High" -> Color(0xFFFF9800)
                            "Medium" -> Color(0xFF2196F3)
                            else -> Color(0xFF4CAF50)
                        },
                        CircleShape
                    )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    title,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    description,
                    color = Color.Gray,
                    fontSize = 10.sp
                )
            }
        }

        Switch(
            checked = value,
            onCheckedChange = onValueChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF4CAF50),
                checkedTrackColor = Color(0xFF1B5E20)
            ),
            modifier = Modifier.scale(0.8f)
        )
    }
}

@Composable
fun SystemNotificationsCard(modifier: Modifier = Modifier) {
    var aiUpdates by remember { mutableStateOf(true) }
    var systemMaintenance by remember { mutableStateOf(true) }
    var reportReady by remember { mutableStateOf(false) }
    var weeklyDigest by remember { mutableStateOf(true) }

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
                    "System Notifications",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    Icons.Default.Computer,
                    contentDescription = null,
                    tint = Color(0xFF2196F3)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            SettingItem(
                title = "AI Optimization Updates",
                description = "When AI makes system improvements",
                value = aiUpdates,
                onValueChange = { aiUpdates = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            SettingItem(
                title = "System Maintenance",
                description = "Scheduled maintenance notifications",
                value = systemMaintenance,
                onValueChange = { systemMaintenance = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            SettingItem(
                title = "Report Ready",
                description = "When analytics reports are generated",
                value = reportReady,
                onValueChange = { reportReady = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            SettingItem(
                title = "Weekly Digest",
                description = "Summary of weekly performance",
                value = weeklyDigest,
                onValueChange = { weeklyDigest = it }
            )
        }
    }
}

@Composable
fun NotificationChannelsCard(modifier: Modifier = Modifier) {
    var emailEnabled by remember { mutableStateOf(true) }
    var pushEnabled by remember { mutableStateOf(true) }
    var smsEnabled by remember { mutableStateOf(false) }
    var slackEnabled by remember { mutableStateOf(false) }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Notification Channels",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            NotificationChannelItem(
                title = "Email",
                description = "alims-repo@traffic.system",
                icon = Icons.Default.Email,
                enabled = emailEnabled,
                onToggle = { emailEnabled = it }
            )

            NotificationChannelItem(
                title = "Push Notifications",
                description = "Browser notifications",
                icon = Icons.Default.Notifications,
                enabled = pushEnabled,
                onToggle = { pushEnabled = it }
            )

            NotificationChannelItem(
                title = "SMS",
                description = "Text message alerts",
                icon = Icons.Default.Sms,
                enabled = smsEnabled,
                onToggle = { smsEnabled = it }
            )

            NotificationChannelItem(
                title = "Slack Integration",
                description = "Workspace notifications",
                icon = Icons.Default.Chat,
                enabled = slackEnabled,
                onToggle = { slackEnabled = it }
            )
        }
    }
}

@Composable
fun NotificationChannelItem(
    title: String,
    description: String,
    icon: ImageVector,
    enabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (enabled) Color(0xFF4CAF50) else Color.Gray,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    title,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    description,
                    color = Color.Gray,
                    fontSize = 10.sp
                )
            }
        }

        Switch(
            checked = enabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF4CAF50),
                checkedTrackColor = Color(0xFF1B5E20)
            ),
            modifier = Modifier.scale(0.8f)
        )
    }
}

@Composable
fun QuietHoursCard(modifier: Modifier = Modifier) {
    var quietHoursEnabled by remember { mutableStateOf(true) }
    var startTime by remember { mutableStateOf("22:00") }
    var endTime by remember { mutableStateOf("07:00") }
    var weekendsOnly by remember { mutableStateOf(false) }

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
                    "Quiet Hours",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Switch(
                    checked = quietHoursEnabled,
                    onCheckedChange = { quietHoursEnabled = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF4CAF50),
                        checkedTrackColor = Color(0xFF1B5E20)
                    ),
                    modifier = Modifier.scale(0.8f)
                )
            }

            if (quietHoursEnabled) {
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Start Time",
                            color = Color.Gray,
                            fontSize = 11.sp
                        )
                        Text(
                            startTime,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )

                    Column {
                        Text(
                            "End Time",
                            color = Color.Gray,
                            fontSize = 11.sp
                        )
                        Text(
                            endTime,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                SettingItem(
                    title = "Weekends Only",
                    description = "Apply quiet hours only on weekends",
                    value = weekendsOnly,
                    onValueChange = { weekendsOnly = it }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "During quiet hours, only critical alerts will be sent",
                    color = Color.Gray,
                    fontSize = 10.sp,
                    style = androidx.compose.ui.text.TextStyle(
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                )
            }
        }
    }
}

@Composable
fun AIAutomationSettingsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AIControlCard(modifier = Modifier.weight(1f))
            AutomationRulesCard(modifier = Modifier.weight(1f))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LearningPreferencesCard(modifier = Modifier.weight(1f))
            AIPerformanceMonitoringCard(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun AIControlCard(modifier: Modifier = Modifier) {
    var aiEnabled by remember { mutableStateOf(true) }
    var autoOptimization by remember { mutableStateOf(true) }
    var predictiveMode by remember { mutableStateOf(true) }
    var aggressiveness by remember { mutableStateOf(2) }

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
                    "AI Control",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (aiEnabled) Color(0xFF1B5E20) else Color(0xFF424242)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Psychology,
                            contentDescription = null,
                            tint = if (aiEnabled) Color(0xFF4CAF50) else Color.Gray,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            if (aiEnabled) "ACTIVE" else "INACTIVE",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            SettingItem(
                title = "AI System Enabled",
                description = "Master control for all AI features",
                value = aiEnabled,
                onValueChange = { aiEnabled = it }
            )

            if (aiEnabled) {
                Spacer(modifier = Modifier.height(8.dp))

                SettingItem(
                    title = "Auto-Optimization",
                    description = "Allow AI to make automatic adjustments",
                    value = autoOptimization,
                    onValueChange = { autoOptimization = it }
                )

                Spacer(modifier = Modifier.height(8.dp))

                SettingItem(
                    title = "Predictive Mode",
                    description = "Use machine learning for traffic prediction",
                    value = predictiveMode,
                    onValueChange = { predictiveMode = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "Optimization Aggressiveness",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Conservative", "Balanced", "Aggressive").forEachIndexed { index, level ->
                        FilterChip(
                            onClick = { aggressiveness = index },
                            label = {
                                Text(
                                    level,
                                    fontSize = 9.sp,
                                    color = if (aggressiveness == index) Color.White else Color.Gray
                                )
                            },
                            selected = aggressiveness == index,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = when (index) {
                                    0 -> Color(0xFF4CAF50)
                                    1 -> Color(0xFF2196F3)
                                    2 -> Color(0xFFFF9800)
                                    else -> Color(0xFF2E7D32)
                                },
                                containerColor = Color(0xFF0D1117)
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AutomationRulesCard(modifier: Modifier = Modifier) {
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
                    "Automation Rules",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                OutlinedButton(
                    onClick = { },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2196F3)),
                    border = BorderStroke(1.dp, Color(0xFF2196F3).copy(alpha = 0.5f))
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("New Rule", fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            AutomationRuleItem(
                title = "Peak Hour Response",
                description = "Auto-adjust signals during rush hours",
                enabled = true,
                trigger = "Traffic > 80%"
            )

            AutomationRuleItem(
                title = "Incident Detection",
                description = "Reroute traffic when incidents detected",
                enabled = true,
                trigger = "Incident Alert"
            )

            AutomationRuleItem(
                title = "Weather Adaptation",
                description = "Modify timing for weather conditions",
                enabled = false,
                trigger = "Weather Alert"
            )

            AutomationRuleItem(
                title = "Maintenance Mode",
                description = "Activate during scheduled maintenance",
                enabled = true,
                trigger = "Schedule"
            )
        }
    }
}

@Composable
fun AutomationRuleItem(title: String, description: String, enabled: Boolean, trigger: String) {
    var isEnabled by remember { mutableStateOf(enabled) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                description,
                color = Color.Gray,
                fontSize = 10.sp
            )
            Text(
                "Trigger: $trigger",
                color = Color(0xFF2196F3),
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Switch(
            checked = isEnabled,
            onCheckedChange = { isEnabled = it },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF4CAF50),
                checkedTrackColor = Color(0xFF1B5E20)
            ),
            modifier = Modifier.scale(0.7f)
        )
    }
}

@Composable
fun LearningPreferencesCard(modifier: Modifier = Modifier) {
    var collectData by remember { mutableStateOf(true) }
    var shareAnonymous by remember { mutableStateOf(false) }
    var adaptiveLearning by remember { mutableStateOf(true) }

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
                    "Learning Preferences",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    Icons.Default.School,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SettingItem(
                title = "Collect Usage Data",
                description = "Allow AI to learn from traffic patterns",
                value = collectData,
                onValueChange = { collectData = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            SettingItem(
                title = "Share Anonymous Data",
                description = "Contribute to global traffic optimization",
                value = shareAnonymous,
                onValueChange = { shareAnonymous = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            SettingItem(
                title = "Adaptive Learning",
                description = "Continuously improve based on outcomes",
                value = adaptiveLearning,
                onValueChange = { adaptiveLearning = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1117))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "AI Learning Status",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Model Accuracy: 97.8%",
                            color = Color(0xFF4CAF50),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            "Last Updated: 2h ago",
                            color = Color.Gray,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AIPerformanceMonitoringCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "AI Performance Monitoring",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            AIMetricMonitorItem("Decision Speed", "0.28ms", Color(0xFF4CAF50), "Excellent")
            AIMetricMonitorItem("Accuracy Rate", "97.8%", Color(0xFF4CAF50), "Very High")
            AIMetricMonitorItem("Learning Rate", "97.8%", Color(0xFF2196F3), "Optimal")
            AIMetricMonitorItem("Error Rate", "2.2%", Color(0xFF4CAF50), "Low")

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = { },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2196F3)),
                    border = BorderStroke(1.dp, Color(0xFF2196F3).copy(alpha = 0.5f))
                ) {
                    Text("View Details", fontSize = 11.sp)
                }

                OutlinedButton(
                    onClick = { },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF9800)),
                    border = BorderStroke(1.dp, Color(0xFFFF9800).copy(alpha = 0.5f))
                ) {
                    Text("Reset AI", fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
fun AIMetricMonitorItem(label: String, value: String, color: Color, status: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color.Gray, fontSize = 11.sp)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                value,
                color = color,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                status,
                color = color,
                fontSize = 9.sp
            )
        }
    }
}

@Composable
fun DataPrivacySettingsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DataRetentionCard(modifier = Modifier.weight(1f))
            PrivacyControlsCard(modifier = Modifier.weight(1f))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ExportImportCard(modifier = Modifier.weight(1f))
            SecuritySettingsCard(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun DataRetentionCard(modifier: Modifier = Modifier) {
    var retentionPeriod by remember { mutableStateOf(2) }
    var autoDelete by remember { mutableStateOf(true) }

    val periods = listOf("30 days", "90 days", "1 year", "2 years", "5 years")

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
                    "Data Retention",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    Icons.Default.Storage,
                    contentDescription = null,
                    tint = Color(0xFF2196F3)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Traffic Data Retention Period",
                color = Color.Gray,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.height(120.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(periods.size) { index ->
                    FilterChip(
                        onClick = { retentionPeriod = index },
                        label = {
                            Text(
                                periods[index],
                                fontSize = 11.sp,
                                color = if (retentionPeriod == index) Color.White else Color.Gray
                            )
                        },
                        selected = retentionPeriod == index,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF2196F3),
                            containerColor = Color(0xFF0D1117)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            SettingItem(
                title = "Auto-delete Old Data",
                description = "Automatically remove data after retention period",
                value = autoDelete,
                onValueChange = { autoDelete = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1117))
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        "Storage Usage",
                        color = Color.Gray,
                        fontSize = 10.sp
                    )
                    Text(
                        "2.3 GB / 10 GB (23% used)",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun PrivacyControlsCard(modifier: Modifier = Modifier) {
    var analytics by remember { mutableStateOf(true) }
    var thirdParty by remember { mutableStateOf(false) }
    var locationData by remember { mutableStateOf(true) }
    var behavioralData by remember { mutableStateOf(false) }

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
                    "Privacy Controls",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    Icons.Default.Security,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SettingItem(
                title = "Analytics Collection",
                description = "Allow collection of usage analytics",
                value = analytics,
                onValueChange = { analytics = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            SettingItem(
                title = "Third-party Sharing",
                description = "Share data with partner organizations",
                value = thirdParty,
                onValueChange = { thirdParty = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            SettingItem(
                title = "Location Data",
                description = "Include precise location information",
                value = locationData,
                onValueChange = { locationData = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            SettingItem(
                title = "Behavioral Analytics",
                description = "Track user interaction patterns",
                value = behavioralData,
                onValueChange = { behavioralData = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2196F3)),
                border = BorderStroke(1.dp, Color(0xFF2196F3).copy(alpha = 0.5f))
            ) {
                Icon(
                    Icons.Default.Description,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("View Privacy Policy", fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun ExportImportCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Data Export & Import",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            ExportImportItem(
                title = "Export User Data",
                description = "Download all your personal data",
                icon = Icons.Default.FileDownload,
                actionText = "Export"
            )

            ExportImportItem(
                title = "Export Settings",
                description = "Download current configuration",
                icon = Icons.Default.Settings,
                actionText = "Export"
            )

            ExportImportItem(
                title = "Import Settings",
                description = "Upload configuration file",
                icon = Icons.Default.FileUpload,
                actionText = "Import"
            )

            ExportImportItem(
                title = "Delete All Data",
                description = "Permanently remove all personal data",
                icon = Icons.Default.DeleteForever,
                actionText = "Delete",
                destructive = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Last export: 2025-06-10 14:30 UTC",
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
fun ExportImportItem(
    title: String,
    description: String,
    icon: ImageVector,
    actionText: String,
    destructive: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (destructive) Color(0xFFF44336) else Color.Gray,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    title,
                    color = if (destructive) Color(0xFFF44336) else Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    description,
                    color = Color.Gray,
                    fontSize = 10.sp
                )
            }
        }

        OutlinedButton(
            onClick = { },
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = if (destructive) Color(0xFFF44336) else Color(0xFF2196F3)
            ),
            border = BorderStroke(
                1.dp,
                if (destructive) Color(0xFFF44336).copy(alpha = 0.5f) else Color(0xFF2196F3).copy(alpha = 0.5f)
            )
        ) {
            Text(actionText, fontSize = 10.sp)
        }
    }
}

@Composable
fun SecuritySettingsCard(modifier: Modifier = Modifier) {
    var twoFactorAuth by remember { mutableStateOf(true) }
    var sessionTimeout by remember { mutableStateOf(2) }
    var apiKeyAccess by remember { mutableStateOf(false) }

    val timeoutOptions = listOf("15 min", "30 min", "1 hour", "4 hours", "Never")

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
                    "Security Settings",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    Icons.Default.Shield,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SettingItem(
                title = "Two-Factor Authentication",
                description = "Add extra security to your account",
                value = twoFactorAuth,
                onValueChange = { twoFactorAuth = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            SettingItem(
                title = "API Key Access",
                description = "Allow access via API keys",
                value = apiKeyAccess,
                onValueChange = { apiKeyAccess = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Session Timeout",
                color = Color.Gray,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(4.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                timeoutOptions.take(3).forEachIndexed { index, option ->
                    FilterChip(
                        onClick = { sessionTimeout = index },
                        label = {
                            Text(
                                option,
                                fontSize = 9.sp,
                                color = if (sessionTimeout == index) Color.White else Color.Gray
                            )
                        },
                        selected = sessionTimeout == index,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF2196F3),
                            containerColor = Color(0xFF0D1117)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2196F3)),
                    border = BorderStroke(1.dp, Color(0xFF2196F3).copy(alpha = 0.5f))
                ) {
                    Text("Change Password", fontSize = 10.sp)
                }

                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF9800)),
                    border = BorderStroke(1.dp, Color(0xFFFF9800).copy(alpha = 0.5f))
                ) {
                    Text("View Sessions", fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
fun SystemSettingsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SystemStatusCard(modifier = Modifier.weight(1f))
            MaintenanceCard(modifier = Modifier.weight(1f))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BackupRestoreCard(modifier = Modifier.weight(1f))
            UpdatesCard(modifier = Modifier.weight(1f))
        }

        SystemLogsCard()
    }
}

@Composable
fun SystemStatusCard(modifier: Modifier = Modifier) {
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
                    "System Status",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B5E20))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PulsingDot()
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "OPERATIONAL",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            SystemStatusItem("System Health", "Excellent", Color(0xFF4CAF50))
            SystemStatusItem("Database", "Online", Color(0xFF4CAF50))
            SystemStatusItem("AI Models", "12/12 Active", Color(0xFF4CAF50))
            SystemStatusItem("Network", "Stable", Color(0xFF4CAF50))
            SystemStatusItem("Storage", "73% Available", Color(0xFF2196F3))

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1117))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Uptime:", color = Color.Gray, fontSize = 11.sp)
                        Text("47 days, 12h 23m", color = Color(0xFF4CAF50), fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Last Restart:", color = Color.Gray, fontSize = 11.sp)
                        Text("2025-05-01 04:00 UTC", color = Color.White, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun SystemStatusItem(label: String, status: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color.Gray, fontSize = 11.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(color, CircleShape)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                status,
                color = color,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun MaintenanceCard(modifier: Modifier = Modifier) {
    var maintenanceMode by remember { mutableStateOf(false) }
    var autoMaintenance by remember { mutableStateOf(true) }

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
                    "Maintenance",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    Icons.Default.Build,
                    contentDescription = null,
                    tint = Color(0xFFFF9800)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SettingItem(
                title = "Maintenance Mode",
                description = "Enable system-wide maintenance mode",
                value = maintenanceMode,
                onValueChange = { maintenanceMode = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            SettingItem(
                title = "Auto-Maintenance",
                description = "Schedule automatic maintenance tasks",
                value = autoMaintenance,
                onValueChange = { autoMaintenance = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1117))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "Next Scheduled Maintenance",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                    Text(
                        "2025-06-21 02:00 UTC",
                        color = Color(0xFFFF9800),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        "Estimated duration: 2 hours",
                        color = Color.Gray,
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF9800)),
                    border = BorderStroke(1.dp, Color(0xFFFF9800).copy(alpha = 0.5f))
                ) {
                    Text("Schedule", fontSize = 10.sp)
                }

                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2196F3)),
                    border = BorderStroke(1.dp, Color(0xFF2196F3).copy(alpha = 0.5f))
                ) {
                    Text("Run Now", fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
fun BackupRestoreCard(modifier: Modifier = Modifier) {
    var autoBackup by remember { mutableStateOf(true) }
    var backupFrequency by remember { mutableStateOf(1) }

    val frequencies = listOf("Daily", "Weekly", "Monthly")

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
                    "Backup & Restore",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    Icons.Default.Backup,
                    contentDescription = null,
                    tint = Color(0xFF2196F3)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SettingItem(
                title = "Automatic Backups",
                description = "Create regular system backups",
                value = autoBackup,
                onValueChange = { autoBackup = it }
            )

            if (autoBackup) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Backup Frequency",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    frequencies.forEachIndexed { index, freq ->
                        FilterChip(
                            onClick = { backupFrequency = index },
                            label = {
                                Text(
                                    freq,
                                    fontSize = 10.sp,
                                    color = if (backupFrequency == index) Color.White else Color.Gray
                                )
                            },
                            selected = backupFrequency == index,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF2196F3),
                                containerColor = Color(0xFF0D1117)
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1117))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Last Backup:", color = Color.Gray, fontSize = 11.sp)
                        Text("2025-06-17 02:00 UTC", color = Color.White, fontSize = 11.sp)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Backup Size:", color = Color.Gray, fontSize = 11.sp)
                        Text("1.7 GB", color = Color(0xFF2196F3), fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF4CAF50)),
                    border = BorderStroke(1.dp, Color(0xFF4CAF50).copy(alpha = 0.5f))
                ) {
                    Text("Backup Now", fontSize = 10.sp)
                }

                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF9800)),
                    border = BorderStroke(1.dp, Color(0xFFFF9800).copy(alpha = 0.5f))
                ) {
                    Text("Restore", fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
fun UpdatesCard(modifier: Modifier = Modifier) {
    var autoUpdates by remember { mutableStateOf(false) }
    var updateChannel by remember { mutableStateOf(1) }

    val channels = listOf("Stable", "Beta", "Alpha")

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
                    "System Updates",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B5E20))
                ) {
                    Text(
                        "UP TO DATE",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            SettingItem(
                title = "Automatic Updates",
                description = "Install updates automatically",
                value = autoUpdates,
                onValueChange = { autoUpdates = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Update Channel",
                color = Color.Gray,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(4.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                channels.forEachIndexed { index, channel ->
                    FilterChip(
                        onClick = { updateChannel = index },
                        label = {
                            Text(
                                channel,
                                fontSize = 10.sp,
                                color = if (updateChannel == index) Color.White else Color.Gray
                            )
                        },
                        selected = updateChannel == index,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = when (index) {
                                0 -> Color(0xFF4CAF50)
                                1 -> Color(0xFFFF9800)
                                2 -> Color(0xFFF44336)
                                else -> Color(0xFF2E7D32)
                            },
                            containerColor = Color(0xFF0D1117)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1117))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Current Version:", color = Color.Gray, fontSize = 11.sp)
                        Text("v2.4.1", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Last Update:", color = Color.Gray, fontSize = 11.sp)
                        Text("2025-06-15 09:30 UTC", color = Color.White, fontSize = 11.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2196F3)),
                    border = BorderStroke(1.dp, Color(0xFF2196F3).copy(alpha = 0.5f))
                ) {
                    Text("Check Updates", fontSize = 10.sp)
                }

                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF4CAF50)),
                    border = BorderStroke(1.dp, Color(0xFF4CAF50).copy(alpha = 0.5f))
                ) {
                    Text("Release Notes", fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
fun SystemLogsCard() {
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
                    "System Logs",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                OutlinedButton(
                    onClick = { },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2196F3)),
                    border = BorderStroke(1.dp, Color(0xFF2196F3).copy(alpha = 0.5f))
                ) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Refresh", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1117))
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(10) { index ->
                        val logs = listOf(
                            LogEntry("16:23:46", "INFO", "AI optimization completed successfully", Color(0xFF4CAF50)),
                            LogEntry("16:21:12", "INFO", "User Alims-Repo logged in", Color(0xFF2196F3)),
                            LogEntry("16:18:33", "WARN", "High traffic detected at Main St intersection", Color(0xFFFF9800)),
                            LogEntry("16:15:07", "INFO", "Signal timing updated for 5th Ave", Color(0xFF4CAF50)),
                            LogEntry("16:12:45", "INFO", "Backup completed successfully", Color(0xFF4CAF50)),
                            LogEntry("16:08:21", "INFO", "AI model updated with new patterns", Color(0xFF2196F3)),
                            LogEntry("16:05:33", "INFO", "Traffic flow optimization activated", Color(0xFF4CAF50)),
                            LogEntry("16:02:18", "WARN", "Database connection timeout recovered", Color(0xFFFF9800)),
                            LogEntry("15:58:44", "INFO", "System health check passed", Color(0xFF4CAF50)),
                            LogEntry("15:55:12", "INFO", "Analytics report generated", Color(0xFF2196F3))
                        )

                        SystemLogItem(logs[index])
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2196F3)),
                    border = BorderStroke(1.dp, Color(0xFF2196F3).copy(alpha = 0.5f))
                ) {
                    Text("View Full Logs", fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF4CAF50)),
                    border = BorderStroke(1.dp, Color(0xFF4CAF50).copy(alpha = 0.5f))
                ) {
                    Text("Export Logs", fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFF44336)),
                    border = BorderStroke(1.dp, Color(0xFFF44336).copy(alpha = 0.5f))
                ) {
                    Text("Clear Logs", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun SystemLogItem(log: LogEntry) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            log.timestamp,
            color = Color.Gray,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.width(60.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = log.levelColor.copy(alpha = 0.2f)),
            modifier = Modifier.width(50.dp)
        ) {
            Text(
                log.level,
                color = log.levelColor,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            log.message,
            color = Color.White,
            fontSize = 11.sp,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun SettingsFooter() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Settings Information",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SettingsInfoItem("Configuration Version", "v2.4.1")
                SettingsInfoItem("Settings Format", "JSON")
                SettingsInfoItem("Last Modified", "2025-06-17 16:23:46 UTC")
                SettingsInfoItem("Modified By", "Alims-Repo")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF4CAF50)),
                    border = BorderStroke(1.dp, Color(0xFF4CAF50).copy(alpha = 0.5f))
                ) {
                    Icon(
                        Icons.Default.Save,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save All Settings", fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF9800)),
                    border = BorderStroke(1.dp, Color(0xFFFF9800).copy(alpha = 0.5f))
                ) {
                    Icon(
                        Icons.Default.RestoreFromTrash,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Reset to Defaults", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun SettingsInfoItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            label,
            color = Color.Gray,
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
        Text(
            value,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SettingItem(
    title: String,
    description: String,
    value: Boolean,
    onValueChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                description,
                color = Color.Gray,
                fontSize = 10.sp
            )
        }

        Switch(
            checked = value,
            onCheckedChange = onValueChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF4CAF50),
                checkedTrackColor = Color(0xFF1B5E20)
            ),
            modifier = Modifier.scale(0.8f)
        )
    }
}

// Data classes for Settings
data class LogEntry(
    val timestamp: String,
    val level: String,
    val message: String,
    val levelColor: Color
)