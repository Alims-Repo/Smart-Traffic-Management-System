package com.gub.routes

import com.gub.application.ServiceModule
import com.gub.data.service.dashboard.ServiceDashboard
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.dashboardRoute() {

    val dashboardService by inject<ServiceDashboard>()

    route("/api/dashboard") {
        get("/ai-control") {
            val result = dashboardService.getAiControlStatus()
            call.respond(HttpStatusCode.OK, result)
        }

        get("/traffic") {
            val result = dashboardService.getLiveTrafficStatus()
            call.respond(HttpStatusCode.OK, result)
        }

        get("/system") {
            val result = dashboardService.getSystemOverview()
            call.respond(HttpStatusCode.OK, result)
        }

        get("/ai-control/history") {
            val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 100
            val result = dashboardService.getAiControlHistory(limit)
            call.respond(HttpStatusCode.OK, result)
        }

        post("/ai-control/update") {
            val params = call.receiveParameters()
            val efficiency = params["efficiency"]?.toDoubleOrNull() ?: return@post call.respond(HttpStatusCode.BadRequest)
            val runningModel = params["runningModel"]?.toIntOrNull() ?: return@post call.respond(HttpStatusCode.BadRequest)
            val decisionSpeed = params["decisionSpeed"]?.toIntOrNull() ?: return@post call.respond(HttpStatusCode.BadRequest)

            dashboardService.updateAiMetrics(efficiency, runningModel, decisionSpeed)
            call.respond(HttpStatusCode.OK, "AI metrics updated")
        }

        post("/cleanup") {
            val daysToKeep = call.request.queryParameters["days"]?.toIntOrNull() ?: 30
            dashboardService.performDataCleanup(daysToKeep)
            call.respond(HttpStatusCode.OK, "Data cleanup completed")
        }
    }
}