package com.longbark.maintenance.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.longbark.maintenance.data.local.preferences.AppPreferences
import com.longbark.maintenance.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appPreferences: AppPreferences,
    private val authRepository: AuthRepository
) : ViewModel() {

    val themeMode = appPreferences.themeMode
    val biometricEnabled = appPreferences.biometricEnabled
    val notificationsEnabled = appPreferences.notificationsEnabled
    val ntfyEnabled = appPreferences.ntfyEnabled
    val ntfyServerUrl = appPreferences.ntfyServerUrl
    val invoiceNinjaUrl = appPreferences.invoiceNinjaUrl
    val invoiceNinjaEnabled = appPreferences.invoiceNinjaEnabled
    val syncInterval = appPreferences.syncInterval
    val wifiOnlySync = appPreferences.wifiOnlySync

    fun setThemeMode(mode: String) {
        viewModelScope.launch {
            appPreferences.setThemeMode(mode)
        }
    }

    fun setBiometricEnabled(enabled: Boolean) {
        viewModelScope.launch {
            appPreferences.setBiometricEnabled(enabled)
        }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            appPreferences.setNotificationsEnabled(enabled)
        }
    }

    fun setNtfyEnabled(enabled: Boolean) {
        viewModelScope.launch {
            appPreferences.setNtfyEnabled(enabled)
        }
    }

    fun setNtfyServerUrl(url: String) {
        viewModelScope.launch {
            appPreferences.setNtfyServerUrl(url)
        }
    }

    fun setInvoiceNinjaUrl(url: String) {
        viewModelScope.launch {
            appPreferences.setInvoiceNinjaUrl(url)
        }
    }

    fun setInvoiceNinjaApiToken(token: String) {
        viewModelScope.launch {
            appPreferences.setInvoiceNinjaApiToken(token)
        }
    }

    fun setInvoiceNinjaEnabled(enabled: Boolean) {
        viewModelScope.launch {
            appPreferences.setInvoiceNinjaEnabled(enabled)
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
