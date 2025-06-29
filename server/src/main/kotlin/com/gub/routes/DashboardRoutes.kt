package com.gub.routes

import com.gub.models.dashboard.overview.ModelSystemOverview
import com.gub.models.dashboard.overview.ModelWeather
import com.gub.services.SystemOverviewService
import com.gub.services.WeatherService
import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.dashboardRoutes() {

    val systemService = SystemOverviewService(
        WeatherService("34ba7300025929e59b1fbcb711310dae")
    )

    routing {
        get("/api/system/overview") {
            runCatching {
                val overview = systemService.getSystemOverview()
                call.respond(overview)
            }
        }
    }
}