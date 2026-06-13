package com.takasr.kampstakasnoktam.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
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
import com.takasr.kampstakasnoktam.ui.additem.AddItemScreen
import com.takasr.kampstakasnoktam.ui.basket.BasketScreen
import com.takasr.kampstakasnoktam.ui.basket.BasketViewModel
import com.takasr.kampstakasnoktam.ui.home.BottomNavTab
import com.takasr.kampstakasnoktam.ui.chat.ChatDetailScreen
import com.takasr.kampstakasnoktam.ui.chat.ChatScreen
import com.takasr.kampstakasnoktam.ui.home.FavoritesScreen
import com.takasr.kampstakasnoktam.ui.home.HomeScreen
import com.takasr.kampstakasnoktam.ui.home.HomeViewModel
import com.takasr.kampstakasnoktam.ui.itemdetail.ItemDetailScreen
import com.takasr.kampstakasnoktam.ui.itemdetail.ItemDetailViewModel
import com.takasr.kampstakasnoktam.ui.home.HomeUiData
import com.takasr.kampstakasnoktam.ui.chat.ChatViewModel
import com.takasr.kampstakasnoktam.ui.editad.EditAdScreen
import com.takasr.kampstakasnoktam.ui.myads.MyAdsScreen
import com.takasr.kampstakasnoktam.ui.myads.MyAdsViewModel
import com.takasr.kampstakasnoktam.ui.profile.ProfileScreen
import com.takasr.kampstakasnoktam.ui.settings.SettingsViewModel
import com.takasr.kampstakasnoktam.ui.seller.SellerScreen
import com.takasr.kampstakasnoktam.base.UiState
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
                    homeViewModel.refreshAds()
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
                onBackClick = { navController.popBackStack() },
                onSuccess = {
                    homeViewModel.refreshAds()
                    navController.navigate(AppDestination.Home.route) {
                        popUpTo(AppDestination.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = AppDestination.MyAds.route) { entry ->
            val myAdsViewModel: MyAdsViewModel = hiltViewModel()
            LaunchedEffect(entry) {
                entry.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    myAdsViewModel.loadMyAds()
                }
            }
            MyAdsScreen(
                viewModel = myAdsViewModel,
                onTabSelected = navigateToMainTab,
                onChatClick = { navController.navigate(AppDestination.Chat.route) },
                onBasketClick = { navController.navigate(AppDestination.Basket.route) },
                onAddClick = { navController.navigate(AppDestination.AddItem.route) },
                onAdClick = { adId -> navController.navigate(AppDestination.ItemDetail.createRoute(adId, isMyAd = true)) }
            )
        }

        composable(route = AppDestination.Profile.route) {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            ProfileScreen(
                onTabSelected = navigateToMainTab,
                onChatClick = { navController.navigate(AppDestination.Chat.route) },
                onBasketClick = { navController.navigate(AppDestination.Basket.route) },
                onLogoutClick = {
                    navController.navigate(AppDestination.Auth.route) {
                        popUpTo(AppDestination.Home.route) { inclusive = true }
                    }
                },
                settingsViewModel = settingsViewModel
            )
        }

        composable(route = AppDestination.Chat.route) { entry ->
            val chatViewModel: ChatViewModel = hiltViewModel()
            LaunchedEffect(entry) {
                entry.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    chatViewModel.refreshConversations()
                }
            }
            ChatScreen(
                viewModel = chatViewModel,
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
                navArgument("itemId") { type = NavType.IntType },
                navArgument("isMyAd") { type = NavType.BoolType; defaultValue = false }
            )
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: return@composable
            val isMyAd = backStackEntry.arguments?.getBoolean("isMyAd") ?: false
            val itemDetailViewModel: ItemDetailViewModel = hiltViewModel()
            val homeState by homeViewModel.uiState.collectAsState()
            
            // Unconditionally get MyAdsViewModel to avoid conditional composable invocation
            val myAdsViewModel: MyAdsViewModel = hiltViewModel()
            val myAdsState by myAdsViewModel.uiState.collectAsState()
            
            // Look for the item in Home feed
            var selectedItem = (homeState as? UiState.Success<HomeUiData>)?.data?.ads?.find { it.id == itemId }
                ?: (homeState as? UiState.Success<HomeUiData>)?.data?.filteredFavoriteAds?.find { it.id == itemId }

            // If not found and it's my ad, look in MyAds
            if (selectedItem == null && isMyAd) {
                selectedItem = (myAdsState as? UiState.Success)?.data?.ads?.find { it.id == itemId }

                LaunchedEffect(Unit) {
                    if (myAdsState !is UiState.Success) {
                        myAdsViewModel.loadMyAds()
                    }
                }
            }

            LaunchedEffect(Unit) {
                itemDetailViewModel.navigateToChat.collect { conversationId ->
                    navController.navigate(AppDestination.ChatDetail.createRoute(conversationId))
                }
            }

            ItemDetailScreen(
                item = selectedItem,
                isMyAd = isMyAd,
                onBackClick = { navController.popBackStack() },
                onAddToBasket = homeViewModel::onAddToBasket,
                onEditClick = { navController.navigate(AppDestination.EditAd.createRoute(itemId)) },
                onSellerClick = { sellerId ->
                    navController.navigate(AppDestination.SellerProfile.createRoute(sellerId))
                },
                onSendMessageClick = { targetUserId ->
                    itemDetailViewModel.onSendMessageClick(targetUserId)
                }
            )
        }

        composable(
            route = AppDestination.EditAd.route,
            arguments = listOf(
                navArgument("adId") { type = NavType.IntType }
            )
        ) {
            EditAdScreen(
                onBackClick = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() }
            )
        }

        composable(
            route = AppDestination.SellerProfile.route,
            arguments = listOf(
                navArgument("sellerId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val sellerId = backStackEntry.arguments?.getString("sellerId") ?: return@composable
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
