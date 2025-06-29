package com.gub.features.analytics.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gub.core.domain.Response
import com.gub.features.analytics.domain.model.*
import com.gub.models.analytics.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class ViewModelAnalytics : ViewModel() {

    private val _predictiveAnalytics = MutableStateFlow<Response<List<ModelPrediction>>>(Response.Loading)
    val predictiveAnalytics: StateFlow<Response<List<ModelPrediction>>> = _predictiveAnalytics.asStateFlow()

    private val _peakHourData = MutableStateFlow<Response<List<PeakHourData>>>(Response.Loading)
    val peakHourData: StateFlow<Response<List<PeakHourData>>> = _peakHourData.asStateFlow()

    private val _trafficVolumeData = MutableStateFlow<Response<List<TrafficVolumeData>>>(Response.Loading)
    val trafficVolumeData: StateFlow<Response<List<TrafficVolumeData>>> = _trafficVolumeData.asStateFlow()

    private val _chartData = MutableStateFlow<Response<List<ChartDataPoint>>>(Response.Loading)
    val chartData: StateFlow<Response<List<ChartDataPoint>>> = _chartData.asStateFlow()

    private val _exportInfo = MutableStateFlow<Response<ExportInfo>>(Response.Loading)
    val exportInfo: StateFlow<Response<ExportInfo>> = _exportInfo.asStateFlow()

    private val _analyticsMetrics = MutableStateFlow<Response<AnalyticsMetrics>>(Response.Loading)
    val analyticsMetrics: StateFlow<Response<AnalyticsMetrics>> = _analyticsMetrics.asStateFlow()

    private val _filterOptions = MutableStateFlow(
        FilterOptions(
            timeRange = TimeRange.LAST_24H,
            intersectionFilter = IntersectionFilter.ALL,
            showAdvancedFilters = false
        )
    )
    val filterOptions: StateFlow<FilterOptions> = _filterOptions.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    // Current time: 2025-06-29 05:24:38 UTC
    private val currentTimeMillis = 1750851878000L // 2025-06-29 05:24:38 UTC in milliseconds

    init {
        loadInitialData()
        startAutoRefresh()
    }

    private fun loadInitialData() {
        loadPredictiveAnalytics()
        loadPeakHourData()
        loadTrafficVolumeData()
        loadChartData()
        loadExportInfo()
        loadAnalyticsMetrics()
    }

    private fun loadPredictiveAnalytics() {
        viewModelScope.launch {
            try {
                delay(1500L)
                _predictiveAnalytics.value = Response.Success(
                    listOf(
                        ModelPrediction(
                            title = "Morning Rush Prediction",
                            prediction = "Peak starting at 07:30",
                            confidence = 94,
                            impact = "High volume expected",
                            isActive = false
                        ),
                        ModelPrediction(
                            title = "Weather Impact",
                            prediction = "Clear conditions today",
                            confidence = 87,
                            impact = "Normal traffic flow"
                        ),
                        ModelPrediction(
                            title = "Incident Probability",
                            prediction = "Low risk next 6 hours",
                            confidence = 91,
                            impact = "Normal operations"
                        ),
                        ModelPrediction(
                            title = "Signal Optimization",
                            prediction = "AI adjusting for early traffic",
                            confidence = 96,
                            impact = "5% efficiency boost"
                        )
                    )
                )
            } catch (e: Exception) {
                _predictiveAnalytics.value = Response.Error("Failed to load predictions: ${e.message}")
            }
        }
    }

    private fun loadPeakHourData() {
        viewModelScope.launch {
            try {
                delay(1000L)
                val currentData = listOf(
                    PeakHourData("Early Morning", "05:00 - 06:30", 25, true, PeakType.CURRENT),
                    PeakHourData("Morning Rush", "07:30 - 09:00", 85, false, PeakType.PREDICTED),
                    PeakHourData("Lunch Period", "12:00 - 13:30", 65, false, PeakType.PREDICTED)
                )
                _peakHourData.value = Response.Success(currentData)
            } catch (e: Exception) {
                _peakHourData.value = Response.Error("Failed to load peak hour data: ${e.message}")
            }
        }
    }

    private fun loadChartData() {
        viewModelScope.launch {
            try {
                delay(700L)
                val chartPoints = mutableListOf<ChartDataPoint>()
                val currentHour = 5 // Current time is 05:24

                // Generate 24 hours of data starting from midnight
                for (hour in 0..23) {
                    val timestamp = currentTimeMillis - (currentHour - hour) * 3600000L
                    val baseValue = when (hour) {
                        in 0..5 -> Random.nextInt(150, 300) // Very early morning
                        in 6..9 -> Random.nextInt(800, 1200) // Morning rush
                        in 10..11 -> Random.nextInt(400, 600) // Mid morning
                        in 12..13 -> Random.nextInt(600, 900) // Lunch
                        in 14..16 -> Random.nextInt(500, 700) // Afternoon
                        in 17..19 -> Random.nextInt(900, 1400) // Evening rush
                        in 20..23 -> Random.nextInt(300, 500) // Evening
                        else -> Random.nextInt(200, 400)
                    }

                    chartPoints.add(
                        ChartDataPoint(
                            hour = hour,
                            value = if (hour <= currentHour) baseValue else baseValue + Random.nextInt(-50, 100), // Past vs predicted
                            prediction = hour > currentHour, // Future hours are predictions
                            timestamp = timestamp,
                            label = String.format("%02d:00", hour),
                            color = if (hour > currentHour) "#FF9800" else "#2196F3"
                        )
                    )
                }

                _chartData.value = Response.Success(chartPoints)
            } catch (e: Exception) {
                _chartData.value = Response.Error("Failed to load chart data: ${e.message}")
            }
        }
    }

    private fun loadTrafficVolumeData() {
        viewModelScope.launch {
            try {
                delay(800L)
                val data = generateMockTrafficData()
                _trafficVolumeData.value = Response.Success(data)
            } catch (e: Exception) {
                _trafficVolumeData.value = Response.Error("Failed to load traffic data: ${e.message}")
            }
        }
    }

    private fun loadExportInfo() {
        viewModelScope.launch {
            try {
                delay(500L)
                _exportInfo.value = Response.Success(
                    ExportInfo(
                        lastExportTime = "2025-06-29 04:30 UTC",
                        autoExportEnabled = true,
                        nextScheduledExport = "2025-06-30 00:00 UTC"
                    )
                )
            } catch (e: Exception) {
                _exportInfo.value = Response.Error("Failed to load export info: ${e.message}")
            }
        }
    }

    private fun loadAnalyticsMetrics() {
        viewModelScope.launch {
            try {
                delay(600L)
                _analyticsMetrics.value = Response.Success(
                    AnalyticsMetrics(
                        totalVehicles = Random.nextInt(200, 400), // Early morning traffic
                        averageSpeed = Random.nextDouble(55.0, 75.0).toFloat(), // Higher speeds at this time
                        congestionLevel = Random.nextDouble(0.1, 0.3).toFloat(), // Low congestion
                        incidentCount = Random.nextInt(0, 2), // Few incidents at this hour
                        timestamp = currentTimeMillis
                    )
                )
            } catch (e: Exception) {
                _analyticsMetrics.value = Response.Error("Failed to load metrics: ${e.message}")
            }
        }
    }

    private fun generateMockTrafficData(): List<TrafficVolumeData> {
        val data = mutableListOf<TrafficVolumeData>()

        for (i in 0..23) {
            val timestamp = currentTimeMillis - (5 - i) * 3600000L // Relative to current hour (5)
            val baseVolume = when (i) {
                in 0..5 -> Random.nextInt(150, 300)
                in 6..9 -> Random.nextInt(800, 1200)
                in 10..11 -> Random.nextInt(400, 600)
                in 12..13 -> Random.nextInt(600, 900)
                in 14..16 -> Random.nextInt(500, 700)
                in 17..19 -> Random.nextInt(900, 1400)
                in 20..23 -> Random.nextInt(300, 500)
                else -> Random.nextInt(200, 400)
            }

            data.add(
                TrafficVolumeData(
                    timestamp = timestamp,
                    volume = baseVolume,
                    averageSpeed = Random.nextDouble(25.0, 75.0).toFloat(),
                    congestionLevel = when {
                        baseVolume > 1000 -> CongestionLevel.HIGH
                        baseVolume > 600 -> CongestionLevel.MEDIUM
                        baseVolume > 300 -> CongestionLevel.LOW
                        else -> CongestionLevel.LOW
                    }
                )
            )
        }
        return data
    }

    fun updateTimeRange(timeRange: TimeRange) {
        _filterOptions.value = _filterOptions.value.copy(timeRange = timeRange)
        refreshData()
    }

    fun updateIntersectionFilter(filter: IntersectionFilter) {
        _filterOptions.value = _filterOptions.value.copy(intersectionFilter = filter)
        refreshData()
    }

    fun toggleAdvancedFilters() {
        _filterOptions.value = _filterOptions.value.copy(
            showAdvancedFilters = !_filterOptions.value.showAdvancedFilters
        )
    }

    fun refreshData() {
        viewModelScope.launch {
            _isRefreshing.value = true
            loadInitialData()
            delay(1000L)
            _isRefreshing.value = false
        }
    }

    fun exportData(exportType: String) {
        viewModelScope.launch {
            try {
                val currentExportInfo = _exportInfo.value
                if (currentExportInfo is Response.Success) {
                    _exportInfo.value = Response.Success(
                        currentExportInfo.data.copy(isExporting = true)
                    )

                    delay(3000L)

                    _exportInfo.value = Response.Success(
                        currentExportInfo.data.copy(
                            isExporting = false,
                            lastExportTime = "2025-06-29 05:24 UTC"
                        )
                    )
                }
            } catch (e: Exception) {
                _exportInfo.value = Response.Error("Export failed: ${e.message}")
            }
        }
    }

    private fun startAutoRefresh() {
        viewModelScope.launch {
            while (true) {
                delay(30000L)
                loadAnalyticsMetrics()
                loadChartData()
            }
        }
    }
}