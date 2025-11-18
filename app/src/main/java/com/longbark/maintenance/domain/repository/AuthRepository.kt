package com.longbark.maintenance.domain.repository

import com.longbark.maintenance.domain.model.User
import com.longbark.maintenance.util.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun logout(): Result<Unit>
    suspend fun refreshToken(): Result<Unit>
    fun isLoggedIn(): Flow<Boolean>
    fun getCurrentUserId(): Flow<String?>
}
