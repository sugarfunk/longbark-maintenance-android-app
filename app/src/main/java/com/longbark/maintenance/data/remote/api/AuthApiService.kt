package com.longbark.maintenance.data.remote.api

import com.longbark.maintenance.data.remote.dto.LoginRequest
import com.longbark.maintenance.data.remote.dto.LoginResponse
import com.longbark.maintenance.data.remote.dto.RefreshTokenRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<LoginResponse>

    @POST("auth/logout")
    suspend fun logout(): Response<Unit>
}
