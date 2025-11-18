package com.longbark.maintenance.domain.model

data class Report(
    val id: String,
    val clientId: String?,
    val siteId: String?,
    val title: String,
    val type: ReportType,
    val format: ReportFormat,
    val generatedAt: Long,
    val startDate: Long,
    val endDate: Long,
    val downloadUrl: String,
    val fileSize: Long?
)

enum class ReportType {
    SITE_HEALTH,
    SEO_ANALYSIS,
    UPTIME,
    PERFORMANCE,
    SECURITY,
    COMPREHENSIVE
}

enum class ReportFormat {
    PDF,
    HTML,
    CSV
}
