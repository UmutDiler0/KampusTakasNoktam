package com.takasr.kampstakasnoktam.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.tokenDataStore: DataStore<Preferences> by preferencesDataStore(name = "token_prefs")

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
    }

    val accessToken: Flow<String?> = context.tokenDataStore.data.map { preferences ->
        preferences[ACCESS_TOKEN]
    }

    suspend fun saveToken(token: String) {
        context.tokenDataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = token
        }
    }

    suspend fun clearToken() {
        context.tokenDataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN)
        }
    }
}
