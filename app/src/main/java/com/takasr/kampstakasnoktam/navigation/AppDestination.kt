package com.takasr.kampstakasnoktam.navigation

sealed class AppDestination(val route: String) {
    data object Splash : AppDestination("splash")
    data object Onboarding : AppDestination("onboarding")
    data object Auth : AppDestination("auth")
    data object Home : AppDestination("home")
    data object Chat : AppDestination("chat")
    data object Basket : AppDestination("basket")
}
