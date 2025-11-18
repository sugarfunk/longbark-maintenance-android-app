package com.longbark.maintenance.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.appDataStore: DataStore<Preferences> by preferencesDataStore(name = "app_prefs")

@Singleton
class AppPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferencesKeys {
        // Theme
        val THEME_MODE = stringPreferencesKey("theme_mode") // "light", "dark", "system"

        // Notifications
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val CRITICAL_NOTIFICATIONS = booleanPreferencesKey("critical_notifications")
        val WARNING_NOTIFICATIONS = booleanPreferencesKey("warning_notifications")
        val INFO_NOTIFICATIONS = booleanPreferencesKey("info_notifications")
        val QUIET_HOURS_START = intPreferencesKey("quiet_hours_start")
        val QUIET_HOURS_END = intPreferencesKey("quiet_hours_end")

        // NTFY
        val NTFY_SERVER_URL = stringPreferencesKey("ntfy_server_url")
        val NTFY_ENABLED = booleanPreferencesKey("ntfy_enabled")

        // Sync
        val SYNC_INTERVAL = intPreferencesKey("sync_interval") // minutes
        val WIFI_ONLY_SYNC = booleanPreferencesKey("wifi_only_sync")
        val BACKGROUND_SYNC = booleanPreferencesKey("background_sync")
        val LAST_SYNC_TIMESTAMP = longPreferencesKey("last_sync_timestamp")

        // Security
        val BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")
        val SESSION_TIMEOUT = intPreferencesKey("session_timeout") // minutes
        val LAST_ACTIVITY_TIMESTAMP = longPreferencesKey("last_activity_timestamp")

        // API
        val API_BASE_URL = stringPreferencesKey("api_base_url")
    }

    // Theme
    val themeMode: Flow<String> = context.appDataStore.data.map { preferences ->
        preferences[PreferencesKeys.THEME_MODE] ?: "system"
    }

    suspend fun setThemeMode(mode: String) {
        context.appDataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = mode
        }
    }

    // Notifications
    val notificationsEnabled: Flow<Boolean> = context.appDataStore.data.map { preferences ->
        preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] ?: true
    }

    val criticalNotificationsEnabled: Flow<Boolean> = context.appDataStore.data.map { preferences ->
        preferences[PreferencesKeys.CRITICAL_NOTIFICATIONS] ?: true
    }

    val warningNotificationsEnabled: Flow<Boolean> = context.appDataStore.data.map { preferences ->
        preferences[PreferencesKeys.WARNING_NOTIFICATIONS] ?: true
    }

    val infoNotificationsEnabled: Flow<Boolean> = context.appDataStore.data.map { preferences ->
        preferences[PreferencesKeys.INFO_NOTIFICATIONS] ?: true
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.appDataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] = enabled
        }
    }

    // NTFY
    val ntfyServerUrl: Flow<String> = context.appDataStore.data.map { preferences ->
        preferences[PreferencesKeys.NTFY_SERVER_URL] ?: "https://ntfy.sh/"
    }

    val ntfyEnabled: Flow<Boolean> = context.appDataStore.data.map { preferences ->
        preferences[PreferencesKeys.NTFY_ENABLED] ?: true
    }

    suspend fun setNtfyServerUrl(url: String) {
        context.appDataStore.edit { preferences ->
            preferences[PreferencesKeys.NTFY_SERVER_URL] = url
        }
    }

    suspend fun setNtfyEnabled(enabled: Boolean) {
        context.appDataStore.edit { preferences ->
            preferences[PreferencesKeys.NTFY_ENABLED] = enabled
        }
    }

    // Sync
    val syncInterval: Flow<Int> = context.appDataStore.data.map { preferences ->
        preferences[PreferencesKeys.SYNC_INTERVAL] ?: 15
    }

    val wifiOnlySync: Flow<Boolean> = context.appDataStore.data.map { preferences ->
        preferences[PreferencesKeys.WIFI_ONLY_SYNC] ?: false
    }

    val backgroundSync: Flow<Boolean> = context.appDataStore.data.map { preferences ->
        preferences[PreferencesKeys.BACKGROUND_SYNC] ?: true
    }

    val lastSyncTimestamp: Flow<Long?> = context.appDataStore.data.map { preferences ->
        preferences[PreferencesKeys.LAST_SYNC_TIMESTAMP]
    }

    suspend fun updateLastSyncTimestamp(timestamp: Long) {
        context.appDataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_SYNC_TIMESTAMP] = timestamp
        }
    }

    // Security
    val biometricEnabled: Flow<Boolean> = context.appDataStore.data.map { preferences ->
        preferences[PreferencesKeys.BIOMETRIC_ENABLED] ?: false
    }

    val sessionTimeout: Flow<Int> = context.appDataStore.data.map { preferences ->
        preferences[PreferencesKeys.SESSION_TIMEOUT] ?: 30
    }

    suspend fun setBiometricEnabled(enabled: Boolean) {
        context.appDataStore.edit { preferences ->
            preferences[PreferencesKeys.BIOMETRIC_ENABLED] = enabled
        }
    }

    suspend fun updateLastActivityTimestamp(timestamp: Long) {
        context.appDataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_ACTIVITY_TIMESTAMP] = timestamp
        }
    }

    // API
    val apiBaseUrl: Flow<String> = context.appDataStore.data.map { preferences ->
        preferences[PreferencesKeys.API_BASE_URL] ?: "http://localhost:8000/api/"
    }

    suspend fun setApiBaseUrl(url: String) {
        context.appDataStore.edit { preferences ->
            preferences[PreferencesKeys.API_BASE_URL] = url
        }
    }
}
