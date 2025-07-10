package com.gub.data.database

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object AiControlMetrics : LongIdTable("ai_control_metrics") {
    val efficiency = double("efficiency")
    val runningModel = integer("running_model")
    val decisionSpeed = integer("decision_speed")
    val timestamp = datetime("timestamp").default(LocalDateTime.now())
}

object TrafficMetrics : LongIdTable("traffic_metrics") {
    val vehicleCount = integer("vehicle_count")
    val vehicleDifference = integer("vehicle_difference")
    val vehicleUpwards = bool("vehicle_upwards")
    val congestionCount = integer("congestion_count")
    val congestionDifference = integer("congestion_difference")
    val congestionUpwards = bool("congestion_upwards")
    val timestamp = datetime("timestamp").default(LocalDateTime.now())
}

object SystemMetrics : LongIdTable("system_metrics") {
    val systemHealth = double("system_health")
    val aiResponseTime = double("ai_response_time")
    val avgWaitTime = double("avg_wait_time")
    val currentFlow = double("current_flow")
    val timestamp = datetime("timestamp").default(LocalDateTime.now())
}