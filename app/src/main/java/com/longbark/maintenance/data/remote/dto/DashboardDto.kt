package com.longbark.maintenance.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.longbark.maintenance.domain.model.DashboardStats

data class DashboardStatsDto(
    @SerializedName("total_sites")
    val totalSites: Int,
    @SerializedName("healthy_sites")
    val healthySites: Int,
    @SerializedName("warning_sites")
    val warningSites: Int,
    @SerializedName("critical_sites")
    val criticalSites: Int,
    @SerializedName("recent_alerts")
    val recentAlerts: List<NotificationDto>,
    @SerializedName("last_sync_timestamp")
    val lastSyncTimestamp: Long?
) {
    fun toDomain(): DashboardStats = DashboardStats(
        totalSites = totalSites,
        healthySites = healthySites,
        warningSites = warningSites,
        criticalSites = criticalSites,
        recentAlerts = recentAlerts.map { it.toDomain() },
        lastSyncTimestamp = lastSyncTimestamp
    )
}
