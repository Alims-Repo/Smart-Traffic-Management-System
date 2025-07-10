package com.gub.domain.models.dashboard

import kotlinx.serialization.Serializable

@Serializable
data class ModelLiveTraffic(
    val vehicle: Vehicle = Vehicle(),
    val congestion: Congestion = Congestion()
) {

    @Serializable
    data class Vehicle(
        val count: Int = 0,
        val difference: Int = 0,
        val upWards: Boolean = true
    )

    @Serializable
    data class Congestion(
        val count: Int = 0,
        val difference: Int = 0,
        val upWards: Boolean = true
    )
}
