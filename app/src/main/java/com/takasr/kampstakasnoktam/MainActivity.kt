package com.takasr.kampstakasnoktam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.takasr.kampstakasnoktam.navigation.AppNavHost
import com.takasr.kampstakasnoktam.ui.theme.KampüsTakasNoktamTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KampüsTakasNoktamTheme {
                AppNavHost()
            }
        }
    }
}