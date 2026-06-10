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
