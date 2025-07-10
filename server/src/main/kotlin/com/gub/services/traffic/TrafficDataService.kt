package com.gub.services.traffic

import com.gub.database.dao.*
import com.gub.database.tables.*
import com.gub.models.traffic.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Service for managing traffic data operations
 */
class TrafficDataService {
    
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    
    /**
     * Convert shared model enum to database enum
     */
    private fun convertCongestionLevel(level: CongestionLevel): DbCongestionLevel {
        return when (level) {
            CongestionLevel.LOW -> DbCongestionLevel.LOW
            CongestionLevel.MEDIUM -> DbCongestionLevel.MEDIUM
            CongestionLevel.HIGH -> DbCongestionLevel.HIGH
            CongestionLevel.CRITICAL -> DbCongestionLevel.CRITICAL
        }
    }
    
    /**
     * Convert database enum to shared model enum
     */
    private fun convertDbCongestionLevel(level: DbCongestionLevel): CongestionLevel {
        return when (level) {
            DbCongestionLevel.LOW -> CongestionLevel.LOW
            DbCongestionLevel.MEDIUM -> CongestionLevel.MEDIUM
            DbCongestionLevel.HIGH -> CongestionLevel.HIGH
            DbCongestionLevel.CRITICAL -> CongestionLevel.CRITICAL
        }
    }
    
    /**
     * Convert shared model enum to database enum
     */
    private fun convertPeriodType(type: PeriodType): DbPeriodType {
        return when (type) {
            PeriodType.HOURLY -> DbPeriodType.HOURLY
            PeriodType.DAILY -> DbPeriodType.DAILY
            PeriodType.WEEKLY -> DbPeriodType.WEEKLY
            PeriodType.MONTHLY -> DbPeriodType.MONTHLY
        }
    }
    
    /**
     * Convert database enum to shared model enum
     */
    private fun convertDbPeriodType(type: DbPeriodType): PeriodType {
        return when (type) {
            DbPeriodType.HOURLY -> PeriodType.HOURLY
            DbPeriodType.DAILY -> PeriodType.DAILY
            DbPeriodType.WEEKLY -> PeriodType.WEEKLY
            DbPeriodType.MONTHLY -> PeriodType.MONTHLY
        }
    }
    
    /**
     * Create new traffic data entry
     */
    fun createTrafficData(request: CreateTrafficDataRequest): TrafficDataModel {
        return transaction {
            val intersection = IntersectionDAO.findById(request.intersectionId)
                ?: throw IllegalArgumentException("Intersection not found: ${request.intersectionId}")
            
            val trafficData = TrafficDataDAO.new {
                this.intersection = intersection
                this.timestamp = LocalDateTime.now()
                this.vehicleCount = request.vehicleCount
                this.averageSpeed = BigDecimal(request.averageSpeed.toString())
                this.congestionLevel = convertCongestionLevel(request.congestionLevel)
                this.temperature = request.temperature?.let { BigDecimal(it.toString()) }
                this.humidity = request.humidity
                this.visibility = request.visibility?.let { BigDecimal(it.toString()) }
                this.createdAt = LocalDateTime.now()
            }
            
            convertTrafficDataToModel(trafficData)
        }
    }
    
    /**
     * Get traffic data by intersection ID
     */
    fun getTrafficDataByIntersection(intersectionId: Int, limit: Int = 100): List<TrafficDataModel> {
        return transaction {
            TrafficDataDAO.find { TrafficData.intersectionId eq intersectionId }
                .orderBy(TrafficData.timestamp to SortOrder.DESC)
                .limit(limit)
                .map { convertTrafficDataToModel(it) }
        }
    }
    
    /**
     * Get latest traffic data for all intersections
     */
    fun getLatestTrafficData(): List<TrafficDataModel> {
        return transaction {
            TrafficDataDAO.all()
                .orderBy(TrafficData.timestamp to SortOrder.DESC)
                .limit(50)
                .map { convertTrafficDataToModel(it) }
        }
    }
    
    /**
     * Get traffic data within time range
     */
    fun getTrafficDataByTimeRange(
        intersectionId: Int,
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ): List<TrafficDataModel> {
        return transaction {
            TrafficDataDAO.find { 
                (TrafficData.intersectionId eq intersectionId) and 
                (TrafficData.timestamp greaterEq startTime) and 
                (TrafficData.timestamp lessEq endTime) 
            }.orderBy(TrafficData.timestamp to SortOrder.DESC)
                .map { convertTrafficDataToModel(it) }
        }
    }
    
    /**
     * Create traffic analytics entry
     */
    fun createTrafficAnalytics(request: CreateTrafficAnalyticsRequest): TrafficAnalyticsModel {
        return transaction {
            val intersection = IntersectionDAO.findById(request.intersectionId)
                ?: throw IllegalArgumentException("Intersection not found: ${request.intersectionId}")
            
            val analytics = TrafficAnalyticsDAO.new {
                this.intersection = intersection
                this.timestamp = LocalDateTime.now()
                this.totalVehicles = request.totalVehicles
                this.averageSpeed = BigDecimal(request.averageSpeed.toString())
                this.congestionLevel = BigDecimal(request.congestionLevel.toString())
                this.incidentCount = request.incidentCount
                this.peakHourIntensity = request.peakHourIntensity?.let { BigDecimal(it.toString()) }
                this.aiOptimizedTiming = request.aiOptimizedTiming
                this.periodType = convertPeriodType(request.periodType)
                this.createdAt = LocalDateTime.now()
            }
            
            convertTrafficAnalyticsToModel(analytics)
        }
    }
    
    /**
     * Get traffic analytics by intersection and period
     */
    fun getTrafficAnalytics(
        intersectionId: Int,
        periodType: PeriodType,
        limit: Int = 50
    ): List<TrafficAnalyticsModel> {
        return transaction {
            TrafficAnalyticsDAO.find { 
                (TrafficAnalytics.intersectionId eq intersectionId) and 
                (TrafficAnalytics.periodType eq convertPeriodType(periodType))
            }.orderBy(TrafficAnalytics.timestamp to SortOrder.DESC)
                .limit(limit)
                .map { convertTrafficAnalyticsToModel(it) }
        }
    }
    
    /**
     * Get all intersections
     */
    fun getAllIntersections(): List<IntersectionModel> {
        return transaction {
            IntersectionDAO.all()
                .filter { it.isActive }
                .map { convertIntersectionToModel(it) }
        }
    }
    
    /**
     * Get intersection by ID
     */
    fun getIntersectionById(id: Int): IntersectionModel? {
        return transaction {
            IntersectionDAO.findById(id)?.let { convertIntersectionToModel(it) }
        }
    }
    
    /**
     * Convert DAO to model
     */
    private fun convertTrafficDataToModel(dao: TrafficDataDAO): TrafficDataModel {
        return TrafficDataModel(
            id = dao.id.value,
            intersectionId = dao.intersection.id.value,
            timestamp = dao.timestamp.format(dateFormatter),
            vehicleCount = dao.vehicleCount,
            averageSpeed = dao.averageSpeed.toDouble(),
            congestionLevel = convertDbCongestionLevel(dao.congestionLevel),
            temperature = dao.temperature?.toDouble(),
            humidity = dao.humidity,
            visibility = dao.visibility?.toDouble(),
            createdAt = dao.createdAt.format(dateFormatter)
        )
    }
    
    /**
     * Convert analytics DAO to model
     */
    private fun convertTrafficAnalyticsToModel(dao: TrafficAnalyticsDAO): TrafficAnalyticsModel {
        return TrafficAnalyticsModel(
            id = dao.id.value,
            intersectionId = dao.intersection.id.value,
            timestamp = dao.timestamp.format(dateFormatter),
            totalVehicles = dao.totalVehicles,
            averageSpeed = dao.averageSpeed.toDouble(),
            congestionLevel = dao.congestionLevel.toDouble(),
            incidentCount = dao.incidentCount,
            peakHourIntensity = dao.peakHourIntensity?.toDouble(),
            aiOptimizedTiming = dao.aiOptimizedTiming,
            periodType = convertDbPeriodType(dao.periodType),
            createdAt = dao.createdAt.format(dateFormatter)
        )
    }
    
    /**
     * Convert intersection DAO to model
     */
    private fun convertIntersectionToModel(dao: IntersectionDAO): IntersectionModel {
        return IntersectionModel(
            id = dao.id.value,
            name = dao.name,
            latitude = dao.latitude.toDouble(),
            longitude = dao.longitude.toDouble(),
            description = dao.description,
            isActive = dao.isActive,
            createdAt = dao.createdAt.format(dateFormatter)
        )
    }
}