package com.longbark.maintenance.data.remote.interceptors

import com.longbark.maintenance.data.local.preferences.TokenManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Skip auth for login/refresh endpoints
        if (request.url.encodedPath.contains("/auth/login") ||
            request.url.encodedPath.contains("/auth/refresh")) {
            return chain.proceed(request)
        }

        val token = runBlocking {
            tokenManager.getAccessToken().first()
        }

        val authenticatedRequest = if (token != null) {
            request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            request
        }

        return chain.proceed(authenticatedRequest)
    }
}
