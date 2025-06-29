package com.gub.models.analytics

data class ModelPrediction(
    val title: String,
    val prediction: String,
    val confidence: Int,
    val impact: String,
    val isActive: Boolean = false
)