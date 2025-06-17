package com.gub.core.domain

sealed class Response<out T> {

    data object Loading: Response<Nothing>()

    data class Success<T>(val data: T): Response<T>()

    data class Error(val error: String): Response<Nothing>()
}