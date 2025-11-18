package com.longbark.maintenance.domain.model

data class AppNotification(
    val id: String,
    val siteId: String?,
    val clientId: String?,
    val title: String,
    val message: String,
    val type: NotificationType,
    val severity: NotificationSeverity,
    val timestamp: Long,
    val isRead: Boolean,
    val actionUrl: String?
)

enum class NotificationType {
    DOWNTIME,
    SSL_EXPIRY,
    PLUGIN_UPDATE,
    BROKEN_LINKS,
    SECURITY_ISSUE,
    REPORT_READY,
    CHECK_SUCCESS,
    GENERAL
}

enum class NotificationSeverity {
    CRITICAL,
    WARNING,
    INFO
}
