package com.longbark.maintenance.domain.model

data class CheckHistory(
    val id: String,
    val siteId: String,
    val timestamp: Long,
    val status: CheckStatus,
    val responseTime: Long?,
    val statusCode: Int?,
    val errorMessage: String?,
    val details: String?
)

enum class CheckStatus {
    SUCCESS,
    FAILED,
    TIMEOUT,
    SSL_ERROR,
    DNS_ERROR,
    UNKNOWN
}
