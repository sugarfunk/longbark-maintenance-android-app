package com.longbark.maintenance.data.local.dao

import androidx.room.*
import com.longbark.maintenance.data.local.entities.SiteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SiteDao {
    @Query("SELECT * FROM sites ORDER BY name ASC")
    fun getAllSites(): Flow<List<SiteEntity>>

    @Query("SELECT * FROM sites WHERE clientId = :clientId ORDER BY name ASC")
    fun getSitesByClient(clientId: String): Flow<List<SiteEntity>>

    @Query("SELECT * FROM sites WHERE id = :siteId")
    suspend fun getSiteById(siteId: String): SiteEntity?

    @Query("SELECT * FROM sites WHERE id = :siteId")
    fun getSiteByIdFlow(siteId: String): Flow<SiteEntity?>

    @Query("SELECT * FROM sites WHERE healthStatus = 'CRITICAL'")
    fun getCriticalSites(): Flow<List<SiteEntity>>

    @Query("SELECT * FROM sites WHERE healthStatus = 'WARNING'")
    fun getWarningSites(): Flow<List<SiteEntity>>

    @Query("SELECT * FROM sites WHERE healthStatus = 'HEALTHY'")
    fun getHealthySites(): Flow<List<SiteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSite(site: SiteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSites(sites: List<SiteEntity>)

    @Update
    suspend fun updateSite(site: SiteEntity)

    @Delete
    suspend fun deleteSite(site: SiteEntity)

    @Query("DELETE FROM sites")
    suspend fun deleteAllSites()

    @Query("SELECT COUNT(*) FROM sites")
    suspend fun getSiteCount(): Int

    @Query("SELECT COUNT(*) FROM sites WHERE healthStatus = 'CRITICAL'")
    suspend fun getCriticalSiteCount(): Int

    @Query("SELECT COUNT(*) FROM sites WHERE healthStatus = 'WARNING'")
    suspend fun getWarningSiteCount(): Int

    @Query("SELECT COUNT(*) FROM sites WHERE healthStatus = 'HEALTHY'")
    suspend fun getHealthySiteCount(): Int
}
