package com.gub.data.repository

import com.gub.data.service.dashboard.ServiceDashboard
import com.gub.domain.models.dashboard.ModelAiControl
import com.gub.domain.models.dashboard.ModelLiveTraffic
import com.gub.domain.models.dashboard.ModelSystemOverview
import com.gub.domain.repository.RepositoryDashboard

class RepositoryDashboardImpl(
    private val serviceDashboard: ServiceDashboard
) : RepositoryDashboard {

    override fun getSystemOverview(): ModelSystemOverview {
        return serviceDashboard.getSystemOverview()
    }

    override fun getLiveTrafficMetrics(): ModelLiveTraffic {
        return ModelLiveTraffic()
    }

    override fun getAiControlSystem(): ModelAiControl {
        return ModelAiControl()
    }
}