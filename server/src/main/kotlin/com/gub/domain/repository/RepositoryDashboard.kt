package com.gub.domain.repository

import com.gub.domain.models.dashboard.ModelAiControl
import com.gub.domain.models.dashboard.ModelLiveTraffic
import com.gub.domain.models.dashboard.ModelSystemOverview

interface RepositoryDashboard {

    fun getSystemOverview(): ModelSystemOverview

    fun getLiveTrafficMetrics(): ModelLiveTraffic

    fun getAiControlSystem(): ModelAiControl
}