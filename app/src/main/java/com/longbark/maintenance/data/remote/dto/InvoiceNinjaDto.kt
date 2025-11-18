package com.longbark.maintenance.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.longbark.maintenance.domain.model.*

data class InvoiceNinjaClientDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("website")
    val website: String?,
    @SerializedName("balance")
    val balance: Double,
    @SerializedName("paid_to_date")
    val paidToDate: Double,
    @SerializedName("credit_balance")
    val creditBalance: Double,
    @SerializedName("last_login")
    val lastLoginAt: Long?,
    @SerializedName("address1")
    val address1: String?,
    @SerializedName("address2")
    val address2: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("postal_code")
    val postalCode: String?,
    @SerializedName("country_id")
    val countryId: String?
) {
    fun toDomain(): InvoiceNinjaClient = InvoiceNinjaClient(
        id = id,
        name = name,
        email = email,
        phone = phone,
        website = website,
        balance = balance,
        paidToDate = paidToDate,
        creditBalance = creditBalance,
        lastLoginAt = lastLoginAt,
        address1 = address1,
        address2 = address2,
        city = city,
        state = state,
        postalCode = postalCode,
        countryId = countryId
    )
}

data class InvoiceNinjaClientsResponse(
    @SerializedName("data")
    val data: List<InvoiceNinjaClientDto>
)

data class InvoiceDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("client_id")
    val clientId: String,
    @SerializedName("number")
    val number: String,
    @SerializedName("po_number")
    val poNumber: String?,
    @SerializedName("date")
    val date: String,
    @SerializedName("due_date")
    val dueDate: String,
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("balance")
    val balance: Double,
    @SerializedName("status_id")
    val statusId: String,
    @SerializedName("is_deleted")
    val isDeleted: Boolean,
    @SerializedName("partial_due_date")
    val partialDueDate: String?,
    @SerializedName("partial")
    val partial: Double?
) {
    fun toDomain(): Invoice {
        val status = when (statusId) {
            "1" -> InvoiceStatus.DRAFT
            "2" -> InvoiceStatus.SENT
            "3" -> InvoiceStatus.VIEWED
            "4" -> InvoiceStatus.PARTIAL
            "5" -> InvoiceStatus.PAID
            "6" -> InvoiceStatus.CANCELLED
            "7" -> InvoiceStatus.REVERSED
            else -> InvoiceStatus.UNKNOWN
        }

        return Invoice(
            id = id,
            clientId = clientId,
            number = number,
            poNumber = poNumber,
            date = parseDate(date),
            dueDate = parseDate(dueDate),
            amount = amount,
            balance = balance,
            statusId = statusId,
            status = status,
            isDeleted = isDeleted,
            isPaid = statusId == "5",
            partialDueDate = partialDueDate?.let { parseDate(it) },
            partial = partial
        )
    }

    private fun parseDate(dateString: String): Long {
        return try {
            java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
                .parse(dateString)?.time ?: 0L
        } catch (e: Exception) {
            0L
        }
    }
}

data class InvoicesResponse(
    @SerializedName("data")
    val data: List<InvoiceDto>
)

data class PaymentDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("client_id")
    val clientId: String,
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("applied")
    val applied: Double,
    @SerializedName("refunded")
    val refunded: Double,
    @SerializedName("date")
    val date: String,
    @SerializedName("transaction_reference")
    val transactionReference: String?,
    @SerializedName("private_notes")
    val privateNotes: String?,
    @SerializedName("status_id")
    val statusId: String,
    @SerializedName("invoices")
    val invoices: List<PaymentInvoiceDto>
) {
    fun toDomain(): Payment = Payment(
        id = id,
        clientId = clientId,
        amount = amount,
        applied = applied,
        refunded = refunded,
        date = parseDate(date),
        transactionReference = transactionReference,
        privateNotes = privateNotes,
        statusId = statusId,
        invoices = invoices.map { it.toDomain() }
    )

    private fun parseDate(dateString: String): Long {
        return try {
            java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
                .parse(dateString)?.time ?: 0L
        } catch (e: Exception) {
            0L
        }
    }
}

data class PaymentInvoiceDto(
    @SerializedName("invoice_id")
    val invoiceId: String,
    @SerializedName("amount")
    val amount: Double
) {
    fun toDomain(): PaymentInvoice = PaymentInvoice(
        invoiceId = invoiceId,
        amount = amount
    )
}

data class PaymentsResponse(
    @SerializedName("data")
    val data: List<PaymentDto>
)

data class QuoteDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("client_id")
    val clientId: String,
    @SerializedName("number")
    val number: String,
    @SerializedName("po_number")
    val poNumber: String?,
    @SerializedName("date")
    val date: String,
    @SerializedName("due_date")
    val dueDate: String,
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("balance")
    val balance: Double,
    @SerializedName("status_id")
    val statusId: String,
    @SerializedName("is_deleted")
    val isDeleted: Boolean
) {
    fun toDomain(): Quote {
        val status = when (statusId) {
            "1" -> QuoteStatus.DRAFT
            "2" -> QuoteStatus.SENT
            "3" -> QuoteStatus.VIEWED
            "4" -> QuoteStatus.APPROVED
            "-1" -> QuoteStatus.EXPIRED
            "-2" -> QuoteStatus.CONVERTED
            else -> QuoteStatus.UNKNOWN
        }

        return Quote(
            id = id,
            clientId = clientId,
            number = number,
            poNumber = poNumber,
            date = parseDate(date),
            dueDate = parseDate(dueDate),
            amount = amount,
            balance = balance,
            statusId = statusId,
            status = status,
            isDeleted = isDeleted
        )
    }

    private fun parseDate(dateString: String): Long {
        return try {
            java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
                .parse(dateString)?.time ?: 0L
        } catch (e: Exception) {
            0L
        }
    }
}

data class QuotesResponse(
    @SerializedName("data")
    val data: List<QuoteDto>
)
