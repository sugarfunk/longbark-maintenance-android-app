package com.longbark.maintenance.domain.repository

import com.longbark.maintenance.domain.model.DashboardStats
import com.longbark.maintenance.util.Result

interface DashboardRepository {
    suspend fun getDashboardStats(): Result<DashboardStats>
    suspend fun refreshDashboard(): Result<Unit>
}
