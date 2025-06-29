package com.gub.features.dashboard.domain.extension

import com.gub.models.dashboard.overview.ModelSystemOverview
import java.util.Locale

fun ModelSystemOverview.systemHealthValue() = "${String.format(Locale.US, "%.1f", this.systemHealth)}%"

fun ModelSystemOverview.systemHealthSubText() = when {
    this.systemHealth >= 80 -> "Healthy"
    this.systemHealth >= 50 -> "Moderate"
    else -> "Critical"
}

fun ModelSystemOverview.weatherTempValue() = "${String.format(Locale.US, "%.1f", this.weather.temp)}${this.weather.tempUnit.toSymbol()}"

fun ModelSystemOverview.weatherTempSubText() = when {
    this.weather.temp < 0 -> "Freezing"
    this.weather.temp < 15 -> "Cold"
    this.weather.temp < 25 -> "Mild"
    this.weather.temp < 35 -> "Warm"
    else -> "Hot"
}

fun ModelSystemOverview.aiResponseTimeValue() = "${String.format(Locale.US, "%.1f", this.aiResponseTime)}ms"

fun ModelSystemOverview.aiResponseTimeSubText() = when {
    this.aiResponseTime < 1.0 -> "Excellent"
    this.aiResponseTime < 2.0 -> "Good"
    this.aiResponseTime < 3.0 -> "Average"
    else -> "Poor"
}

fun ModelSystemOverview.currentFlowValue() = "${String.format(Locale.US, "%.1f", this.currentFlow)}/hour"

fun ModelSystemOverview.currentFlowSubText() = when {
    this.currentFlow < 1.0 -> "Low"
    this.currentFlow < 2.0 -> "Moderate"
    this.currentFlow < 3.0 -> "High"
    else -> "Very High"
}

fun ModelSystemOverview.avgWaitTimeValue() = "${String.format(Locale.US, "%.1f", this.avgWaitTime)} minutes"

fun ModelSystemOverview.avgWaitTimeSubText() = when {
    this.avgWaitTime < 1.0 -> "Excellent"
    this.avgWaitTime < 2.0 -> "Good"
    this.avgWaitTime < 3.0 -> "Average"
    else -> "Poor"
}