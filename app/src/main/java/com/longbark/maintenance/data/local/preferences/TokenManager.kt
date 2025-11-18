package com.longbark.maintenance.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferencesKeys {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val TOKEN_EXPIRY = longPreferencesKey("token_expiry")
        val USER_ID = stringPreferencesKey("user_id")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_NAME = stringPreferencesKey("user_name")
    }

    fun getAccessToken(): Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.ACCESS_TOKEN]
    }

    fun getRefreshToken(): Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.REFRESH_TOKEN]
    }

    fun getUserId(): Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_ID]
    }

    fun isLoggedIn(): Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.ACCESS_TOKEN] != null
    }

    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String?,
        expiresIn: Long,
        userId: String,
        userEmail: String,
        userName: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ACCESS_TOKEN] = accessToken
            if (refreshToken != null) {
                preferences[PreferencesKeys.REFRESH_TOKEN] = refreshToken
            }
            preferences[PreferencesKeys.TOKEN_EXPIRY] = System.currentTimeMillis() + (expiresIn * 1000)
            preferences[PreferencesKeys.USER_ID] = userId
            preferences[PreferencesKeys.USER_EMAIL] = userEmail
            preferences[PreferencesKeys.USER_NAME] = userName
        }
    }

    suspend fun clearTokens() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
