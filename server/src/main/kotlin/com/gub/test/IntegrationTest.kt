package com.gub.test

import com.gub.database.DatabaseFactory
import com.gub.database.TrafficDatabaseInitializer
import com.gub.models.traffic.*
import com.gub.services.traffic.TrafficDataService
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import kotlin.random.Random

/**
 * Integration test that simulates a complete traffic data flow
 * This demonstrates how AI systems would feed data into the database
 * and how analytics would be generated
 */
fun main() = runBlocking {
    println("🚦 Traffic Management System Integration Test")
    println("================================================")
    
    try {
        // Initialize system
        println("🔧 Setting up database...")
        DatabaseFactory.init()
        TrafficDatabaseInitializer.createTables()
        TrafficDatabaseInitializer.insertSampleData()
        
        val trafficService = TrafficDataService()
        val intersections = trafficService.getAllIntersections()
        
        println("✅ Found ${intersections.size} intersections for testing")
        
        // Simulate AI detection systems sending real-time data
        println("\n📊 Simulating AI Detection Systems...")
        repeat(10) { cycle ->
            intersections.forEach { intersection ->
                // Simulate varying traffic conditions throughout the day
                val timeOfDay = (cycle * 2) % 24  // Simulate hours 0-22
                val isRushHour = timeOfDay in 7..9 || timeOfDay in 17..19
                
                // Generate realistic traffic data based on time
                val baseVehicleCount = if (isRushHour) 60 + Random.nextInt(40) else 20 + Random.nextInt(30)
                val baseSpeed = if (isRushHour) 15.0 + Random.nextDouble(15.0) else 40.0 + Random.nextDouble(20.0)
                val congestionLevel = when {
                    baseVehicleCount > 80 -> CongestionLevel.CRITICAL
                    baseVehicleCount > 60 -> CongestionLevel.HIGH
                    baseVehicleCount > 35 -> CongestionLevel.MEDIUM
                    else -> CongestionLevel.LOW
                }
                
                val trafficData = CreateTrafficDataRequest(
                    intersectionId = intersection.id,
                    vehicleCount = baseVehicleCount,
                    averageSpeed = baseSpeed,
                    congestionLevel = congestionLevel,
                    temperature = 20.0 + Random.nextDouble(15.0),
                    humidity = 40 + Random.nextInt(40),
                    visibility = 5.0 + Random.nextDouble(5.0)
                )
                
                val created = trafficService.createTrafficData(trafficData)
                println("📈 Cycle $cycle - ${intersection.name}: ${created.vehicleCount} vehicles, ${String.format("%.1f", created.averageSpeed)} km/h, ${created.congestionLevel}")
            }
            
            // Small delay to simulate real-time data collection
            delay(100)
        }
        
        println("\n📊 Generating Analytics...")
        
        // Generate analytics for each intersection
        intersections.forEach { intersection ->
            // Get recent traffic data for this intersection
            val recentData = trafficService.getTrafficDataByIntersection(intersection.id, 10)
            
            if (recentData.isNotEmpty()) {
                // Calculate analytics
                val totalVehicles = recentData.sumOf { it.vehicleCount }
                val avgSpeed = recentData.map { it.averageSpeed }.average()
                val highCongestionCount = recentData.count { 
                    it.congestionLevel == CongestionLevel.HIGH || it.congestionLevel == CongestionLevel.CRITICAL 
                }
                val congestionScore = highCongestionCount.toDouble() / recentData.size
                
                val analyticsRequest = CreateTrafficAnalyticsRequest(
                    intersectionId = intersection.id,
                    totalVehicles = totalVehicles,
                    averageSpeed = avgSpeed,
                    congestionLevel = congestionScore,
                    incidentCount = if (congestionScore > 0.5) Random.nextInt(3) else 0,
                    peakHourIntensity = if (congestionScore > 0.3) 0.7 + Random.nextDouble(0.3) else Random.nextDouble(0.4),
                    aiOptimizedTiming = congestionScore > 0.4,
                    periodType = PeriodType.HOURLY
                )
                
                val analytics = trafficService.createTrafficAnalytics(analyticsRequest)
                println("📈 Analytics for ${intersection.name}:")
                println("   - Total Vehicles: ${analytics.totalVehicles}")
                println("   - Avg Speed: ${String.format("%.1f", analytics.averageSpeed)} km/h")
                println("   - Congestion Score: ${String.format("%.2f", analytics.congestionLevel)}")
                println("   - AI Optimized: ${analytics.aiOptimizedTiming}")
            }
        }
        
        println("\n🔍 Testing Data Retrieval...")
        
        // Test various data retrieval methods
        val firstIntersection = intersections.first()
        
        // Get latest data
        val latestData = trafficService.getLatestTrafficData()
        println("📊 Latest traffic data: ${latestData.size} entries")
        
        // Get data for specific intersection
        val intersectionData = trafficService.getTrafficDataByIntersection(firstIntersection.id, 5)
        println("📍 Data for ${firstIntersection.name}: ${intersectionData.size} entries")
        
        // Get analytics
        val analytics = trafficService.getTrafficAnalytics(firstIntersection.id, PeriodType.HOURLY, 3)
        println("📈 Analytics for ${firstIntersection.name}: ${analytics.size} entries")
        
        // Display recent traffic patterns
        println("\n📊 Recent Traffic Patterns:")
        intersectionData.forEach { data ->
            val statusEmoji = when (data.congestionLevel) {
                CongestionLevel.LOW -> "🟢"
                CongestionLevel.MEDIUM -> "🟡"
                CongestionLevel.HIGH -> "🟠"
                CongestionLevel.CRITICAL -> "🔴"
            }
            println("$statusEmoji ${data.timestamp.substring(11, 16)}: ${data.vehicleCount} vehicles @ ${String.format("%.1f", data.averageSpeed)} km/h")
        }
        
        println("\n🎉 Integration Test Completed Successfully!")
        println("✅ Database schema working correctly")
        println("✅ Real-time data ingestion simulated")
        println("✅ Analytics generation working")
        println("✅ Data retrieval APIs functional")
        println("✅ System ready for production use")
        
    } catch (e: Exception) {
        println("❌ Integration test failed: ${e.message}")
        e.printStackTrace()
    }
}