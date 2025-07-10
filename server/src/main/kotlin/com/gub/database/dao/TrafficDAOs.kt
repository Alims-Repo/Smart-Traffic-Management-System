package com.gub.database.dao

import com.gub.database.tables.*
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

/**
 * DAO for Intersection entities
 */
class IntersectionDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<IntersectionDAO>(Intersections)
    
    var name by Intersections.name
    var latitude by Intersections.latitude
    var longitude by Intersections.longitude
    var description by Intersections.description
    var isActive by Intersections.isActive
    var createdAt by Intersections.createdAt
}

/**
 * DAO for TrafficData entities
 */
class TrafficDataDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TrafficDataDAO>(TrafficData)
    
    var intersection by IntersectionDAO referencedOn TrafficData.intersectionId
    var timestamp by TrafficData.timestamp
    var vehicleCount by TrafficData.vehicleCount
    var averageSpeed by TrafficData.averageSpeed
    var congestionLevel by TrafficData.congestionLevel
    var temperature by TrafficData.temperature
    var humidity by TrafficData.humidity
    var visibility by TrafficData.visibility
    var createdAt by TrafficData.createdAt
}

/**
 * DAO for TrafficAnalytics entities
 */
class TrafficAnalyticsDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TrafficAnalyticsDAO>(TrafficAnalytics)
    
    var intersection by IntersectionDAO referencedOn TrafficAnalytics.intersectionId
    var timestamp by TrafficAnalytics.timestamp
    var totalVehicles by TrafficAnalytics.totalVehicles
    var averageSpeed by TrafficAnalytics.averageSpeed
    var congestionLevel by TrafficAnalytics.congestionLevel
    var incidentCount by TrafficAnalytics.incidentCount
    var peakHourIntensity by TrafficAnalytics.peakHourIntensity
    var aiOptimizedTiming by TrafficAnalytics.aiOptimizedTiming
    var periodType by TrafficAnalytics.periodType
    var createdAt by TrafficAnalytics.createdAt
}

/**
 * DAO for PeakHours entities
 */
class PeakHoursDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PeakHoursDAO>(PeakHours)
    
    var intersection by IntersectionDAO referencedOn PeakHours.intersectionId
    var startTime by PeakHours.startTime
    var endTime by PeakHours.endTime
    var period by PeakHours.period
    var intensity by PeakHours.intensity
    var isActive by PeakHours.isActive
    var peakType by PeakHours.peakType
    var averageVolume by PeakHours.averageVolume
    var createdAt by PeakHours.createdAt
}

/**
 * DAO for TrafficIncidents entities
 */
class TrafficIncidentDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TrafficIncidentDAO>(TrafficIncidents)
    
    var intersection by IntersectionDAO referencedOn TrafficIncidents.intersectionId
    var incidentType by TrafficIncidents.incidentType
    var severity by TrafficIncidents.severity
    var description by TrafficIncidents.description
    var startTime by TrafficIncidents.startTime
    var endTime by TrafficIncidents.endTime
    var isResolved by TrafficIncidents.isResolved
    var affectedLanes by TrafficIncidents.affectedLanes
    var estimatedDelay by TrafficIncidents.estimatedDelay
    var createdAt by TrafficIncidents.createdAt
}

/**
 * DAO for TrafficPredictions entities
 */
class TrafficPredictionDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TrafficPredictionDAO>(TrafficPredictions)
    
    var intersection by IntersectionDAO referencedOn TrafficPredictions.intersectionId
    var predictionTimestamp by TrafficPredictions.predictionTimestamp
    var predictedVehicleCount by TrafficPredictions.predictedVehicleCount
    var predictedAverageSpeed by TrafficPredictions.predictedAverageSpeed
    var predictedCongestionLevel by TrafficPredictions.predictedCongestionLevel
    var confidence by TrafficPredictions.confidence
    var predictionModel by TrafficPredictions.predictionModel
    var createdAt by TrafficPredictions.createdAt
}