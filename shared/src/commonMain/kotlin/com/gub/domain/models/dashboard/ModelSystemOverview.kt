package com.gub.domain.models.dashboard

import com.gub.models.dashboard.overview.ModelWeather
import kotlinx.serialization.Serializable

@Serializable
data class ModelSystemOverview(
    val systemHealth: Double = 0.0,
    val aiResponseTime: Double = 0.0,
    val avgWaitTime: Double = 0.0,
    val currentFlow: Double = 0.0,
    val weather: ModelWeather = ModelWeather()
)