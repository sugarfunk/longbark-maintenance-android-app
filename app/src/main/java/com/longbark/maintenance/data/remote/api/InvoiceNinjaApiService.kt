package com.longbark.maintenance.data.remote.api

import com.longbark.maintenance.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface InvoiceNinjaApiService {
    // Clients
    @GET("clients")
    suspend fun getClients(
        @Query("per_page") perPage: Int = 100,
        @Query("page") page: Int = 1
    ): Response<InvoiceNinjaClientsResponse>

    @GET("clients/{id}")
    suspend fun getClientById(
        @Path("id") clientId: String,
        @Query("include") include: String = "invoices,payments,quotes"
    ): Response<InvoiceNinjaClientDto>

    // Invoices
    @GET("invoices")
    suspend fun getInvoices(
        @Query("client_id") clientId: String? = null,
        @Query("per_page") perPage: Int = 100,
        @Query("page") page: Int = 1,
        @Query("status") status: String? = null
    ): Response<InvoicesResponse>

    @GET("invoices/{id}")
    suspend fun getInvoiceById(@Path("id") invoiceId: String): Response<InvoiceDto>

    // Payments
    @GET("payments")
    suspend fun getPayments(
        @Query("client_id") clientId: String? = null,
        @Query("per_page") perPage: Int = 100,
        @Query("page") page: Int = 1
    ): Response<PaymentsResponse>

    @GET("payments/{id}")
    suspend fun getPaymentById(@Path("id") paymentId: String): Response<PaymentDto>

    // Quotes
    @GET("quotes")
    suspend fun getQuotes(
        @Query("client_id") clientId: String? = null,
        @Query("per_page") perPage: Int = 100,
        @Query("page") page: Int = 1
    ): Response<QuotesResponse>

    @GET("quotes/{id}")
    suspend fun getQuoteById(@Path("id") quoteId: String): Response<QuoteDto>
}
