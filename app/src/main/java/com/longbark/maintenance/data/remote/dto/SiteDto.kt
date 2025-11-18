package com.longbark.maintenance.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.longbark.maintenance.domain.model.*

data class SiteDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("client_id")
    val clientId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("is_wordpress")
    val isWordPress: Boolean,
    @SerializedName("uptime_percentage")
    val uptimePercentage: Float,
    @SerializedName("health_status")
    val healthStatus: String,
    @SerializedName("last_check_timestamp")
    val lastCheckTimestamp: Long,
    @SerializedName("created_at")
    val createdAt: Long,
    @SerializedName("updated_at")
    val updatedAt: Long
) {
    fun toDomain(): Site = Site(
        id = id,
        clientId = clientId,
        name = name,
        url = url,
        isWordPress = isWordPress,
        uptimePercentage = uptimePercentage,
        healthStatus = try {
            HealthStatus.valueOf(healthStatus.uppercase())
        } catch (e: IllegalArgumentException) {
            HealthStatus.UNKNOWN
        },
        lastCheckTimestamp = lastCheckTimestamp,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

data class SitesResponse(
    @SerializedName("sites")
    val sites: List<SiteDto>,
    @SerializedName("total")
    val total: Int
)

data class SiteDetailsDto(
    @SerializedName("site")
    val site: SiteDto,
    @SerializedName("ssl_info")
    val sslInfo: SslInfoDto?,
    @SerializedName("wordpress_info")
    val wordPressInfo: WordPressInfoDto?,
    @SerializedName("seo_info")
    val seoInfo: SeoInfoDto?,
    @SerializedName("performance_metrics")
    val performanceMetrics: PerformanceMetricsDto?
) {
    fun toDomain(): SiteDetails = SiteDetails(
        site = site.toDomain(),
        sslInfo = sslInfo?.toDomain(),
        wordPressInfo = wordPressInfo?.toDomain(),
        seoInfo = seoInfo?.toDomain(),
        performanceMetrics = performanceMetrics?.toDomain()
    )
}

data class SslInfoDto(
    @SerializedName("is_valid")
    val isValid: Boolean,
    @SerializedName("expiry_date")
    val expiryDate: Long,
    @SerializedName("issuer")
    val issuer: String,
    @SerializedName("days_until_expiry")
    val daysUntilExpiry: Int
) {
    fun toDomain(): SslInfo = SslInfo(
        isValid = isValid,
        expiryDate = expiryDate,
        issuer = issuer,
        daysUntilExpiry = daysUntilExpiry
    )
}

data class WordPressInfoDto(
    @SerializedName("core_version")
    val coreVersion: String,
    @SerializedName("has_updates")
    val hasUpdates: Boolean,
    @SerializedName("plugins")
    val plugins: List<PluginDto>,
    @SerializedName("themes")
    val themes: List<ThemeDto>,
    @SerializedName("security_issues")
    val securityIssues: List<SecurityIssueDto>
) {
    fun toDomain(): WordPressInfo = WordPressInfo(
        coreVersion = coreVersion,
        hasUpdates = hasUpdates,
        plugins = plugins.map { it.toDomain() },
        themes = themes.map { it.toDomain() },
        securityIssues = securityIssues.map { it.toDomain() }
    )
}

data class PluginDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("version")
    val version: String,
    @SerializedName("latest_version")
    val latestVersion: String?,
    @SerializedName("has_update")
    val hasUpdate: Boolean,
    @SerializedName("is_active")
    val isActive: Boolean
) {
    fun toDomain(): Plugin = Plugin(
        name = name,
        version = version,
        latestVersion = latestVersion,
        hasUpdate = hasUpdate,
        isActive = isActive
    )
}

data class ThemeDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("version")
    val version: String,
    @SerializedName("latest_version")
    val latestVersion: String?,
    @SerializedName("has_update")
    val hasUpdate: Boolean,
    @SerializedName("is_active")
    val isActive: Boolean
) {
    fun toDomain(): Theme = Theme(
        name = name,
        version = version,
        latestVersion = latestVersion,
        hasUpdate = hasUpdate,
        isActive = isActive
    )
}

data class SecurityIssueDto(
    @SerializedName("type")
    val type: String,
    @SerializedName("severity")
    val severity: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("recommendation")
    val recommendation: String
) {
    fun toDomain(): SecurityIssue = SecurityIssue(
        type = type,
        severity = severity,
        description = description,
        recommendation = recommendation
    )
}

data class SeoInfoDto(
    @SerializedName("health_score")
    val healthScore: Int,
    @SerializedName("keywords")
    val keywords: List<KeywordRankingDto>,
    @SerializedName("backlinks_count")
    val backlinksCount: Int,
    @SerializedName("domain_authority")
    val domainAuthority: Int,
    @SerializedName("google_search_console")
    val googleSearchConsole: GoogleSearchConsoleDataDto?
) {
    fun toDomain(): SeoInfo = SeoInfo(
        healthScore = healthScore,
        keywords = keywords.map { it.toDomain() },
        backlinksCount = backlinksCount,
        domainAuthority = domainAuthority,
        googleSearchConsole = googleSearchConsole?.toDomain()
    )
}

data class KeywordRankingDto(
    @SerializedName("keyword")
    val keyword: String,
    @SerializedName("position")
    val position: Int,
    @SerializedName("previous_position")
    val previousPosition: Int?,
    @SerializedName("trend")
    val trend: String,
    @SerializedName("search_volume")
    val searchVolume: Int?
) {
    fun toDomain(): KeywordRanking = KeywordRanking(
        keyword = keyword,
        position = position,
        previousPosition = previousPosition,
        trend = try {
            RankingTrend.valueOf(trend.uppercase())
        } catch (e: IllegalArgumentException) {
            RankingTrend.STABLE
        },
        searchVolume = searchVolume
    )
}

data class GoogleSearchConsoleDataDto(
    @SerializedName("impressions")
    val impressions: Long,
    @SerializedName("clicks")
    val clicks: Long,
    @SerializedName("average_position")
    val averagePosition: Float,
    @SerializedName("ctr")
    val ctr: Float
) {
    fun toDomain(): GoogleSearchConsoleData = GoogleSearchConsoleData(
        impressions = impressions,
        clicks = clicks,
        averagePosition = averagePosition,
        ctr = ctr
    )
}

data class PerformanceMetricsDto(
    @SerializedName("response_time")
    val responseTime: Long,
    @SerializedName("lighthouse_score")
    val lighthouseScore: LighthouseScoreDto,
    @SerializedName("broken_links_count")
    val brokenLinksCount: Int,
    @SerializedName("response_time_history")
    val responseTimeHistory: List<ResponseTimePointDto>
) {
    fun toDomain(): PerformanceMetrics = PerformanceMetrics(
        responseTime = responseTime,
        lighthouseScore = lighthouseScore.toDomain(),
        brokenLinksCount = brokenLinksCount,
        responseTimeHistory = responseTimeHistory.map { it.toDomain() }
    )
}

data class LighthouseScoreDto(
    @SerializedName("performance")
    val performance: Int,
    @SerializedName("accessibility")
    val accessibility: Int,
    @SerializedName("best_practices")
    val bestPractices: Int,
    @SerializedName("seo")
    val seo: Int
) {
    fun toDomain(): LighthouseScore = LighthouseScore(
        performance = performance,
        accessibility = accessibility,
        bestPractices = bestPractices,
        seo = seo
    )
}

data class ResponseTimePointDto(
    @SerializedName("timestamp")
    val timestamp: Long,
    @SerializedName("response_time")
    val responseTime: Long
) {
    fun toDomain(): ResponseTimePoint = ResponseTimePoint(
        timestamp = timestamp,
        responseTime = responseTime
    )
}

data class TriggerCheckResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("check_id")
    val checkId: String?
)
