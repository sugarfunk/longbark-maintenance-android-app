package com.longbark.maintenance.presentation.client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.longbark.maintenance.domain.model.Client
import com.longbark.maintenance.domain.repository.ClientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientListViewModel @Inject constructor(
    private val clientRepository: ClientRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ClientListUiState>(ClientListUiState.Loading)
    val uiState: StateFlow<ClientListUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadClients()
    }

    fun loadClients() {
        viewModelScope.launch {
            clientRepository.getAllClients().collect { clients ->
                _uiState.value = ClientListUiState.Success(filterClients(clients))
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = ClientListUiState.Loading
            clientRepository.refreshClients()
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            clientRepository.getAllClients().collect { clients ->
                _uiState.value = ClientListUiState.Success(filterClients(clients))
            }
        }
    }

    private fun filterClients(clients: List<Client>): List<Client> {
        val query = _searchQuery.value
        return if (query.isBlank()) {
            clients
        } else {
            clients.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }
    }
}

sealed class ClientListUiState {
    data object Loading : ClientListUiState()
    data class Success(val clients: List<Client>) : ClientListUiState()
    data class Error(val message: String) : ClientListUiState()
}
