package com.longbark.maintenance.di

import android.content.Context
import com.longbark.maintenance.BuildConfig
import com.longbark.maintenance.data.local.preferences.AppPreferences
import com.longbark.maintenance.data.remote.api.AuthApiService
import com.longbark.maintenance.data.remote.api.InvoiceNinjaApiService
import com.longbark.maintenance.data.remote.api.LongBarkApiService
import com.longbark.maintenance.data.remote.interceptors.AuthInterceptor
import com.longbark.maintenance.data.remote.interceptors.InvoiceNinjaInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideLongBarkApiService(retrofit: Retrofit): LongBarkApiService {
        return retrofit.create(LongBarkApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideInvoiceNinjaOkHttpClient(
        invoiceNinjaInterceptor: InvoiceNinjaInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        return OkHttpClient.Builder()
            .addInterceptor(invoiceNinjaInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideInvoiceNinjaRetrofit(
        @ApplicationContext context: Context,
        appPreferences: AppPreferences
    ): Retrofit {
        // Use a wrapper to get the base URL dynamically
        val baseUrl = runBlocking {
            appPreferences.invoiceNinjaUrl.first()
        }

        return Retrofit.Builder()
            .baseUrl(if (baseUrl.endsWith("/")) "$baseUrl/api/v1/" else "$baseUrl/api/v1/")
            .client(provideInvoiceNinjaOkHttpClient(InvoiceNinjaInterceptor(appPreferences)))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideInvoiceNinjaApiService(
        appPreferences: AppPreferences
    ): InvoiceNinjaApiService {
        val baseUrl = runBlocking {
            appPreferences.invoiceNinjaUrl.first()
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(if (baseUrl.endsWith("/")) "$baseUrl/api/v1/" else "$baseUrl/api/v1/")
            .client(provideInvoiceNinjaOkHttpClient(InvoiceNinjaInterceptor(appPreferences)))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(InvoiceNinjaApiService::class.java)
    }
}
