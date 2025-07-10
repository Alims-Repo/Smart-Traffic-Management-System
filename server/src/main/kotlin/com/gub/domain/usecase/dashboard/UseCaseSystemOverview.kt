package com.gub.domain.usecase.dashboard

import com.gub.domain.repository.RepositoryDashboard
import com.gub.models.dashboard.overview.ModelSystemOverview

class UseCaseSystemOverview(private val repositoryDashboard: RepositoryDashboard) {

    operator fun invoke(): ModelSystemOverview {
        return repositoryDashboard.getSystemOverview()
    }
}