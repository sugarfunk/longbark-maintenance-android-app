package com.longbark.maintenance.di

import android.content.Context
import androidx.room.Room
import com.longbark.maintenance.data.local.dao.*
import com.longbark.maintenance.data.local.database.LongBarkDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideLongBarkDatabase(
        @ApplicationContext context: Context
    ): LongBarkDatabase {
        // Use SQLCipher for encrypted database
        val passphrase = SQLiteDatabase.getBytes("longbark_secure_key_2024".toCharArray())
        val factory = SupportFactory(passphrase)

        return Room.databaseBuilder(
            context,
            LongBarkDatabase::class.java,
            LongBarkDatabase.DATABASE_NAME
        )
            .openHelperFactory(factory)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideClientDao(database: LongBarkDatabase): ClientDao = database.clientDao()

    @Provides
    fun provideSiteDao(database: LongBarkDatabase): SiteDao = database.siteDao()

    @Provides
    fun provideNotificationDao(database: LongBarkDatabase): NotificationDao = database.notificationDao()

    @Provides
    fun provideReportDao(database: LongBarkDatabase): ReportDao = database.reportDao()
}
