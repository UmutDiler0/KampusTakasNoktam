package com.takasr.kampstakasnoktam.ui

import com.takasr.kampstakasnoktam.base.UiData

data class HomeAdItem(
    val id: Int,
    val title: String,
    val price: String,
    val sellerName: String,
    val location: String,
    val imageUrl: String,
    val isFavorite: Boolean = false
)

data class HomeUiData(
    val searchQuery: String = "",
    val ads: List<HomeAdItem> = emptyList()
) : UiData {

    val filteredAds: List<HomeAdItem>
        get() {
            if (searchQuery.isBlank()) return ads
            val query = searchQuery.trim()
            return ads.filter { ad ->
                ad.title.contains(query, ignoreCase = true) ||
                    ad.sellerName.contains(query, ignoreCase = true) ||
                    ad.location.contains(query, ignoreCase = true)
            }
        }

    companion object {
        val Initial = HomeUiData(
            ads = listOf(
                HomeAdItem(
                    id = 1,
                    title = "MacBook Air M1 256GB",
                    price = "24.500 TL",
                    sellerName = "Ayse K.",
                    location = "ITU Ayazaga",
                    imageUrl = "https://picsum.photos/seed/ad-1/600/400"
                ),
                HomeAdItem(
                    id = 2,
                    title = "Calculus 2 Ders Kitabi",
                    price = "350 TL",
                    sellerName = "Mehmet T.",
                    location = "YTU Davutpasa",
                    imageUrl = "https://picsum.photos/seed/ad-2/600/400"
                ),
                HomeAdItem(
                    id = 3,
                    title = "Ikea Calisma Masasi",
                    price = "2.100 TL",
                    sellerName = "Elif D.",
                    location = "Bogazici Universitesi",
                    imageUrl = "https://picsum.photos/seed/ad-3/600/400"
                ),
                HomeAdItem(
                    id = 4,
                    title = "Sony WH-1000XM4",
                    price = "6.700 TL",
                    sellerName = "Can A.",
                    location = "Marmara Universitesi",
                    imageUrl = "https://picsum.photos/seed/ad-4/600/400"
                ),
                HomeAdItem(
                    id = 5,
                    title = "Bisiklet - Sehir Tipi",
                    price = "4.900 TL",
                    sellerName = "Zeynep U.",
                    location = "Istanbul Universitesi",
                    imageUrl = "https://picsum.photos/seed/ad-5/600/400"
                )
            )
        )
    }
}
