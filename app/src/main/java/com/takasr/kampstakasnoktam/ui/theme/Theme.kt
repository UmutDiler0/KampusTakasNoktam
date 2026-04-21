package com.takasr.kampstakasnoktam.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Blue80,
    secondary = Mint80,
    tertiary = Coral70,
    background = Navy20,
    surface = Navy30,
    onPrimary = Navy20,
    onSecondary = Navy20,
    onTertiary = Navy20,
    onBackground = SoftWhite,
    onSurface = SoftWhite
)

private val LightColorScheme = lightColorScheme(
    primary = Blue40,
    secondary = Mint40,
    tertiary = Coral70,
    background = Sky95,
    surface = SoftWhite,
    onPrimary = SoftWhite,
    onSecondary = SoftWhite,
    onTertiary = Ink10,
    onBackground = Ink10,
    onSurface = Ink10
)

@Composable
fun KampüsTakasNoktamTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}