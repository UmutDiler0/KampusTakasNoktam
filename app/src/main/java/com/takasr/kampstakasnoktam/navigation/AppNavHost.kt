package com.takasr.kampstakasnoktam.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.takasr.kampstakasnoktam.auth.AuthRoute
import com.takasr.kampstakasnoktam.onboarding.OnboardingRoute
import com.takasr.kampstakasnoktam.splash.SplashNavigationTarget
import com.takasr.kampstakasnoktam.splash.SplashScreen
import com.takasr.kampstakasnoktam.splash.SplashViewModel
import com.takasr.kampstakasnoktam.ui.BasketScreen
import com.takasr.kampstakasnoktam.ui.ChatScreen
import com.takasr.kampstakasnoktam.ui.HomeScreen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AppDestination.Splash.route
    ) {
        composable(route = AppDestination.Splash.route) {
            val viewModel: SplashViewModel = hiltViewModel()
            LaunchedEffect(Unit) {
                viewModel.navigationEvent.collectLatest { target ->
                    when (target) {
                        SplashNavigationTarget.Onboarding -> {
                            navController.navigate(AppDestination.Onboarding.route) {
                                popUpTo(AppDestination.Splash.route) { inclusive = true }
                            }
                        }

                        SplashNavigationTarget.Auth -> {
                            navController.navigate(AppDestination.Auth.route) {
                                popUpTo(AppDestination.Splash.route) { inclusive = true }
                            }
                        }
                    }
                }
            }
            SplashScreen()
        }

        composable(route = AppDestination.Onboarding.route) {
            OnboardingRoute(
                onNavigateAuth = {
                    navController.navigate(AppDestination.Auth.route) {
                        popUpTo(AppDestination.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = AppDestination.Auth.route) {
            AuthRoute(
                onLoginSuccess = {
                    navController.navigate(AppDestination.Home.route) {
                        popUpTo(AppDestination.Auth.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = AppDestination.Home.route) {
            HomeScreen(
                onChatClick = { navController.navigate(AppDestination.Chat.route) },
                onBasketClick = { navController.navigate(AppDestination.Basket.route) }
            )
        }

        composable(route = AppDestination.Chat.route) {
            ChatScreen()
        }

        composable(route = AppDestination.Basket.route) {
            BasketScreen()
        }
    }
}
