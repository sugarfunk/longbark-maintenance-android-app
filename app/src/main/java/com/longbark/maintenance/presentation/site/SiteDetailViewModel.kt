package com.longbark.maintenance.presentation.site

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.longbark.maintenance.domain.model.SiteDetails
import com.longbark.maintenance.domain.repository.SiteRepository
import com.longbark.maintenance.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SiteDetailViewModel @Inject constructor(
    private val siteRepository: SiteRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val siteId: String = checkNotNull(savedStateHandle["siteId"])

    private val _uiState = MutableStateFlow<SiteDetailUiState>(SiteDetailUiState.Loading)
    val uiState: StateFlow<SiteDetailUiState> = _uiState.asStateFlow()

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    init {
        loadSiteDetails()
    }

    fun loadSiteDetails() {
        viewModelScope.launch {
            _uiState.value = SiteDetailUiState.Loading
            when (val result = siteRepository.getSiteById(siteId)) {
                is Result.Success -> {
                    _uiState.value = SiteDetailUiState.Success(result.data)
                }
                is Result.Error -> {
                    _uiState.value = SiteDetailUiState.Error(
                        result.message ?: "Failed to load site"
                    )
                }
                is Result.Loading -> {
                    _uiState.value = SiteDetailUiState.Loading
                }
            }
        }
    }

    fun selectTab(index: Int) {
        _selectedTab.value = index
    }

    fun triggerCheck() {
        viewModelScope.launch {
            siteRepository.triggerSiteCheck(siteId)
            loadSiteDetails()
        }
    }
}

sealed class SiteDetailUiState {
    data object Loading : SiteDetailUiState()
    data class Success(val siteDetails: SiteDetails) : SiteDetailUiState()
    data class Error(val message: String) : SiteDetailUiState()
}
