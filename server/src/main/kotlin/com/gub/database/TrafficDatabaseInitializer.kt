package com.gub.database

import com.gub.database.dao.IntersectionDAO
import com.gub.database.tables.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * Database schema initialization for traffic management system
 */
object TrafficDatabaseInitializer {
    
    /**
     * Create all traffic-related tables
     */
    fun createTables() {
        transaction {
            SchemaUtils.create(
                Intersections,
                TrafficData,
                TrafficAnalytics,
                PeakHours,
                TrafficIncidents,
                TrafficPredictions
            )
        }
    }
    
    /**
     * Insert sample data for testing
     */
    fun insertSampleData() {
        transaction {
            // Create sample intersections
            val intersection1 = IntersectionDAO.new {
                name = "Main St & 1st Ave"
                latitude = BigDecimal("40.7128")
                longitude = BigDecimal("-74.0060")
                description = "Busy downtown intersection"
                isActive = true
                createdAt = LocalDateTime.now()
            }
            
            val intersection2 = IntersectionDAO.new {
                name = "Broadway & 42nd St"
                latitude = BigDecimal("40.7505")
                longitude = BigDecimal("-73.9934")
                description = "Times Square area"
                isActive = true
                createdAt = LocalDateTime.now()
            }
            
            val intersection3 = IntersectionDAO.new {
                name = "Park Ave & 59th St"
                latitude = BigDecimal("40.7676")
                longitude = BigDecimal("-73.9708")
                description = "Central Park area"
                isActive = true
                createdAt = LocalDateTime.now()
            }
            
            println("Sample intersections created:")
            println("- ${intersection1.name} (ID: ${intersection1.id})")
            println("- ${intersection2.name} (ID: ${intersection2.id})")
            println("- ${intersection3.name} (ID: ${intersection3.id})")
        }
    }
    
    /**
     * Drop all traffic-related tables (for testing/development)
     */
    fun dropTables() {
        transaction {
            SchemaUtils.drop(
                TrafficPredictions,
                TrafficIncidents,
                PeakHours,
                TrafficAnalytics,
                TrafficData,
                Intersections
            )
        }
    }
}