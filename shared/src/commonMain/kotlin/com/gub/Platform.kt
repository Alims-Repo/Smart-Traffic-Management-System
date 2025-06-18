package com.gub

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform