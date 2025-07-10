package com.gub.domain.usecase.dashboard

import com.gub.domain.models.dashboard.ModelSystemOverview
import com.gub.domain.repository.RepositoryDashboard

class UseCaseSystemOverview(private val repositoryDashboard: RepositoryDashboard) {

    operator fun invoke(): ModelSystemOverview {
        return repositoryDashboard.getSystemOverview()
    }
}