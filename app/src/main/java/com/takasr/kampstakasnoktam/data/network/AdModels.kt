package com.takasr.kampstakasnoktam.data.network

import com.google.gson.annotations.SerializedName

data class AdCreateRequest(
    val title: String,
    val description: String,
    val price: Double,
    @SerializedName("is_swap") val isSwap: Boolean,
    val condition: String,
    val category: String,
    val location: String
)

data class AdvertisementResponse(
    val id: Int,
    val title: String,
    val price: String,
    val description: String,
    @SerializedName("seller_id") val sellerId: String,
    @SerializedName("seller_full_name") val sellerFullName: String? = null,
    @SerializedName("is_swap") val isSwap: Boolean = false,
    val condition: String,
    val category: String,
    val location: String,
    @SerializedName("image_urls") val imageUrls: List<String>? = null,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("is_active") val isActive: Boolean = true
)
