package com.longbark.maintenance.data.repository

import com.longbark.maintenance.data.local.dao.SiteDao
import com.longbark.maintenance.data.local.entities.SiteEntity
import com.longbark.maintenance.data.remote.api.LongBarkApiService
import com.longbark.maintenance.domain.model.Site
import com.longbark.maintenance.domain.model.SiteDetails
import com.longbark.maintenance.domain.repository.SiteRepository
import com.longbark.maintenance.util.Result
import com.longbark.maintenance.util.safeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SiteRepositoryImpl @Inject constructor(
    private val apiService: LongBarkApiService,
    private val siteDao: SiteDao
) : SiteRepository {

    override fun getAllSites(): Flow<List<Site>> =
        siteDao.getAllSites().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getSitesByClient(clientId: String): Flow<List<Site>> =
        siteDao.getSitesByClient(clientId).map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun getSiteById(siteId: String): Result<SiteDetails> {
        return when (val result = safeApiCall { apiService.getSiteById(siteId) }) {
            is Result.Success -> {
                val siteDetails = result.data.toDomain()
                // Cache the site data
                siteDao.insertSite(SiteEntity.fromDomain(siteDetails.site))
                Result.Success(siteDetails)
            }
            is Result.Error -> {
                // Try to get from local cache
                val cachedSite = siteDao.getSiteById(siteId)
                if (cachedSite != null) {
                    Result.Success(
                        SiteDetails(
                            site = cachedSite.toDomain(),
                            sslInfo = null,
                            wordPressInfo = null,
                            seoInfo = null,
                            performanceMetrics = null
                        )
                    )
                } else {
                    result
                }
            }
            is Result.Loading -> result
        }
    }

    override suspend fun triggerSiteCheck(siteId: String): Result<Boolean> {
        return when (val result = safeApiCall { apiService.triggerSiteCheck(siteId) }) {
            is Result.Success -> Result.Success(result.data.success)
            is Result.Error -> result
            is Result.Loading -> result
        }
    }

    override suspend fun triggerAllChecks(): Result<Boolean> {
        return when (val result = safeApiCall { apiService.triggerAllChecks() }) {
            is Result.Success -> Result.Success(result.data.success)
            is Result.Error -> result
            is Result.Loading -> result
        }
    }

    override suspend fun refreshSites(): Result<Unit> {
        return when (val result = safeApiCall { apiService.getSites() }) {
            is Result.Success -> {
                val sites = result.data.sites.map { SiteEntity.fromDomain(it.toDomain()) }
                siteDao.insertSites(sites)
                Result.Success(Unit)
            }
            is Result.Error -> result
            is Result.Loading -> result
        }
    }
}
