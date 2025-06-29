package com.gub.features.analytics.domain.model

data class TrafficVolumeData(
    val timestamp: Long,
    val volume: Int,
    val averageSpeed: Float,
    val congestionLevel: CongestionLevel
)

data class ChartDataPoint(
    val hour: Int,
    val value: Int,
    val prediction: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val label: String = "",
    val color: String = "#2196F3"
)

data class PeakHourData(
    val period: String,
    val timeRange: String,
    val intensity: Int,
    val isActive: Boolean,
    val type: PeakType
)

data class ExportInfo(
    val lastExportTime: String,
    val autoExportEnabled: Boolean,
    val nextScheduledExport: String,
    val isExporting: Boolean = false
)

data class AnalyticsMetrics(
    val totalVehicles: Int,
    val averageSpeed: Float,
    val congestionLevel: Float,
    val incidentCount: Int,
    val timestamp: Long
)

enum class CongestionLevel {
    LOW, MEDIUM, HIGH, CRITICAL
}

enum class PeakType {
    CURRENT, PREDICTED, HISTORICAL
}

data class FilterOptions(
    val timeRange: TimeRange,
    val intersectionFilter: IntersectionFilter,
    val showAdvancedFilters: Boolean
)

enum class TimeRange(val displayName: String, val hours: Int) {
    LAST_HOUR("Last Hour", 1),
    LAST_6H("Last 6H", 6),
    LAST_24H("Last 24H", 24),
    LAST_WEEK("Last Week", 168),
    LAST_MONTH("Last Month", 720)
}

enum class IntersectionFilter(val displayName: String) {
    ALL("All Intersections"),
    CRITICAL("Critical Only"),
    AI_OPTIMIZED("AI Optimized")
}