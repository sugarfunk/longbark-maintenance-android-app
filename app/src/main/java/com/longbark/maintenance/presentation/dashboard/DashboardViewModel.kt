package com.longbark.maintenance.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.longbark.maintenance.domain.model.DashboardStats
import com.longbark.maintenance.domain.repository.DashboardRepository
import com.longbark.maintenance.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboard()
    }

    fun loadDashboard() {
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            when (val result = dashboardRepository.getDashboardStats()) {
                is Result.Success -> {
                    _uiState.value = DashboardUiState.Success(result.data)
                }
                is Result.Error -> {
                    _uiState.value = DashboardUiState.Error(
                        result.message ?: "Failed to load dashboard"
                    )
                }
                is Result.Loading -> {
                    _uiState.value = DashboardUiState.Loading
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            dashboardRepository.refreshDashboard()
            loadDashboard()
        }
    }
}

sealed class DashboardUiState {
    data object Loading : DashboardUiState()
    data class Success(val stats: DashboardStats) : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}
