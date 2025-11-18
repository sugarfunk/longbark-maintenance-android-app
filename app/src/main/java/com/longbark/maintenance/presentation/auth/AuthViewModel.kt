package com.longbark.maintenance.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.longbark.maintenance.domain.repository.AuthRepository
import com.longbark.maintenance.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val isLoggedIn = authRepository.isLoggedIn()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            when (val result = authRepository.login(email, password)) {
                is Result.Success -> {
                    _loginState.value = LoginState.Success
                }
                is Result.Error -> {
                    _loginState.value = LoginState.Error(
                        result.message ?: "Login failed"
                    )
                }
                is Result.Loading -> {
                    _loginState.value = LoginState.Loading
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    fun resetLoginState() {
        _loginState.value = LoginState.Idle
    }
}

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data object Success : LoginState()
    data class Error(val message: String) : LoginState()
}
