package com.takasr.kampstakasnoktam.data.model

import com.takasr.kampstakasnoktam.data.network.AdvertisementResponse

data class Advertisement(
    val id: Int,
    val sellerId: String,
    val sellerName: String = "",
    val title: String,
    val description: String,
    val price: Double,
    val isSwap: Boolean,
    val condition: String,
    val category: String,
    val location: String,
    val imageUrls: List<String>,
    val createdAt: String,
    val isActive: Boolean,
    val isFavorite: Boolean = false
) {
    val imageUrl: String
        get() = imageUrls.firstOrNull()?.let { url ->
            if (url.startsWith("http")) url else "https://kampustakasnoktam.keserbaros.com$url"
        } ?: "https://picsum.photos/seed/placeholder/600/400"

    val formattedPrice: String
        get() = if (price % 1.0 == 0.0) price.toLong().toString() else "%.2f".format(price)
}

fun AdvertisementResponse.toAdvertisement(): Advertisement = Advertisement(
    id = id,
    sellerId = sellerId,
    sellerName = sellerFullName.orEmpty(),
    title = title,
    description = description,
    price = price.toDoubleOrNull() ?: 0.0,
    isSwap = isSwap,
    condition = condition,
    category = category,
    location = location,
    imageUrls = imageUrls.orEmpty(),
    createdAt = createdAt,
    isActive = isActive
)
