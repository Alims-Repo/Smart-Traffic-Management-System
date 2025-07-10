package com.gub.domain.models.dashboard

import kotlinx.serialization.Serializable

@Serializable
data class ModelAiControl(
    val efficiency: Double = 0.0,
    val runningModel: Int = 0,
    val decisionSpeed: Int = 0
)