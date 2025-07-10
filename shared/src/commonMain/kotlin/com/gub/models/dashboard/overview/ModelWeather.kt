package com.gub.models.dashboard.overview

import kotlinx.serialization.Serializable

@Serializable
data class ModelWeather(
    val temp: Double = 0.0,
    val tempUnit: TempUnit = TempUnit.CELSIUS,
    val wind: Double = 0.0,
    val humidity: Double = 0.0,
    val visibility: Double = 0.0,
) {

    enum class TempUnit {
        CELSIUS,
        FAHRENHEIT;

        fun toSymbol(): String {
            return when (this) {
                CELSIUS -> "°C"
                FAHRENHEIT -> "°F"
            }
        }
    }
}
