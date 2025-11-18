package com.longbark.maintenance.domain.model

data class Client(
    val id: String,
    val name: String,
    val logoUrl: String?,
    val siteCount: Int,
    val healthStatus: HealthStatus,
    val lastCheckTimestamp: Long,
    val createdAt: Long
)

enum class HealthStatus {
    HEALTHY,
    WARNING,
    CRITICAL,
    UNKNOWN
}
