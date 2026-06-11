package com.takasr.kampstakasnoktam.data.model

import com.takasr.kampstakasnoktam.data.network.SellerProfileResponse
import com.takasr.kampstakasnoktam.data.network.UserResponse

data class User(
    val id: String,
    val fullName: String,
    val email: String? = null,
    val university: String?,
    val profileImageUrl: String? = null,
    val phone: String? = null,
    val rating: Float = 0f,
    val totalSales: Int = 0,
    val totalReviews: Int = 0,
    val memberSince: String,
    val isEmailVerified: Boolean = false,
    val ads: List<Advertisement> = emptyList()
) {
    val initials: String
        get() = fullName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.joinToString("")
}

fun UserResponse.toUser(): User = User(
    id = id,
    fullName = fullName,
    email = email,
    university = university,
    profileImageUrl = profileImageUrl,
    phone = phone,
    rating = rating,
    totalSales = totalSales,
    totalReviews = totalReviews,
    memberSince = memberSince,
    isEmailVerified = isEmailVerified
)

fun SellerProfileResponse.toUser(): User = User(
    id = id,
    fullName = fullName,
    email = null,
    university = university,
    profileImageUrl = profileImageUrl,
    phone = phone,
    rating = rating,
    totalSales = totalSales,
    totalReviews = totalReviews,
    memberSince = memberSince,
    isEmailVerified = isEmailVerified,
    ads = ads.orEmpty().map { it.toAdvertisement() }
)
