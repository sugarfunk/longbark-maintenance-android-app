package com.longbark.maintenance.di

import com.longbark.maintenance.data.repository.*
import com.longbark.maintenance.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindClientRepository(
        clientRepositoryImpl: ClientRepositoryImpl
    ): ClientRepository

    @Binds
    @Singleton
    abstract fun bindSiteRepository(
        siteRepositoryImpl: SiteRepositoryImpl
    ): SiteRepository

    @Binds
    @Singleton
    abstract fun bindDashboardRepository(
        dashboardRepositoryImpl: DashboardRepositoryImpl
    ): DashboardRepository

    @Binds
    @Singleton
    abstract fun bindInvoiceNinjaRepository(
        invoiceNinjaRepositoryImpl: InvoiceNinjaRepositoryImpl
    ): InvoiceNinjaRepository
}
