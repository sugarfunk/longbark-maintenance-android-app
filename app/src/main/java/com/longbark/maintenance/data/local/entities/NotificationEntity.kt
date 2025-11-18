package com.longbark.maintenance.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.longbark.maintenance.domain.model.AppNotification
import com.longbark.maintenance.domain.model.NotificationSeverity
import com.longbark.maintenance.domain.model.NotificationType

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey
    val id: String,
    val siteId: String?,
    val clientId: String?,
    val title: String,
    val message: String,
    val type: String,
    val severity: String,
    val timestamp: Long,
    val isRead: Boolean,
    val actionUrl: String?
) {
    fun toDomain(): AppNotification = AppNotification(
        id = id,
        siteId = siteId,
        clientId = clientId,
        title = title,
        message = message,
        type = NotificationType.valueOf(type),
        severity = NotificationSeverity.valueOf(severity),
        timestamp = timestamp,
        isRead = isRead,
        actionUrl = actionUrl
    )

    companion object {
        fun fromDomain(notification: AppNotification): NotificationEntity = NotificationEntity(
            id = notification.id,
            siteId = notification.siteId,
            clientId = notification.clientId,
            title = notification.title,
            message = notification.message,
            type = notification.type.name,
            severity = notification.severity.name,
            timestamp = notification.timestamp,
            isRead = notification.isRead,
            actionUrl = notification.actionUrl
        )
    }
}
