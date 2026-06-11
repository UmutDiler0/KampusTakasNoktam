package com.takasr.kampstakasnoktam.navigation

sealed class AppDestination(val route: String) {
    data object Splash : AppDestination("splash")
    data object Onboarding : AppDestination("onboarding")
    data object Auth : AppDestination("auth")
    data object Home : AppDestination("home")
    data object Favorites : AppDestination("favorites")
    data object AddItem : AppDestination("add-item")
    data object MyAds : AppDestination("my-ads")
    data object Profile : AppDestination("profile")
    data object Chat : AppDestination("chat")
    data object ChatDetail : AppDestination("chat-detail/{conversationId}") {
        fun createRoute(conversationId: Int) = "chat-detail/$conversationId"
    }
    data object Basket : AppDestination("basket")
    data object ItemDetail : AppDestination("item-detail/{itemId}") {
        fun createRoute(itemId: Int): String = "item-detail/$itemId"
    }

    data object SellerProfile : AppDestination("seller-profile/{sellerId}") {
        fun createRoute(sellerId: String): String = "seller-profile/$sellerId"
    }
}
