package com.gub.routes

import com.gub.application.ServiceModule
import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Route.dashboardRoute() {

    route("dashboard") {
        get("overview") {
            return@get call.respond(ServiceModule.useCaseSystemOverview.invoke())
        }

        get("live-matrics") {
            return@get call.respond(ServiceModule.useCaseLiveTraffic.invoke())
        }

        get("ai-control") {

        }
    }
}