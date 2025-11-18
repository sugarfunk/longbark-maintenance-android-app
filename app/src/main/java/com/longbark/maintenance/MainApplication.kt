package com.longbark.maintenance

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Critical notifications channel
        val criticalChannel = NotificationChannel(
            CHANNEL_CRITICAL,
            "Critical Alerts",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Critical site issues like downtime and SSL expiry"
            enableVibration(true)
            enableLights(true)
        }

        // Warning notifications channel
        val warningChannel = NotificationChannel(
            CHANNEL_WARNING,
            "Warnings",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Warning notifications like plugin updates and broken links"
        }

        // Info notifications channel
        val infoChannel = NotificationChannel(
            CHANNEL_INFO,
            "Info",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Informational notifications like reports ready and successful checks"
        }

        // NTFY service channel
        val ntfyChannel = NotificationChannel(
            CHANNEL_NTFY_SERVICE,
            "NTFY Service",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Persistent notification for NTFY background service"
            setShowBadge(false)
        }

        notificationManager.createNotificationChannels(
            listOf(criticalChannel, warningChannel, infoChannel, ntfyChannel)
        )
    }

    companion object {
        const val CHANNEL_CRITICAL = "critical_notifications"
        const val CHANNEL_WARNING = "warning_notifications"
        const val CHANNEL_INFO = "info_notifications"
        const val CHANNEL_NTFY_SERVICE = "ntfy_service"
    }
}
