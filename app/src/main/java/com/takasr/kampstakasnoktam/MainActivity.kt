package com.takasr.kampstakasnoktam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.takasr.kampstakasnoktam.datastore.SettingsPreferencesManager
import com.takasr.kampstakasnoktam.navigation.AppNavHost
import com.takasr.kampstakasnoktam.ui.theme.KampüsTakasNoktamTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settingsPreferencesManager: SettingsPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkThemePref by settingsPreferencesManager.isDarkTheme.collectAsState(initial = null)
            val isSystemDark = isSystemInDarkTheme()
            val darkTheme = isDarkThemePref ?: isSystemDark

            val languageCode by settingsPreferencesManager.languageCode.collectAsState(initial = "en")

            val context = LocalContext.current
            val currentConfig = LocalConfiguration.current
            
            val configuration = remember(currentConfig, languageCode) {
                android.content.res.Configuration(currentConfig).apply {
                    setLocale(Locale(languageCode))
                }
            }

            LaunchedEffect(languageCode) {
                val locale = Locale(languageCode)
                Locale.setDefault(locale)
                @Suppress("DEPRECATION")
                context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
            }

            CompositionLocalProvider(
                LocalConfiguration provides configuration
            ) {
                KampüsTakasNoktamTheme(darkTheme = darkTheme) {
                    AppNavHost()
                }
            }
        }
    }
}