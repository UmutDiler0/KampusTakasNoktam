package com.takasr.kampstakasnoktam.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.appDataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

@Singleton
class OnboardingPreferencesManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    private object PreferencesKeys {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("isOnboardingCompleted")
    }

    val isOnboardingCompleted: Flow<Boolean> = context.appDataStore.data.map { preferences ->
        preferences[PreferencesKeys.ONBOARDING_COMPLETED] ?: false
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.appDataStore.edit { preferences ->
            preferences[PreferencesKeys.ONBOARDING_COMPLETED] = completed
        }
    }
}
