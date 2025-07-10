package com.gub.data.repository

import com.gub.data.database.*
import com.gub.data.models.*
import com.gub.domain.models.dashboard.*
import com.gub.models.dashboard.overview.ModelWeather
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.less
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

class DashboardRepositoryImpl {

    suspend fun getLatestAiControl(): ModelAiControl {
        return transaction {
            val result = AiControlMetrics
                .selectAll()
                .orderBy(AiControlMetrics.timestamp, SortOrder.DESC)
                .limit(1)
                .firstOrNull()

            result?.let {
                ModelAiControl(
                    efficiency = it[AiControlMetrics.efficiency],
                    runningModel = it[AiControlMetrics.runningModel],
                    decisionSpeed = it[AiControlMetrics.decisionSpeed]
                )
            } ?: generateMockAiControl()
        }
    }

    suspend fun getLatestTraffic(): ModelLiveTraffic {
        return transaction {
            val result = TrafficMetrics
                .selectAll()
                .orderBy(TrafficMetrics.timestamp, SortOrder.DESC)
                .limit(1)
                .firstOrNull()

            result?.let {
                ModelLiveTraffic(
                    vehicle = ModelLiveTraffic.Vehicle(
                        count = it[TrafficMetrics.vehicleCount],
                        difference = it[TrafficMetrics.vehicleDifference],
                        upWards = it[TrafficMetrics.vehicleUpwards]
                    ),
                    congestion = ModelLiveTraffic.Congestion(
                        count = it[TrafficMetrics.congestionCount],
                        difference = it[TrafficMetrics.congestionDifference],
                        upWards = it[TrafficMetrics.congestionUpwards]
                    )
                )
            } ?: generateMockTraffic()
        }
    }

    suspend fun getLatestSystemOverview(): ModelSystemOverview {
        return transaction {
            val result = SystemMetrics
                .selectAll()
                .orderBy(SystemMetrics.timestamp, SortOrder.DESC)
                .limit(1)
                .firstOrNull()

            result?.let {
                ModelSystemOverview(
                    systemHealth = it[SystemMetrics.systemHealth],
                    aiResponseTime = it[SystemMetrics.aiResponseTime],
                    avgWaitTime = it[SystemMetrics.avgWaitTime],
                    currentFlow = it[SystemMetrics.currentFlow],
                    weather = ModelWeather() // You'll implement weather service
                )
            } ?: generateMockSystemOverview()
        }
    }

    suspend fun insertAiControl(efficiency: Double, runningModel: Int, decisionSpeed: Int) {
        transaction {
            AiControlMetrics.insert {
                it[AiControlMetrics.efficiency] = efficiency
                it[AiControlMetrics.runningModel] = runningModel
                it[AiControlMetrics.decisionSpeed] = decisionSpeed
            }
        }
    }

    suspend fun insertTrafficMetrics(
        vehicleCount: Int, vehicleDifference: Int, vehicleUpwards: Boolean,
        congestionCount: Int, congestionDifference: Int, congestionUpwards: Boolean
    ) {
        transaction {
            TrafficMetrics.insert {
                it[TrafficMetrics.vehicleCount] = vehicleCount
                it[TrafficMetrics.vehicleDifference] = vehicleDifference
                it[TrafficMetrics.vehicleUpwards] = vehicleUpwards
                it[TrafficMetrics.congestionCount] = congestionCount
                it[TrafficMetrics.congestionDifference] = congestionDifference
                it[TrafficMetrics.congestionUpwards] = congestionUpwards
            }
        }
    }

    suspend fun insertSystemMetrics(
        systemHealth: Double, aiResponseTime: Double,
        avgWaitTime: Double, currentFlow: Double
    ) {
        transaction {
            SystemMetrics.insert {
                it[SystemMetrics.systemHealth] = systemHealth
                it[SystemMetrics.aiResponseTime] = aiResponseTime
                it[SystemMetrics.avgWaitTime] = avgWaitTime
                it[SystemMetrics.currentFlow] = currentFlow
            }
        }
    }

    suspend fun getAiControlHistory(limit: Int = 100): List<AiControlMetricEntity> {
        return transaction {
            AiControlMetrics
                .selectAll()
                .orderBy(AiControlMetrics.timestamp, SortOrder.DESC)
                .limit(limit)
                .map {
                    AiControlMetricEntity(
                        id = it[AiControlMetrics.id].value,
                        efficiency = it[AiControlMetrics.efficiency],
                        runningModel = it[AiControlMetrics.runningModel],
                        decisionSpeed = it[AiControlMetrics.decisionSpeed],
                        timestamp = it[AiControlMetrics.timestamp].toString()
                    )
                }
        }
    }

    suspend fun cleanupOldData(daysToKeep: Int = 30) {
        val cutoffDate = LocalDateTime.now().minusDays(daysToKeep.toLong())
        transaction {
            AiControlMetrics.deleteWhere { AiControlMetrics.timestamp less cutoffDate }
            TrafficMetrics.deleteWhere { TrafficMetrics.timestamp less cutoffDate }
            SystemMetrics.deleteWhere { SystemMetrics.timestamp less cutoffDate }
        }
    }

    // Mock data methods
    private fun generateMockAiControl() = ModelAiControl(
        efficiency = 92.5,
        runningModel = 1,
        decisionSpeed = 48
    )

    private fun generateMockTraffic() = ModelLiveTraffic(
        vehicle = ModelLiveTraffic.Vehicle(
            count = 120,
            difference = 10,
            upWards = true
        ),
        congestion = ModelLiveTraffic.Congestion(
            count = 35,
            difference = -5,
            upWards = false
        )
    )

    private fun generateMockSystemOverview() = ModelSystemOverview(
        systemHealth = 96.3,
        aiResponseTime = 0.42,
        avgWaitTime = 23.4,
        currentFlow = 75.2,
        weather = ModelWeather()
    )
}