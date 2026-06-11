package com.takasr.kampstakasnoktam.data.network

import com.google.gson.annotations.SerializedName

data class SellerProfileResponse(
    val id: String,
    @SerializedName("full_name") val fullName: String,
    val university: String,
    @SerializedName("profile_image_url") val profileImageUrl: String? = null,
    val phone: String? = null,
    val rating: Float,
    @SerializedName("total_sales") val totalSales: Int,
    @SerializedName("total_reviews") val totalReviews: Int,
    @SerializedName("member_since") val memberSince: String,
    @SerializedName("is_email_verified") val isEmailVerified: Boolean,
    val ads: List<AdvertisementResponse>? = null
)
