package com.takasr.kampstakasnoktam.ui

import com.takasr.kampstakasnoktam.base.UiData

data class HomeAdItem(
    val id: Int,
    val title: String,
    val price: Double,
    val sellerName: String,
    val location: String,
    val imageUrl: String,
    val isFavorite: Boolean = false,
    val category: String = ""
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

    val filteredFavoriteAds: List<HomeAdItem>
        get() = filteredAds.filter { ad -> ad.isFavorite }

    companion object {
        val Initial = HomeUiData(
            ads = listOf(
                HomeAdItem(
                    id = 1,
                    title = "MacBook Air M1 256GB",
                    price = 24500.0,
                    sellerName = "Ayse K.",
                    location = "ITU Ayazaga",
                    imageUrl = "https://picsum.photos/seed/ad-1/600/400",
                    isFavorite = true,
                    category = "Elektronik"
                ),
                HomeAdItem(
                    id = 2,
                    title = "Calculus 2 Ders Kitabi",
                    price = 350.0,
                    sellerName = "Mehmet T.",
                    location = "YTU Davutpasa",
                    imageUrl = "https://picsum.photos/seed/ad-2/600/400",
                    category = "Kitap"
                ),
                HomeAdItem(
                    id = 3,
                    title = "Ikea Calisma Masasi",
                    price = 2100.0,
                    sellerName = "Elif D.",
                    location = "Bogazici Universitesi",
                    imageUrl = "https://picsum.photos/seed/ad-3/600/400",
                    isFavorite = true,
                    category = "Mobilya"
                ),
                HomeAdItem(
                    id = 4,
                    title = "Sony WH-1000XM4",
                    price = 6700.0,
                    sellerName = "Can A.",
                    location = "Marmara Universitesi",
                    imageUrl = "https://picsum.photos/seed/ad-4/600/400",
                    category = "Elektronik"
                ),
                HomeAdItem(
                    id = 5,
                    title = "Bisiklet - Sehir Tipi",
                    price = 4900.0,
                    sellerName = "Zeynep U.",
                    location = "Istanbul Universitesi",
                    imageUrl = "https://picsum.photos/seed/ad-5/600/400",
                    category = "Spor"
                ),
                HomeAdItem(
                    id = 6,
                    title = "Gaming Mouse",
                    price = 850.0,
                    sellerName = "Mert A.",
                    location = "ITU Ayazaga",
                    imageUrl = "https://picsum.photos/seed/ad-6/600/400",
                    category = "Elektronik"
                ),
                HomeAdItem(
                    id = 7,
                    title = "Python Programlama Kitabi",
                    price = 250.0,
                    sellerName = "Selin Y.",
                    location = "YTU Davutpasa",
                    imageUrl = "https://picsum.photos/seed/ad-7/600/400",
                    category = "Kitap"
                )
            )
        )
    }
}
