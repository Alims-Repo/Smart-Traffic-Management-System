/**
 * Application theme configuration
 * Author: Alims-Repo
 * Date: 2025-06-17
 */
package com.gub.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Primary Colors
val PrimaryBlue = Color(0xFF2196F3)
val PrimaryBlueVariant = Color(0xFF1976D2)
val PrimaryBlueDark = Color(0xFF0D47A1)
val PrimaryBlueLight = Color(0xFF64B5F6)

// Secondary Colors
val SecondaryGreen = Color(0xFF4CAF50)
val SecondaryGreenVariant = Color(0xFF2E7D32)
val SecondaryGreenDark = Color(0xFF1B5E20)
val SecondaryGreenLight = Color(0xFF81C784)

// Accent Colors
val AccentTeal = Color(0xFF00BCD4)
val AccentTealVariant = Color(0xFF0097A7)
val AccentPurple = Color(0xFF9C27B0)
val AccentIndigo = Color(0xFF3F51B5)

// Warning & Status Colors
val WarningOrange = Color(0xFFFF9800)
val WarningOrangeVariant = Color(0xFFF57C00)
val ErrorRed = Color(0xFFF44336)
val ErrorRedVariant = Color(0xFFD32F2F)
val SuccessGreen = Color(0xFF4CAF50)
val InfoBlue = Color(0xFF2196F3)

// Traffic Status Colors
val TrafficOptimal = Color(0xFF4CAF50)
val TrafficModerate = Color(0xFFFF9800)
val TrafficCongested = Color(0xFFF44336)
val TrafficCritical = Color(0xFF9C27B0)
val TrafficUnknown = Color(0xFF9E9E9E)

// AI Status Colors
val AIActive = Color(0xFF4CAF50)
val AILearning = Color(0xFF2196F3)
val AIOptimizing = Color(0xFFFF9800)
val AIError = Color(0xFFF44336)

// Dark Theme Colors
val BackgroundDark = Color(0xFF0D1117)
val OnBackgroundDark = Color(0xFFE6EDF3)
val SurfaceDark = Color(0xFF161B22)
val OnSurfaceDark = Color(0xFFE6EDF3)
val CardBackgroundDark = Color(0xFF21262D)
val CardBackgroundDarkVariant = Color(0xFF30363D)

// Light Theme Colors
val BackgroundLight = Color(0xFFFAFBFC)
val OnBackgroundLight = Color(0xFF24292F)
val SurfaceLight = Color(0xFFFFFFFF)
val OnSurfaceLight = Color(0xFF24292F)
val CardBackgroundLight = Color(0xFFF6F8FA)
val CardBackgroundLightVariant = Color(0xFFEAECEF)

// Gray Scale
val Gray50 = Color(0xFFFAFAFA)
val Gray100 = Color(0xFFF5F5F5)
val Gray200 = Color(0xFFEEEEEE)
val Gray300 = Color(0xFFE0E0E0)
val Gray400 = Color(0xFFBDBDBD)
val Gray500 = Color(0xFF9E9E9E)
val Gray600 = Color(0xFF757575)
val Gray700 = Color(0xFF616161)
val Gray800 = Color(0xFF424242)
val Gray900 = Color(0xFF212121)

// System UI Colors
val StatusBarDark = Color(0xFF000000)
val StatusBarLight = Color(0xFFFFFFFF)
val NavigationBarDark = Color(0xFF0D1117)
val NavigationBarLight = Color(0xFFFFFFFF)

// Special Colors for Traffic Management
val LiveIndicator = Color(0xFF00FF00)
val OfflineIndicator = Color(0xFFFF0000)
val MaintenanceIndicator = Color(0xFFFFFF00)

// Chart Colors
val ChartPrimary = Color(0xFF2196F3)
val ChartSecondary = Color(0xFF4CAF50)
val ChartTertiary = Color(0xFFFF9800)
val ChartQuaternary = Color(0xFF9C27B0)
val ChartBackground = Color(0xFF263238)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = PrimaryBlueVariant,
    onPrimaryContainer = Color.White,
    inversePrimary = PrimaryBlueLight,

    secondary = SecondaryGreen,
    onSecondary = Color.White,
    secondaryContainer = SecondaryGreenVariant,
    onSecondaryContainer = Color.White,

    tertiary = WarningOrange,
    onTertiary = Color.White,
    tertiaryContainer = WarningOrangeVariant,
    onTertiaryContainer = Color.White,

    background = BackgroundDark,
    onBackground = OnBackgroundDark,

    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = CardBackgroundDark,
    onSurfaceVariant = OnSurfaceDark,
    surfaceTint = PrimaryBlue,
    inverseSurface = SurfaceLight,
    inverseOnSurface = OnSurfaceLight,

    error = ErrorRed,
    onError = Color.White,
    errorContainer = ErrorRedVariant,
    onErrorContainer = Color.White,

    outline = Gray600,
    outlineVariant = Gray800,
    scrim = Color.Black.copy(alpha = 0.32f),

    surfaceBright = CardBackgroundDarkVariant,
    surfaceDim = Color(0xFF111418),
    surfaceContainer = CardBackgroundDark,
    surfaceContainerHigh = CardBackgroundDarkVariant,
    surfaceContainerHighest = Color(0xFF3A4048),
    surfaceContainerLow = Color(0xFF1A1F25),
    surfaceContainerLowest = Color(0xFF0F1419)
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE3F2FD),
    onPrimaryContainer = PrimaryBlueVariant,
    inversePrimary = PrimaryBlueDark,

    secondary = SecondaryGreen,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE8F5E8),
    onSecondaryContainer = SecondaryGreenVariant,

    tertiary = WarningOrange,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFF3E0),
    onTertiaryContainer = WarningOrangeVariant,

    background = BackgroundLight,
    onBackground = OnBackgroundLight,

    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = CardBackgroundLight,
    onSurfaceVariant = OnSurfaceLight,
    surfaceTint = PrimaryBlue,
    inverseSurface = SurfaceDark,
    inverseOnSurface = OnSurfaceDark,

    error = ErrorRed,
    onError = Color.White,
    errorContainer = Color(0xFFFFEBEE),
    onErrorContainer = ErrorRedVariant,

    outline = Gray400,
    outlineVariant = Gray200,
    scrim = Color.Black.copy(alpha = 0.32f),

    surfaceBright = Color.White,
    surfaceDim = Gray100,
    surfaceContainer = CardBackgroundLight,
    surfaceContainerHigh = Gray50,
    surfaceContainerHighest = Color.White,
    surfaceContainerLow = Gray100,
    surfaceContainerLowest = Color.White
)

@Composable
fun VehicleDetectionTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}