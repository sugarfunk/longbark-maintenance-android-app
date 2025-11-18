package com.longbark.maintenance.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.longbark.maintenance.data.local.dao.*
import com.longbark.maintenance.data.local.entities.*

@Database(
    entities = [
        ClientEntity::class,
        SiteEntity::class,
        NotificationEntity::class,
        ReportEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class LongBarkDatabase : RoomDatabase() {
    abstract fun clientDao(): ClientDao
    abstract fun siteDao(): SiteDao
    abstract fun notificationDao(): NotificationDao
    abstract fun reportDao(): ReportDao

    companion object {
        const val DATABASE_NAME = "longbark_database"
    }
}
