package com.longbark.maintenance.presentation.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.longbark.maintenance.domain.model.Report
import com.longbark.maintenance.domain.repository.ClientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val clientRepository: ClientRepository
) : ViewModel() {
    
    private val _reports = MutableStateFlow<List<Report>>(emptyList())
    val reports: StateFlow<List<Report>> = _reports.asStateFlow()
    
    init {
        loadReports()
    }
    
    fun loadReports() {
        viewModelScope.launch {
            // TODO: Implement reports loading from repository
            _reports.value = emptyList()
        }
    }
}
