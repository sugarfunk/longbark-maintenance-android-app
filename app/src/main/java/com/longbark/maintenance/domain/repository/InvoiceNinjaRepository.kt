package com.longbark.maintenance.domain.repository

import com.longbark.maintenance.domain.model.*
import com.longbark.maintenance.util.Result
import kotlinx.coroutines.flow.Flow

interface InvoiceNinjaRepository {
    suspend fun getClients(): Result<List<InvoiceNinjaClient>>
    suspend fun getClientById(clientId: String): Result<InvoiceNinjaClient>
    suspend fun getInvoicesByClient(clientId: String): Result<List<Invoice>>
    suspend fun getPaymentsByClient(clientId: String): Result<List<Payment>>
    suspend fun getQuotesByClient(clientId: String): Result<List<Quote>>
    suspend fun getOverdueInvoicesByClient(clientId: String): Result<List<Invoice>>
    suspend fun getBillingStats(clientId: String): Result<BillingStats>

    // Site linking
    suspend fun linkSiteToInvoiceNinjaClient(siteId: String, invoiceNinjaClientId: String)
    suspend fun unlinkSite(siteId: String)
    suspend fun getInvoiceNinjaClientIdForSite(siteId: String): String?
    fun getInvoiceNinjaClientIdForSiteFlow(siteId: String): Flow<String?>
}
