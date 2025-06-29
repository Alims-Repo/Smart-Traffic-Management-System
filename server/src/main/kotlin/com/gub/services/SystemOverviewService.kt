package com.gub.services

import com.gub.models.dashboard.overview.ModelSystemOverview
import com.gub.models.dashboard.overview.ModelWeather
import kotlin.random.Random

class SystemOverviewService(
    private val weatherService: WeatherService
) {

    private var lastLoad = 0L
    private var weather = ModelWeather()

    suspend fun getSystemOverview(): ModelSystemOverview {
        if (lastLoad < (System.currentTimeMillis() - (1 * 60 * 60 * 1000))) {
            lastLoad = System.currentTimeMillis()
            weather = weatherService.getCurrentWeather()
        }
        return ModelSystemOverview(
            systemHealth = Random.nextDouble(85.0, 99.9).round(1),
            aiResponseTime = Random.nextDouble(20.0, 50.0).round(2),
            avgWaitTime = Random.nextDouble(0.5, 3.0).round(1),
            currentFlow = Random.nextDouble(100.0, 500.0).round(1),
            weather = weather //weatherService.getCurrentWeather()
        )
    }

    private fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return kotlin.math.round(this * multiplier) / multiplier
    }
}