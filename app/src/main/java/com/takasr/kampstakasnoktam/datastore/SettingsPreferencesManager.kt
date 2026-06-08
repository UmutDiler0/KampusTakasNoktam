package com.takasr.kampstakasnoktam.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings_preferences")

@Singleton
class SettingsPreferencesManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    private object PreferencesKeys {
        val IS_DARK_THEME = booleanPreferencesKey("isDarkTheme")
        val LANGUAGE_CODE = stringPreferencesKey("languageCode")
    }

    val isDarkTheme: Flow<Boolean?> = context.settingsDataStore.data.map { preferences ->
        preferences[PreferencesKeys.IS_DARK_THEME]
    }

    val languageCode: Flow<String> = context.settingsDataStore.data.map { preferences ->
        preferences[PreferencesKeys.LANGUAGE_CODE] ?: "en" // default to English
    }

    suspend fun setDarkTheme(isDark: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_DARK_THEME] = isDark
        }
    }

    suspend fun setLanguage(languageCode: String) {
        context.settingsDataStore.edit { preferences ->
            preferences[PreferencesKeys.LANGUAGE_CODE] = languageCode
        }
    }
}
