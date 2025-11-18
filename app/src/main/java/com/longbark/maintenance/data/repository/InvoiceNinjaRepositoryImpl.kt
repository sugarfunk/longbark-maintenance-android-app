package com.longbark.maintenance.data.repository

import com.longbark.maintenance.data.local.dao.SiteInvoiceNinjaLinkDao
import com.longbark.maintenance.data.local.entities.SiteInvoiceNinjaLinkEntity
import com.longbark.maintenance.data.remote.api.InvoiceNinjaApiService
import com.longbark.maintenance.domain.model.*
import com.longbark.maintenance.domain.repository.InvoiceNinjaRepository
import com.longbark.maintenance.util.Result
import com.longbark.maintenance.util.safeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class InvoiceNinjaRepositoryImpl @Inject constructor(
    private val apiService: InvoiceNinjaApiService,
    private val linkDao: SiteInvoiceNinjaLinkDao
) : InvoiceNinjaRepository {

    override suspend fun getClients(): Result<List<InvoiceNinjaClient>> {
        return when (val result = safeApiCall { apiService.getClients() }) {
            is Result.Success -> Result.Success(result.data.data.map { it.toDomain() })
            is Result.Error -> result
            is Result.Loading -> result
        }
    }

    override suspend fun getClientById(clientId: String): Result<InvoiceNinjaClient> {
        return when (val result = safeApiCall { apiService.getClientById(clientId) }) {
            is Result.Success -> Result.Success(result.data.toDomain())
            is Result.Error -> result
            is Result.Loading -> result
        }
    }

    override suspend fun getInvoicesByClient(clientId: String): Result<List<Invoice>> {
        return when (val result = safeApiCall { apiService.getInvoices(clientId = clientId) }) {
            is Result.Success -> Result.Success(result.data.data.map { it.toDomain() })
            is Result.Error -> result
            is Result.Loading -> result
        }
    }

    override suspend fun getPaymentsByClient(clientId: String): Result<List<Payment>> {
        return when (val result = safeApiCall { apiService.getPayments(clientId = clientId) }) {
            is Result.Success -> Result.Success(result.data.data.map { it.toDomain() })
            is Result.Error -> result
            is Result.Loading -> result
        }
    }

    override suspend fun getQuotesByClient(clientId: String): Result<List<Quote>> {
        return when (val result = safeApiCall { apiService.getQuotes(clientId = clientId) }) {
            is Result.Success -> Result.Success(result.data.data.map { it.toDomain() })
            is Result.Error -> result
            is Result.Loading -> result
        }
    }

    override suspend fun getOverdueInvoicesByClient(clientId: String): Result<List<Invoice>> {
        return when (val result = getInvoicesByClient(clientId)) {
            is Result.Success -> {
                val overdueInvoices = result.data.filter { invoice ->
                    !invoice.isPaid && invoice.dueDate < System.currentTimeMillis()
                }
                Result.Success(overdueInvoices)
            }
            is Result.Error -> result
            is Result.Loading -> result
        }
    }

    override suspend fun getBillingStats(clientId: String): Result<BillingStats> {
        return try {
            val clientResult = getClientById(clientId)
            val invoicesResult = getInvoicesByClient(clientId)
            val quotesResult = getQuotesByClient(clientId)

            if (clientResult is Result.Success && invoicesResult is Result.Success && quotesResult is Result.Success) {
                val client = clientResult.data
                val invoices = invoicesResult.data

                val unpaidInvoices = invoices.filter { !it.isPaid }.sumOf { it.balance }
                val overdueInvoices = invoices.filter {
                    !it.isPaid && it.dueDate < System.currentTimeMillis()
                }.sumOf { it.balance }

                val averageInvoice = if (invoices.isNotEmpty()) {
                    invoices.sumOf { it.amount } / invoices.size
                } else {
                    0.0
                }

                val outstandingQuotes = quotesResult.data
                    .filter { it.status != QuoteStatus.CONVERTED && it.status != QuoteStatus.EXPIRED }
                    .sumOf { it.amount }

                Result.Success(
                    BillingStats(
                        totalRevenue = client.paidToDate,
                        unpaidInvoices = unpaidInvoices,
                        overdueInvoices = overdueInvoices,
                        averageInvoice = averageInvoice,
                        outstandingQuotes = outstandingQuotes
                    )
                )
            } else {
                Result.Error(Exception("Failed to fetch billing stats"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun linkSiteToInvoiceNinjaClient(siteId: String, invoiceNinjaClientId: String) {
        linkDao.insertLink(
            SiteInvoiceNinjaLinkEntity(
                siteId = siteId,
                invoiceNinjaClientId = invoiceNinjaClientId
            )
        )
    }

    override suspend fun unlinkSite(siteId: String) {
        linkDao.deleteLinkBySiteId(siteId)
    }

    override suspend fun getInvoiceNinjaClientIdForSite(siteId: String): String? {
        return linkDao.getLinkBySiteId(siteId)?.invoiceNinjaClientId
    }

    override fun getInvoiceNinjaClientIdForSiteFlow(siteId: String): Flow<String?> {
        return linkDao.getLinkBySiteIdFlow(siteId).map { it?.invoiceNinjaClientId }
    }
}
