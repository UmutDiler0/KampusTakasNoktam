package com.takasr.kampstakasnoktam.ui

import com.google.gson.annotations.SerializedName
import com.takasr.kampstakasnoktam.base.UiData

data class HomeAdItem(
    val id: Int,
    @SerializedName("seller_id") val sellerId: String,
    val title: String,
    val description: String,
    val price: Double,
    @SerializedName("is_swap") val isSwap: Boolean,
    val condition: String,
    val category: String,
    val location: String,
    @SerializedName("image_urls") val imageUrls: List<String>,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("is_active") val isActive: Boolean,
    val isFavorite: Boolean = false
) {
    val imageUrl: String
        get() = if (imageUrls.isNotEmpty()) {
            val url = imageUrls.first()
            if (url.startsWith("http")) url else "https://kampustakasnoktam.keserbaros.com$url"
        } else {
            "https://picsum.photos/seed/placeholder/600/400"
        }

    val sellerName: String get() = "Satıcı" // Backend'den ayrıca çekilecek veya AdResponse'a eklenebilir
}

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
            ads = emptyList()
        )
    }
}
