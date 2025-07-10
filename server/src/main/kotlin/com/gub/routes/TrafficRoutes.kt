package com.gub.routes

import com.gub.models.traffic.CreateTrafficDataRequest
import com.gub.models.traffic.CreateTrafficAnalyticsRequest
import com.gub.models.traffic.PeriodType
import com.gub.services.traffic.TrafficDataService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * API routes for traffic data operations
 */
fun Application.trafficRoutes() {
    val trafficService = TrafficDataService()
    
    routing {
        route("/api/traffic") {
            
            // Get all intersections
            get("/intersections") {
                try {
                    val intersections = trafficService.getAllIntersections()
                    call.respond(HttpStatusCode.OK, intersections)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
                }
            }
            
            // Get intersection by ID
            get("/intersections/{id}") {
                try {
                    val id = call.parameters["id"]?.toIntOrNull()
                    if (id == null) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid intersection ID"))
                        return@get
                    }
                    
                    val intersection = trafficService.getIntersectionById(id)
                    if (intersection == null) {
                        call.respond(HttpStatusCode.NotFound, mapOf("error" to "Intersection not found"))
                        return@get
                    }
                    
                    call.respond(HttpStatusCode.OK, intersection)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
                }
            }
            
            // Create new traffic data
            post("/data") {
                try {
                    val request = call.receive<CreateTrafficDataRequest>()
                    val trafficData = trafficService.createTrafficData(request)
                    call.respond(HttpStatusCode.Created, trafficData)
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
                }
            }
            
            // Get traffic data by intersection
            get("/data/intersection/{id}") {
                try {
                    val id = call.parameters["id"]?.toIntOrNull()
                    if (id == null) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid intersection ID"))
                        return@get
                    }
                    
                    val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 100
                    val trafficData = trafficService.getTrafficDataByIntersection(id, limit)
                    call.respond(HttpStatusCode.OK, trafficData)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
                }
            }
            
            // Get latest traffic data for all intersections
            get("/data/latest") {
                try {
                    val trafficData = trafficService.getLatestTrafficData()
                    call.respond(HttpStatusCode.OK, trafficData)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
                }
            }
            
            // Get traffic data by time range
            get("/data/intersection/{id}/range") {
                try {
                    val id = call.parameters["id"]?.toIntOrNull()
                    if (id == null) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid intersection ID"))
                        return@get
                    }
                    
                    val startTimeStr = call.request.queryParameters["start"]
                    val endTimeStr = call.request.queryParameters["end"]
                    
                    if (startTimeStr == null || endTimeStr == null) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to "start and end time parameters are required"))
                        return@get
                    }
                    
                    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
                    val startTime = LocalDateTime.parse(startTimeStr, formatter)
                    val endTime = LocalDateTime.parse(endTimeStr, formatter)
                    
                    val trafficData = trafficService.getTrafficDataByTimeRange(id, startTime, endTime)
                    call.respond(HttpStatusCode.OK, trafficData)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
                }
            }
            
            // Create traffic analytics
            post("/analytics") {
                try {
                    val request = call.receive<CreateTrafficAnalyticsRequest>()
                    val analytics = trafficService.createTrafficAnalytics(request)
                    call.respond(HttpStatusCode.Created, analytics)
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
                }
            }
            
            // Get traffic analytics
            get("/analytics/intersection/{id}") {
                try {
                    val id = call.parameters["id"]?.toIntOrNull()
                    if (id == null) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid intersection ID"))
                        return@get
                    }
                    
                    val periodTypeStr = call.request.queryParameters["period"] ?: "HOURLY"
                    val periodType = try {
                        PeriodType.valueOf(periodTypeStr.uppercase())
                    } catch (e: IllegalArgumentException) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid period type"))
                        return@get
                    }
                    
                    val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 50
                    val analytics = trafficService.getTrafficAnalytics(id, periodType, limit)
                    call.respond(HttpStatusCode.OK, analytics)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
                }
            }
        }
    }
}