package com.longbark.maintenance.domain.model

data class InvoiceNinjaClient(
    val id: String,
    val name: String,
    val email: String?,
    val phone: String?,
    val website: String?,
    val balance: Double,
    val paidToDate: Double,
    val creditBalance: Double,
    val lastLoginAt: Long?,
    val address1: String?,
    val address2: String?,
    val city: String?,
    val state: String?,
    val postalCode: String?,
    val countryId: String?
)

data class Invoice(
    val id: String,
    val clientId: String,
    val number: String,
    val poNumber: String?,
    val date: Long,
    val dueDate: Long,
    val amount: Double,
    val balance: Double,
    val statusId: String,
    val status: InvoiceStatus,
    val isDeleted: Boolean,
    val isPaid: Boolean,
    val partialDueDate: Long?,
    val partial: Double?
)

enum class InvoiceStatus {
    DRAFT,
    SENT,
    VIEWED,
    PARTIAL,
    PAID,
    CANCELLED,
    REVERSED,
    UNKNOWN
}

data class Payment(
    val id: String,
    val clientId: String,
    val amount: Double,
    val applied: Double,
    val refunded: Double,
    val date: Long,
    val transactionReference: String?,
    val privateNotes: String?,
    val statusId: String,
    val invoices: List<PaymentInvoice>
)

data class PaymentInvoice(
    val invoiceId: String,
    val amount: Double
)

data class Quote(
    val id: String,
    val clientId: String,
    val number: String,
    val poNumber: String?,
    val date: Long,
    val dueDate: Long,
    val amount: Double,
    val balance: Double,
    val statusId: String,
    val status: QuoteStatus,
    val isDeleted: Boolean
)

enum class QuoteStatus {
    DRAFT,
    SENT,
    VIEWED,
    APPROVED,
    EXPIRED,
    CONVERTED,
    UNKNOWN
}

data class BillingStats(
    val totalRevenue: Double,
    val unpaidInvoices: Double,
    val overdueInvoices: Double,
    val averageInvoice: Double,
    val outstandingQuotes: Double
)
