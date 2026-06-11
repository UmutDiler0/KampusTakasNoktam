package com.takasr.kampstakasnoktam.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.takasr.kampstakasnoktam.datastore.SettingsPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsPreferencesManager: SettingsPreferencesManager
) : ViewModel() {

    val isDarkTheme = settingsPreferencesManager.isDarkTheme.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val languageCode = settingsPreferencesManager.languageCode.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "en"
    )

    fun setDarkTheme(isDark: Boolean) {
        viewModelScope.launch {
            settingsPreferencesManager.setDarkTheme(isDark)
        }
    }

    fun setLanguage(languageCode: String) {
        viewModelScope.launch {
            settingsPreferencesManager.setLanguage(languageCode)
        }
    }
}
