package com.longbark.maintenance.domain.model

data class DashboardStats(
    val totalSites: Int,
    val healthySites: Int,
    val warningSites: Int,
    val criticalSites: Int,
    val recentAlerts: List<AppNotification>,
    val lastSyncTimestamp: Long?
)
