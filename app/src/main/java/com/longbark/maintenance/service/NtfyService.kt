package com.longbark.maintenance.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.longbark.maintenance.MainApplication
import com.longbark.maintenance.R
import com.longbark.maintenance.presentation.MainActivity
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

@AndroidEntryPoint
class NtfyService : Service() {

    @Inject
    lateinit var okHttpClient: OkHttpClient

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var connectionJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        startForeground(NOTIFICATION_ID, createForegroundNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startNtfyConnection()
            ACTION_STOP -> stopSelf()
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        connectionJob?.cancel()
        serviceScope.cancel()
    }

    private fun startNtfyConnection() {
        connectionJob?.cancel()
        connectionJob = serviceScope.launch {
            try {
                // TODO: Get NTFY server URL and topics from preferences
                val ntfyServerUrl = "https://ntfy.sh"
                val topic = "longbark-notifications"

                val request = Request.Builder()
                    .url("$ntfyServerUrl/$topic/json")
                    .build()

                okHttpClient.newCall(request).execute().use { response ->
                    response.body?.source()?.let { source ->
                        while (!source.exhausted() && isActive) {
                            val line = source.readUtf8Line()
                            line?.let { handleNtfyMessage(it) }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Retry connection after delay
                delay(5000)
                if (isActive) {
                    startNtfyConnection()
                }
            }
        }
    }

    private fun handleNtfyMessage(message: String) {
        try {
            val ntfyMessage = com.google.gson.Gson().fromJson(message, NtfyMessage::class.java)

            // Only process message events
            if (ntfyMessage.event != "message") return

            val title = ntfyMessage.title ?: "LongBark Alert"
            val channelId = when (ntfyMessage.priority) {
                4, 5 -> MainApplication.CHANNEL_CRITICAL
                3 -> MainApplication.CHANNEL_WARNING
                else -> MainApplication.CHANNEL_INFO
            }

            showNotification(
                title = title,
                message = ntfyMessage.message,
                channelId = channelId,
                deepLink = ntfyMessage.click
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createForegroundNotification(): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, MainApplication.CHANNEL_NTFY_SERVICE)
            .setContentTitle("LongBark Monitoring")
            .setContentText("Listening for site alerts")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    private fun showNotification(
        title: String,
        message: String,
        channelId: String = MainApplication.CHANNEL_CRITICAL,
        deepLink: String? = null
    ) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val intent = if (deepLink != null) {
            Intent(Intent.ACTION_VIEW, android.net.Uri.parse(deepLink)).apply {
                `package` = packageName
            }
        } else {
            Intent(this, MainActivity::class.java)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .build()

        notificationManager.notify(
            System.currentTimeMillis().toInt(),
            notification
        )
    }

    companion object {
        private const val NOTIFICATION_ID = 1
        const val ACTION_START = "com.longbark.maintenance.service.ACTION_START"
        const val ACTION_STOP = "com.longbark.maintenance.service.ACTION_STOP"
    }
}
