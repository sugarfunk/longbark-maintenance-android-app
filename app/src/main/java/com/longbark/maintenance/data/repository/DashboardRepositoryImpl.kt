package com.longbark.maintenance.data.repository

import com.longbark.maintenance.data.local.dao.SiteDao
import com.longbark.maintenance.data.local.dao.NotificationDao
import com.longbark.maintenance.data.local.preferences.AppPreferences
import com.longbark.maintenance.data.remote.api.LongBarkApiService
import com.longbark.maintenance.domain.model.DashboardStats
import com.longbark.maintenance.domain.repository.DashboardRepository
import com.longbark.maintenance.util.Result
import com.longbark.maintenance.util.safeApiCall
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val apiService: LongBarkApiService,
    private val siteDao: SiteDao,
    private val notificationDao: NotificationDao,
    private val appPreferences: AppPreferences
) : DashboardRepository {

    override suspend fun getDashboardStats(): Result<DashboardStats> {
        return when (val result = safeApiCall { apiService.getDashboardStats() }) {
            is Result.Success -> Result.Success(result.data.toDomain())
            is Result.Error -> {
                // Fallback to local data
                try {
                    val totalSites = siteDao.getSiteCount()
                    val healthySites = siteDao.getHealthySiteCount()
                    val warningSites = siteDao.getWarningSiteCount()
                    val criticalSites = siteDao.getCriticalSiteCount()
                    val recentAlerts = notificationDao.getAllNotifications().first()
                        .take(10)
                        .map { it.toDomain() }
                    val lastSync = appPreferences.lastSyncTimestamp.first()

                    Result.Success(
                        DashboardStats(
                            totalSites = totalSites,
                            healthySites = healthySites,
                            warningSites = warningSites,
                            criticalSites = criticalSites,
                            recentAlerts = recentAlerts,
                            lastSyncTimestamp = lastSync
                        )
                    )
                } catch (e: Exception) {
                    result
                }
            }
            is Result.Loading -> result
        }
    }

    override suspend fun refreshDashboard(): Result<Unit> {
        return when (safeApiCall { apiService.getDashboardStats() }) {
            is Result.Success -> {
                appPreferences.updateLastSyncTimestamp(System.currentTimeMillis())
                Result.Success(Unit)
            }
            is Result.Error -> Result.Error(Exception("Failed to refresh dashboard"))
            is Result.Loading -> Result.Loading
        }
    }
}
