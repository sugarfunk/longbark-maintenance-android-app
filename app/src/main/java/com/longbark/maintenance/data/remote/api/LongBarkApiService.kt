package com.longbark.maintenance.data.remote.api

import com.longbark.maintenance.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface LongBarkApiService {
    // Dashboard
    @GET("dashboard/stats")
    suspend fun getDashboardStats(): Response<DashboardStatsDto>

    // Clients
    @GET("clients")
    suspend fun getClients(): Response<ClientsResponse>

    @GET("clients/{id}")
    suspend fun getClientById(@Path("id") clientId: String): Response<ClientDto>

    // Sites
    @GET("sites")
    suspend fun getSites(
        @Query("client_id") clientId: String? = null
    ): Response<SitesResponse>

    @GET("sites/{id}")
    suspend fun getSiteById(@Path("id") siteId: String): Response<SiteDetailsDto>

    @POST("sites/{id}/check")
    suspend fun triggerSiteCheck(@Path("id") siteId: String): Response<TriggerCheckResponse>

    @POST("sites/check-all")
    suspend fun triggerAllChecks(): Response<TriggerCheckResponse>

    // Reports
    @GET("reports")
    suspend fun getReports(
        @Query("client_id") clientId: String? = null,
        @Query("site_id") siteId: String? = null,
        @Query("type") type: String? = null
    ): Response<ReportsResponse>

    @GET("reports/{id}")
    suspend fun getReportById(@Path("id") reportId: String): Response<ReportDto>

    @GET("reports/{id}/download")
    suspend fun downloadReport(@Path("id") reportId: String): Response<okhttp3.ResponseBody>

    // Notifications
    @GET("notifications")
    suspend fun getNotifications(
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): Response<List<NotificationDto>>

    @PUT("notifications/{id}/read")
    suspend fun markNotificationAsRead(@Path("id") notificationId: String): Response<Unit>

    @PUT("notifications/read-all")
    suspend fun markAllNotificationsAsRead(): Response<Unit>
}
