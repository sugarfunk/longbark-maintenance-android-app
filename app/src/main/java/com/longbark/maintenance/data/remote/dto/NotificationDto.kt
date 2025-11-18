package com.longbark.maintenance.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.longbark.maintenance.domain.model.AppNotification
import com.longbark.maintenance.domain.model.NotificationSeverity
import com.longbark.maintenance.domain.model.NotificationType

data class NotificationDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("site_id")
    val siteId: String?,
    @SerializedName("client_id")
    val clientId: String?,
    @SerializedName("title")
    val title: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("severity")
    val severity: String,
    @SerializedName("timestamp")
    val timestamp: Long,
    @SerializedName("is_read")
    val isRead: Boolean,
    @SerializedName("action_url")
    val actionUrl: String?
) {
    fun toDomain(): AppNotification = AppNotification(
        id = id,
        siteId = siteId,
        clientId = clientId,
        title = title,
        message = message,
        type = try {
            NotificationType.valueOf(type.uppercase())
        } catch (e: IllegalArgumentException) {
            NotificationType.GENERAL
        },
        severity = try {
            NotificationSeverity.valueOf(severity.uppercase())
        } catch (e: IllegalArgumentException) {
            NotificationSeverity.INFO
        },
        timestamp = timestamp,
        isRead = isRead,
        actionUrl = actionUrl
    )
}
