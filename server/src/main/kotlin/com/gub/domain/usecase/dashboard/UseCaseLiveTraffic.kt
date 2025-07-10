package com.gub.domain.usecase.dashboard

import com.gub.domain.models.dashboard.ModelLiveTraffic
import com.gub.domain.repository.RepositoryDashboard

class UseCaseLiveTraffic(private val repositoryDashboard: RepositoryDashboard) {

    operator fun invoke(): ModelLiveTraffic {
        return repositoryDashboard.getLiveTrafficMetrics()
    }
}