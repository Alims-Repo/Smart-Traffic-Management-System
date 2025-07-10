package com.gub.test

import com.gub.database.DatabaseFactory
import com.gub.database.TrafficDatabaseInitializer
import com.gub.models.traffic.CreateTrafficDataRequest
import com.gub.models.traffic.CongestionLevel
import com.gub.services.traffic.TrafficDataService
import kotlinx.coroutines.runBlocking

/**
 * Simple test to verify the traffic database system works
 */
fun main() = runBlocking {
    println("🚦 Testing Traffic Management Database System...")
    
    try {
        // Initialize database
        println("📊 Initializing database connection...")
        DatabaseFactory.init()
        
        // Create tables
        println("🔧 Creating database tables...")
        TrafficDatabaseInitializer.createTables()
        
        // Insert sample data
        println("📝 Inserting sample intersections...")
        TrafficDatabaseInitializer.insertSampleData()
        
        // Test service operations
        println("🧪 Testing traffic data service...")
        val trafficService = TrafficDataService()
        
        // Get all intersections
        val intersections = trafficService.getAllIntersections()
        println("✅ Found ${intersections.size} intersections:")
        intersections.forEach { intersection ->
            println("   - ${intersection.name} (ID: ${intersection.id})")
        }
        
        // Create test traffic data
        if (intersections.isNotEmpty()) {
            val firstIntersection = intersections.first()
            println("📊 Creating test traffic data for ${firstIntersection.name}...")
            
            val testTrafficData = CreateTrafficDataRequest(
                intersectionId = firstIntersection.id,
                vehicleCount = 45,
                averageSpeed = 35.5,
                congestionLevel = CongestionLevel.MEDIUM,
                temperature = 22.5,
                humidity = 65,
                visibility = 8.2
            )
            
            val createdData = trafficService.createTrafficData(testTrafficData)
            println("✅ Created traffic data entry with ID: ${createdData.id}")
            println("   - Vehicle Count: ${createdData.vehicleCount}")
            println("   - Average Speed: ${createdData.averageSpeed} km/h")
            println("   - Congestion Level: ${createdData.congestionLevel}")
            
            // Retrieve traffic data
            println("📈 Retrieving traffic data for intersection...")
            val trafficData = trafficService.getTrafficDataByIntersection(firstIntersection.id, 5)
            println("✅ Retrieved ${trafficData.size} traffic data entries")
            
            trafficData.forEach { data ->
                println("   - ${data.timestamp}: ${data.vehicleCount} vehicles, ${data.averageSpeed} km/h avg speed")
            }
        }
        
        println("🎉 All database tests passed successfully!")
        
    } catch (e: Exception) {
        println("❌ Database test failed: ${e.message}")
        e.printStackTrace()
    }
}