package com.longbark.maintenance.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.longbark.maintenance.domain.model.Report
import com.longbark.maintenance.domain.model.ReportFormat
import com.longbark.maintenance.domain.model.ReportType

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey
    val id: String,
    val clientId: String?,
    val siteId: String?,
    val title: String,
    val type: String,
    val format: String,
    val generatedAt: Long,
    val startDate: Long,
    val endDate: Long,
    val downloadUrl: String,
    val fileSize: Long?
) {
    fun toDomain(): Report = Report(
        id = id,
        clientId = clientId,
        siteId = siteId,
        title = title,
        type = ReportType.valueOf(type),
        format = ReportFormat.valueOf(format),
        generatedAt = generatedAt,
        startDate = startDate,
        endDate = endDate,
        downloadUrl = downloadUrl,
        fileSize = fileSize
    )

    companion object {
        fun fromDomain(report: Report): ReportEntity = ReportEntity(
            id = report.id,
            clientId = report.clientId,
            siteId = report.siteId,
            title = report.title,
            type = report.type.name,
            format = report.format.name,
            generatedAt = report.generatedAt,
            startDate = report.startDate,
            endDate = report.endDate,
            downloadUrl = report.downloadUrl,
            fileSize = report.fileSize
        )
    }
}
