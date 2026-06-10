package com.takasr.kampstakasnoktam.data.network

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("full_name") val fullName: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class TokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String
)

data class UserResponse(
    val id: String,
    @SerializedName("full_name") val fullName: String,
    val email: String,
    val university: String?,
    @SerializedName("profile_image_url") val profileImageUrl: String?,
    val rating: Float,
    @SerializedName("total_sales") val totalSales: Int,
    @SerializedName("member_since") val memberSince: String
)
