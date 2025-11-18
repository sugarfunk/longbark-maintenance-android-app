package com.longbark.maintenance.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "site_invoice_ninja_links",
    indices = [Index("siteId"), Index("invoiceNinjaClientId")]
)
data class SiteInvoiceNinjaLinkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val siteId: String,
    val invoiceNinjaClientId: String,
    val createdAt: Long = System.currentTimeMillis()
)
