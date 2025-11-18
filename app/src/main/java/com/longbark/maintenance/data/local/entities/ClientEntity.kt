package com.longbark.maintenance.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.longbark.maintenance.domain.model.Client
import com.longbark.maintenance.domain.model.HealthStatus

@Entity(tableName = "clients")
data class ClientEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val logoUrl: String?,
    val siteCount: Int,
    val healthStatus: String,
    val lastCheckTimestamp: Long,
    val createdAt: Long
) {
    fun toDomain(): Client = Client(
        id = id,
        name = name,
        logoUrl = logoUrl,
        siteCount = siteCount,
        healthStatus = HealthStatus.valueOf(healthStatus),
        lastCheckTimestamp = lastCheckTimestamp,
        createdAt = createdAt
    )

    companion object {
        fun fromDomain(client: Client): ClientEntity = ClientEntity(
            id = client.id,
            name = client.name,
            logoUrl = client.logoUrl,
            siteCount = client.siteCount,
            healthStatus = client.healthStatus.name,
            lastCheckTimestamp = client.lastCheckTimestamp,
            createdAt = client.createdAt
        )
    }
}
