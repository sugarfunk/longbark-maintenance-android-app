package com.longbark.maintenance.data.repository

import com.longbark.maintenance.data.local.preferences.TokenManager
import com.longbark.maintenance.data.remote.api.AuthApiService
import com.longbark.maintenance.data.remote.dto.LoginRequest
import com.longbark.maintenance.domain.model.User
import com.longbark.maintenance.domain.repository.AuthRepository
import com.longbark.maintenance.util.Result
import com.longbark.maintenance.util.safeApiCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<User> {
        val result = safeApiCall {
            authApiService.login(LoginRequest(email, password))
        }

        return when (result) {
            is Result.Success -> {
                val response = result.data
                tokenManager.saveTokens(
                    accessToken = response.accessToken,
                    refreshToken = response.refreshToken,
                    expiresIn = response.expiresIn,
                    userId = response.user.id,
                    userEmail = response.user.email,
                    userName = response.user.name
                )
                Result.Success(
                    User(
                        id = response.user.id,
                        email = response.user.email,
                        name = response.user.name,
                        createdAt = response.user.createdAt
                    )
                )
            }
            is Result.Error -> result
            is Result.Loading -> result
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            safeApiCall { authApiService.logout() }
            tokenManager.clearTokens()
            Result.Success(Unit)
        } catch (e: Exception) {
            // Even if API call fails, clear local tokens
            tokenManager.clearTokens()
            Result.Success(Unit)
        }
    }

    override suspend fun refreshToken(): Result<Unit> {
        // TODO: Implement token refresh logic
        return Result.Success(Unit)
    }

    override fun isLoggedIn(): Flow<Boolean> = tokenManager.isLoggedIn()

    override fun getCurrentUserId(): Flow<String?> = tokenManager.getUserId()
}
