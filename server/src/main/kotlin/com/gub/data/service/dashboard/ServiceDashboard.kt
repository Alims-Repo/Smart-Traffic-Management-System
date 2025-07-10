package com.gub.data.service.dashboard

import com.gub.domain.models.dashboard.ModelAiControl
import com.gub.domain.models.dashboard.ModelLiveTraffic
import com.gub.domain.models.dashboard.ModelSystemOverview
import com.gub.models.dashboard.overview.ModelWeather

class ServiceDashboard {

    fun getAiControlStatus(): ModelAiControl {
        // Placeholder logic — replace with real-time AI metrics
        return ModelAiControl(
            efficiency = 92.5,
            runningModel = 1,
            decisionSpeed = 48
        )
    }

    fun getLiveTrafficStatus(): ModelLiveTraffic {
        // Placeholder logic — simulate traffic stats
        return ModelLiveTraffic(
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
    }

    fun getSystemOverview(): ModelSystemOverview {
        // Placeholder logic — simulate system health
        return ModelSystemOverview(
            systemHealth = 96.3,
            aiResponseTime = 0.42,
            avgWaitTime = 23.4,
            currentFlow = 75.2,
            weather = ModelWeather()
        )
    }
}