package com.longbark.maintenance.domain.model

data class Site(
    val id: String,
    val clientId: String,
    val name: String,
    val url: String,
    val isWordPress: Boolean,
    val uptimePercentage: Float,
    val healthStatus: HealthStatus,
    val lastCheckTimestamp: Long,
    val createdAt: Long,
    val updatedAt: Long
)

data class SiteDetails(
    val site: Site,
    val sslInfo: SslInfo?,
    val wordPressInfo: WordPressInfo?,
    val seoInfo: SeoInfo?,
    val performanceMetrics: PerformanceMetrics?
)

data class SslInfo(
    val isValid: Boolean,
    val expiryDate: Long,
    val issuer: String,
    val daysUntilExpiry: Int
)

data class WordPressInfo(
    val coreVersion: String,
    val hasUpdates: Boolean,
    val plugins: List<Plugin>,
    val themes: List<Theme>,
    val securityIssues: List<SecurityIssue>
)

data class Plugin(
    val name: String,
    val version: String,
    val latestVersion: String?,
    val hasUpdate: Boolean,
    val isActive: Boolean
)

data class Theme(
    val name: String,
    val version: String,
    val latestVersion: String?,
    val hasUpdate: Boolean,
    val isActive: Boolean
)

data class SecurityIssue(
    val type: String,
    val severity: String,
    val description: String,
    val recommendation: String
)

data class SeoInfo(
    val healthScore: Int,
    val keywords: List<KeywordRanking>,
    val backlinksCount: Int,
    val domainAuthority: Int,
    val googleSearchConsole: GoogleSearchConsoleData?
)

data class KeywordRanking(
    val keyword: String,
    val position: Int,
    val previousPosition: Int?,
    val trend: RankingTrend,
    val searchVolume: Int?
)

enum class RankingTrend {
    UP,
    DOWN,
    STABLE,
    NEW
}

data class GoogleSearchConsoleData(
    val impressions: Long,
    val clicks: Long,
    val averagePosition: Float,
    val ctr: Float
)

data class PerformanceMetrics(
    val responseTime: Long,
    val lighthouseScore: LighthouseScore,
    val brokenLinksCount: Int,
    val responseTimeHistory: List<ResponseTimePoint>
)

data class LighthouseScore(
    val performance: Int,
    val accessibility: Int,
    val bestPractices: Int,
    val seo: Int
)

data class ResponseTimePoint(
    val timestamp: Long,
    val responseTime: Long
)
