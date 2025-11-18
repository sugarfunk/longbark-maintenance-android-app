package com.longbark.maintenance.data.local.dao

import androidx.room.*
import com.longbark.maintenance.data.local.entities.SiteInvoiceNinjaLinkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SiteInvoiceNinjaLinkDao {
    @Query("SELECT * FROM site_invoice_ninja_links WHERE siteId = :siteId LIMIT 1")
    suspend fun getLinkBySiteId(siteId: String): SiteInvoiceNinjaLinkEntity?

    @Query("SELECT * FROM site_invoice_ninja_links WHERE siteId = :siteId LIMIT 1")
    fun getLinkBySiteIdFlow(siteId: String): Flow<SiteInvoiceNinjaLinkEntity?>

    @Query("SELECT * FROM site_invoice_ninja_links WHERE invoiceNinjaClientId = :clientId")
    fun getSitesByInvoiceNinjaClient(clientId: String): Flow<List<SiteInvoiceNinjaLinkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLink(link: SiteInvoiceNinjaLinkEntity)

    @Delete
    suspend fun deleteLink(link: SiteInvoiceNinjaLinkEntity)

    @Query("DELETE FROM site_invoice_ninja_links WHERE siteId = :siteId")
    suspend fun deleteLinkBySiteId(siteId: String)
}
