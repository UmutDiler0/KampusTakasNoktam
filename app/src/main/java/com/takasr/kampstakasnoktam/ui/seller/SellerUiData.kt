package com.takasr.kampstakasnoktam.ui.seller

import com.takasr.kampstakasnoktam.base.UiData
import com.takasr.kampstakasnoktam.data.model.User

data class SellerReview(
    val id: Int,
    val reviewerName: String,
    val reviewerInitials: String,
    val reviewerImageUrl: String? = null,
    val rating: Float,
    val comment: String,
    val date: String
)

data class SellerUiData(
    val user: User,
    val reviews: List<SellerReview> = emptyList()
) : UiData {
    companion object {
        fun empty() = SellerUiData(
            user = User(id = "", fullName = "", university = null, memberSince = "")
        )
    }
}
