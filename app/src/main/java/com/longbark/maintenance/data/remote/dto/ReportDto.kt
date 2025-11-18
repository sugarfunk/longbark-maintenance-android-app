package com.longbark.maintenance.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.longbark.maintenance.domain.model.Report
import com.longbark.maintenance.domain.model.ReportFormat
import com.longbark.maintenance.domain.model.ReportType

data class ReportDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("client_id")
    val clientId: String?,
    @SerializedName("site_id")
    val siteId: String?,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("format")
    val format: String,
    @SerializedName("generated_at")
    val generatedAt: Long,
    @SerializedName("start_date")
    val startDate: Long,
    @SerializedName("end_date")
    val endDate: Long,
    @SerializedName("download_url")
    val downloadUrl: String,
    @SerializedName("file_size")
    val fileSize: Long?
) {
    fun toDomain(): Report = Report(
        id = id,
        clientId = clientId,
        siteId = siteId,
        title = title,
        type = try {
            ReportType.valueOf(type.uppercase())
        } catch (e: IllegalArgumentException) {
            ReportType.COMPREHENSIVE
        },
        format = try {
            ReportFormat.valueOf(format.uppercase())
        } catch (e: IllegalArgumentException) {
            ReportFormat.PDF
        },
        generatedAt = generatedAt,
        startDate = startDate,
        endDate = endDate,
        downloadUrl = downloadUrl,
        fileSize = fileSize
    )
}

data class ReportsResponse(
    @SerializedName("reports")
    val reports: List<ReportDto>,
    @SerializedName("total")
    val total: Int
)
