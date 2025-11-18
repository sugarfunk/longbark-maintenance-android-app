package com.longbark.maintenance.domain.repository

import com.longbark.maintenance.domain.model.Site
import com.longbark.maintenance.domain.model.SiteDetails
import com.longbark.maintenance.util.Result
import kotlinx.coroutines.flow.Flow

interface SiteRepository {
    fun getAllSites(): Flow<List<Site>>
    fun getSitesByClient(clientId: String): Flow<List<Site>>
    suspend fun getSiteById(siteId: String): Result<SiteDetails>
    suspend fun triggerSiteCheck(siteId: String): Result<Boolean>
    suspend fun triggerAllChecks(): Result<Boolean>
    suspend fun refreshSites(): Result<Unit>
}
