package com.gub.data.service.dashboard

import com.gub.data.repository.DashboardRepositoryImpl
import com.gub.domain.models.dashboard.ModelAiControl
import com.gub.domain.models.dashboard.ModelLiveTraffic
import com.gub.domain.models.dashboard.ModelSystemOverview

class ServiceDashboard(
    private val dashboardRepository: DashboardRepositoryImpl
) {

    suspend fun getAiControlStatus(): ModelAiControl {
        return dashboardRepository.getLatestAiControl()
    }

    suspend fun getLiveTrafficStatus(): ModelLiveTraffic {
        return dashboardRepository.getLatestTraffic()
    }

    suspend fun getSystemOverview(): ModelSystemOverview {
        return dashboardRepository.getLatestSystemOverview()
    }

    // Methods to update metrics from external systems
    suspend fun updateAiMetrics(efficiency: Double, runningModel: Int, decisionSpeed: Int) {
        dashboardRepository.insertAiControl(efficiency, runningModel, decisionSpeed)
    }

    suspend fun updateTrafficMetrics(
        vehicleCount: Int, vehicleDifference: Int, vehicleUpwards: Boolean,
        congestionCount: Int, congestionDifference: Int, congestionUpwards: Boolean
    ) {
        dashboardRepository.insertTrafficMetrics(
            vehicleCount, vehicleDifference, vehicleUpwards,
            congestionCount, congestionDifference, congestionUpwards
        )
    }

    suspend fun updateSystemMetrics(
        systemHealth: Double, aiResponseTime: Double,
        avgWaitTime: Double, currentFlow: Double
    ) {
        dashboardRepository.insertSystemMetrics(systemHealth, aiResponseTime, avgWaitTime, currentFlow)
    }

    // Get historical data
    suspend fun getAiControlHistory(limit: Int = 100) = dashboardRepository.getAiControlHistory(limit)

    // Cleanup old data
    suspend fun performDataCleanup(daysToKeep: Int = 30) = dashboardRepository.cleanupOldData(daysToKeep)
}