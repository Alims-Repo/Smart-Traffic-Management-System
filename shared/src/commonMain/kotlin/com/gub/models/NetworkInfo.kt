package com.gub.models

import kotlinx.serialization.Serializable

@Serializable
data class NetworkInfo(
    val name: String,
    val bytesSent: Long,
    val bytesRecv: Long
)