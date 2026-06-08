package com.takasr.kampstakasnoktam.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.takasr.kampstakasnoktam.auth.AuthRoute
import com.takasr.kampstakasnoktam.onboarding.OnboardingRoute
import com.takasr.kampstakasnoktam.splash.SplashNavigationTarget
import com.takasr.kampstakasnoktam.splash.SplashScreen
import com.takasr.kampstakasnoktam.splash.SplashViewModel
import com.takasr.kampstakasnoktam.ui.AddItemScreen
import com.takasr.kampstakasnoktam.ui.BasketScreen
import com.takasr.kampstakasnoktam.ui.BasketViewModel
import com.takasr.kampstakasnoktam.ui.BottomNavTab
import com.takasr.kampstakasnoktam.ui.ChatDetailScreen
import com.takasr.kampstakasnoktam.ui.ChatScreen
import com.takasr.kampstakasnoktam.ui.FavoritesScreen
import com.takasr.kampstakasnoktam.ui.HomeScreen
import com.takasr.kampstakasnoktam.ui.HomeViewModel
import com.takasr.kampstakasnoktam.ui.ItemDetailScreen
import com.takasr.kampstakasnoktam.ui.MyAdsScreen
import com.takasr.kampstakasnoktam.ui.ProfileScreen
import com.takasr.kampstakasnoktam.ui.SettingsViewModel
import com.takasr.kampstakasnoktam.ui.seller.SellerScreen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    val homeViewModel: HomeViewModel = hiltViewModel()

    val navigateToMainTab: (BottomNavTab) -> Unit = { tab ->
        val route = when (tab) {
            BottomNavTab.Home -> AppDestination.Home.route
            BottomNavTab.Favorites -> AppDestination.Favorites.route
            BottomNavTab.AddItem -> AppDestination.AddItem.route
            BottomNavTab.MyAds -> AppDestination.MyAds.route
            BottomNavTab.Profile -> AppDestination.Profile.route
        }
        navController.navigate(route) {
            popUpTo(AppDestination.Home.route) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

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
                onTabSelected = navigateToMainTab,
                onChatClick = { navController.navigate(AppDestination.Chat.route) },
                onBasketClick = { navController.navigate(AppDestination.Basket.route) },
                onItemClick = { itemId ->
                    navController.navigate(AppDestination.ItemDetail.createRoute(itemId))
                },
                viewModel = homeViewModel
            )
        }

        composable(route = AppDestination.Favorites.route) {
            FavoritesScreen(
                onTabSelected = navigateToMainTab,
                onChatClick = { navController.navigate(AppDestination.Chat.route) },
                onBasketClick = { navController.navigate(AppDestination.Basket.route) },
                onItemClick = { itemId ->
                    navController.navigate(AppDestination.ItemDetail.createRoute(itemId))
                },
                viewModel = homeViewModel
            )
        }

        composable(route = AppDestination.AddItem.route) {
            AddItemScreen(
                onTabSelected = navigateToMainTab,
                onChatClick = { navController.navigate(AppDestination.Chat.route) },
                onBasketClick = { navController.navigate(AppDestination.Basket.route) }
            )
        }

        composable(route = AppDestination.MyAds.route) {
            MyAdsScreen(
                onTabSelected = navigateToMainTab,
                onChatClick = { navController.navigate(AppDestination.Chat.route) },
                onBasketClick = { navController.navigate(AppDestination.Basket.route) }
            )
        }

        composable(route = AppDestination.Profile.route) {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            ProfileScreen(
                onTabSelected = navigateToMainTab,
                onChatClick = { navController.navigate(AppDestination.Chat.route) },
                onBasketClick = { navController.navigate(AppDestination.Basket.route) },
                settingsViewModel = settingsViewModel
            )
        }

        composable(route = AppDestination.Chat.route) {
            ChatScreen(
                onBackClick = { navController.popBackStack() },
                onConversationClick = { conversationId ->
                    navController.navigate(AppDestination.ChatDetail.createRoute(conversationId))
                }
            )
        }

        composable(
            route = AppDestination.ChatDetail.route,
            arguments = listOf(
                navArgument("conversationId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val conversationId = backStackEntry.arguments?.getInt("conversationId") ?: return@composable
            ChatDetailScreen(
                conversationId = conversationId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = AppDestination.Basket.route) {
            val basketViewModel: BasketViewModel = hiltViewModel()
            BasketScreen(
                onBackClick = { navController.popBackStack() },
                viewModel = basketViewModel
            )
        }

        composable(
            route = AppDestination.ItemDetail.route,
            arguments = listOf(
                navArgument("itemId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: return@composable
            ItemDetailScreen(
                itemId = itemId,
                onBackClick = { navController.popBackStack() },
                onAddToBasket = homeViewModel::onAddToBasket,
                onSellerClick = { sellerId ->
                    navController.navigate(AppDestination.SellerProfile.createRoute(sellerId))
                }
            )
        }

        composable(
            route = AppDestination.SellerProfile.route,
            arguments = listOf(
                navArgument("sellerId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val sellerId = backStackEntry.arguments?.getInt("sellerId") ?: return@composable
            SellerScreen(
                sellerId = sellerId,
                onBackClick = { navController.popBackStack() },
                onAdClick = { adId ->
                    navController.navigate(AppDestination.ItemDetail.createRoute(adId))
                }
            )
        }
    }
}
