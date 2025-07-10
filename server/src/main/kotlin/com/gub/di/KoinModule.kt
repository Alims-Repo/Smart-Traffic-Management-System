package com.gub.di

import com.gub.data.repository.DashboardRepositoryImpl
import com.gub.data.service.dashboard.ServiceDashboard
import org.koin.dsl.module

val dashboardModule = module {
    single { DashboardRepositoryImpl() }
    single { ServiceDashboard(get()) }
}