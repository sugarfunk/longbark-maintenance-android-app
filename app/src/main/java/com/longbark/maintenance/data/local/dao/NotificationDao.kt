package com.longbark.maintenance.data.local.dao

import androidx.room.*
import com.longbark.maintenance.data.local.entities.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Query("SELECT * FROM notifications WHERE isRead = 0 ORDER BY timestamp DESC")
    fun getUnreadNotifications(): Flow<List<NotificationEntity>>

    @Query("SELECT * FROM notifications WHERE severity = :severity ORDER BY timestamp DESC")
    fun getNotificationsBySeverity(severity: String): Flow<List<NotificationEntity>>

    @Query("SELECT * FROM notifications WHERE id = :notificationId")
    suspend fun getNotificationById(notificationId: String): NotificationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)

    @Update
    suspend fun updateNotification(notification: NotificationEntity)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :notificationId")
    suspend fun markAsRead(notificationId: String)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllAsRead()

    @Delete
    suspend fun deleteNotification(notification: NotificationEntity)

    @Query("DELETE FROM notifications WHERE timestamp < :timestamp")
    suspend fun deleteOldNotifications(timestamp: Long)

    @Query("DELETE FROM notifications")
    suspend fun deleteAllNotifications()

    @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
    fun getUnreadCount(): Flow<Int>
}
