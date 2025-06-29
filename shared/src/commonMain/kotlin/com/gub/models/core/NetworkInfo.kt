package com.gub.models.core

import kotlinx.serialization.Serializable

@Serializable
data class NetworkInfo(
    val name: String,
    val bytesSent: Long,
    val bytesRecv: Long
)