package com.takasr.kampstakasnoktam.ui.home

import com.takasr.kampstakasnoktam.base.UiData
import com.takasr.kampstakasnoktam.data.model.Advertisement


data class HomeUiData(
    val searchQuery: String = "",
    val ads: List<Advertisement> = emptyList(),
    // Active server-side filters
    val filterCategory: String = "",
    val filterCondition: String = "",
    val filterMinPrice: String = "",
    val filterMaxPrice: String = "",
    val filterIsSwap: Boolean? = null
) : UiData {

    val isFilterActive: Boolean
        get() = filterCategory.isNotBlank() || filterCondition.isNotBlank() ||
                filterMinPrice.isNotBlank() || filterMaxPrice.isNotBlank() ||
                filterIsSwap != null

    val filteredAds: List<Advertisement>
        get() {
            if (searchQuery.isBlank()) return ads
            val query = searchQuery.trim()
            return ads.filter { ad ->
                ad.title.contains(query, ignoreCase = true) ||
                    ad.sellerName.contains(query, ignoreCase = true) ||
                    ad.location.contains(query, ignoreCase = true)
            }
        }

    val filteredFavoriteAds: List<Advertisement>
        get() = filteredAds.filter { ad -> ad.isFavorite }

    companion object {
        val Initial = HomeUiData(ads = emptyList())
    }
}
