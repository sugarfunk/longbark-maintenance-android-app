package com.longbark.maintenance.presentation.client

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.longbark.maintenance.domain.model.*
import com.longbark.maintenance.domain.repository.ClientRepository
import com.longbark.maintenance.domain.repository.InvoiceNinjaRepository
import com.longbark.maintenance.domain.repository.SiteRepository
import com.longbark.maintenance.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientDetailViewModel @Inject constructor(
    private val clientRepository: ClientRepository,
    private val siteRepository: SiteRepository,
    private val invoiceNinjaRepository: InvoiceNinjaRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val clientId: String = checkNotNull(savedStateHandle["clientId"])

    private val _uiState = MutableStateFlow<ClientDetailUiState>(ClientDetailUiState.Loading)
    val uiState: StateFlow<ClientDetailUiState> = _uiState.asStateFlow()

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    init {
        loadClientDetails()
    }

    fun loadClientDetails() {
        viewModelScope.launch {
            _uiState.value = ClientDetailUiState.Loading

            when (val clientResult = clientRepository.getClientById(clientId)) {
                is Result.Success -> {
                    val client = clientResult.data

                    // Load sites
                    siteRepository.getSitesByClient(clientId).collect { sites ->
                        // Load Invoice Ninja data if available
                        val invoiceNinjaClientId = getInvoiceNinjaClientId(sites)
                        
                        val billingData = if (invoiceNinjaClientId != null) {
                            loadBillingData(invoiceNinjaClientId)
                        } else {
                            null
                        }

                        _uiState.value = ClientDetailUiState.Success(
                            client = client,
                            sites = sites,
                            billingData = billingData
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.value = ClientDetailUiState.Error(
                        clientResult.message ?: "Failed to load client"
                    )
                }
                is Result.Loading -> {
                    _uiState.value = ClientDetailUiState.Loading
                }
            }
        }
    }

    fun selectTab(index: Int) {
        _selectedTab.value = index
    }

    private suspend fun getInvoiceNinjaClientId(sites: List<Site>): String? {
        return sites.firstOrNull()?.let { site ->
            invoiceNinjaRepository.getInvoiceNinjaClientIdForSite(site.id)
        }
    }

    private suspend fun loadBillingData(invoiceNinjaClientId: String): BillingData? {
        return try {
            val clientResult = invoiceNinjaRepository.getClientById(invoiceNinjaClientId)
            val invoicesResult = invoiceNinjaRepository.getInvoicesByClient(invoiceNinjaClientId)
            val paymentsResult = invoiceNinjaRepository.getPaymentsByClient(invoiceNinjaClientId)
            val quotesResult = invoiceNinjaRepository.getQuotesByClient(invoiceNinjaClientId)
            val statsResult = invoiceNinjaRepository.getBillingStats(invoiceNinjaClientId)

            if (clientResult is Result.Success &&
                invoicesResult is Result.Success &&
                paymentsResult is Result.Success &&
                quotesResult is Result.Success &&
                statsResult is Result.Success
            ) {
                BillingData(
                    invoiceNinjaClient = clientResult.data,
                    invoices = invoicesResult.data,
                    payments = paymentsResult.data,
                    quotes = quotesResult.data,
                    stats = statsResult.data
                )
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}

sealed class ClientDetailUiState {
    data object Loading : ClientDetailUiState()
    data class Success(
        val client: Client,
        val sites: List<Site>,
        val billingData: BillingData?
    ) : ClientDetailUiState()
    data class Error(val message: String) : ClientDetailUiState()
}

data class BillingData(
    val invoiceNinjaClient: InvoiceNinjaClient,
    val invoices: List<Invoice>,
    val payments: List<Payment>,
    val quotes: List<Quote>,
    val stats: BillingStats
)
