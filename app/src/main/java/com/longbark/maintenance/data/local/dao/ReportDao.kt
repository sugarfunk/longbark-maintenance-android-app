package com.longbark.maintenance.data.local.dao

import androidx.room.*
import com.longbark.maintenance.data.local.entities.ReportEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {
    @Query("SELECT * FROM reports ORDER BY generatedAt DESC")
    fun getAllReports(): Flow<List<ReportEntity>>

    @Query("SELECT * FROM reports WHERE clientId = :clientId ORDER BY generatedAt DESC")
    fun getReportsByClient(clientId: String): Flow<List<ReportEntity>>

    @Query("SELECT * FROM reports WHERE siteId = :siteId ORDER BY generatedAt DESC")
    fun getReportsBySite(siteId: String): Flow<List<ReportEntity>>

    @Query("SELECT * FROM reports WHERE id = :reportId")
    suspend fun getReportById(reportId: String): ReportEntity?

    @Query("SELECT * FROM reports WHERE type = :type ORDER BY generatedAt DESC")
    fun getReportsByType(type: String): Flow<List<ReportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: ReportEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReports(reports: List<ReportEntity>)

    @Delete
    suspend fun deleteReport(report: ReportEntity)

    @Query("DELETE FROM reports")
    suspend fun deleteAllReports()
}
