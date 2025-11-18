package com.longbark.maintenance.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.longbark.maintenance.domain.model.Client
import com.longbark.maintenance.domain.model.HealthStatus

data class ClientDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("logo_url")
    val logoUrl: String?,
    @SerializedName("site_count")
    val siteCount: Int,
    @SerializedName("health_status")
    val healthStatus: String,
    @SerializedName("last_check_timestamp")
    val lastCheckTimestamp: Long,
    @SerializedName("created_at")
    val createdAt: Long
) {
    fun toDomain(): Client = Client(
        id = id,
        name = name,
        logoUrl = logoUrl,
        siteCount = siteCount,
        healthStatus = try {
            HealthStatus.valueOf(healthStatus.uppercase())
        } catch (e: IllegalArgumentException) {
            HealthStatus.UNKNOWN
        },
        lastCheckTimestamp = lastCheckTimestamp,
        createdAt = createdAt
    )
}

data class ClientsResponse(
    @SerializedName("clients")
    val clients: List<ClientDto>,
    @SerializedName("total")
    val total: Int
)
