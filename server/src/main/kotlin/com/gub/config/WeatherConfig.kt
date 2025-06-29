package com.gub.config

data class WeatherConfig(
    val apiKey: String = "",
    val defaultCity: String = "London",
    val defaultCountryCode: String = "GB",
    val cacheExpirationMinutes: Long = 10
)