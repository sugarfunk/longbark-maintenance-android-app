package com.longbark.maintenance.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.longbark.maintenance.domain.model.HealthStatus
import com.longbark.maintenance.domain.model.Site

@Entity(
    tableName = "sites",
    foreignKeys = [
        ForeignKey(
            entity = ClientEntity::class,
            parentColumns = ["id"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("clientId")]
)
data class SiteEntity(
    @PrimaryKey
    val id: String,
    val clientId: String,
    val name: String,
    val url: String,
    val isWordPress: Boolean,
    val uptimePercentage: Float,
    val healthStatus: String,
    val lastCheckTimestamp: Long,
    val createdAt: Long,
    val updatedAt: Long
) {
    fun toDomain(): Site = Site(
        id = id,
        clientId = clientId,
        name = name,
        url = url,
        isWordPress = isWordPress,
        uptimePercentage = uptimePercentage,
        healthStatus = HealthStatus.valueOf(healthStatus),
        lastCheckTimestamp = lastCheckTimestamp,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    companion object {
        fun fromDomain(site: Site): SiteEntity = SiteEntity(
            id = site.id,
            clientId = site.clientId,
            name = site.name,
            url = site.url,
            isWordPress = site.isWordPress,
            uptimePercentage = site.uptimePercentage,
            healthStatus = site.healthStatus.name,
            lastCheckTimestamp = site.lastCheckTimestamp,
            createdAt = site.createdAt,
            updatedAt = site.updatedAt
        )
    }
}
