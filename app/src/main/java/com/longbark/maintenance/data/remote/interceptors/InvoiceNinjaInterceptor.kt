package com.longbark.maintenance.data.remote.interceptors

import com.longbark.maintenance.data.local.preferences.AppPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class InvoiceNinjaInterceptor @Inject constructor(
    private val appPreferences: AppPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val token = runBlocking {
            appPreferences.invoiceNinjaApiToken.first()
        }

        val authenticatedRequest = if (token != null) {
            request.newBuilder()
                .addHeader("X-Api-Token", token)
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .build()
        } else {
            request
        }

        return chain.proceed(authenticatedRequest)
    }
}
