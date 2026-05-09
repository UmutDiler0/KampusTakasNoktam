package com.takasr.kampstakasnoktam.ui.seller

import com.takasr.kampstakasnoktam.base.UiData

// ─── Domain Models ────────────────────────────────────────────────────────────

data class SellerProfile(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String,
    val isEmailVerified: Boolean,
    val profileImageUrl: String? = null,
    val university: String,
    val memberSince: String,
    val rating: Float,
    val totalSales: Int,
    val totalReviews: Int
) {
    val fullName: String get() = "$firstName $lastName"
    val initials: String get() = "${firstName.firstOrNull() ?: ""}${lastName.firstOrNull() ?: ""}"
}

data class SellerAd(
    val id: Int,
    val title: String,
    val price: String,
    val imageUrl: String,
    val location: String,
    val isActive: Boolean = true
)

data class SellerReview(
    val id: Int,
    val reviewerName: String,
    val reviewerInitials: String,
    val reviewerImageUrl: String? = null,
    val rating: Float,
    val comment: String,
    val date: String
)

// ─── UiData ──────────────────────────────────────────────────────────────────

data class SellerUiData(
    val seller: SellerProfile,
    val ads: List<SellerAd> = emptyList(),
    val reviews: List<SellerReview> = emptyList()
) : UiData {

    companion object {
        /**
         * Returns mock data for a given sellerId. In a real app this would
         * come from a repository / network call.
         */
        fun mock(sellerId: Int) = SellerUiData(
            seller = SellerProfile(
                id = sellerId,
                firstName = "Ayşe",
                lastName = "Kaya",
                phone = "+90 532 123 45 67",
                email = "ayse.kaya@itu.edu.tr",
                isEmailVerified = true,
                profileImageUrl = null,
                university = "İTÜ Ayazağa",
                memberSince = "Eylül 2023",
                rating = 4.7f,
                totalSales = 23,
                totalReviews = 18
            ),
            ads = listOf(
                SellerAd(
                    id = 1,
                    title = "MacBook Air M1 256GB",
                    price = "24.500 TL",
                    imageUrl = "https://picsum.photos/seed/ad-1/600/400",
                    location = "İTÜ Ayazağa"
                ),
                SellerAd(
                    id = 6,
                    title = "iPad Pro 11\" 2022",
                    price = "18.000 TL",
                    imageUrl = "https://picsum.photos/seed/ad-6/600/400",
                    location = "İTÜ Ayazağa"
                ),
                SellerAd(
                    id = 7,
                    title = "Mekanik Klavye - Gateron Red",
                    price = "1.200 TL",
                    imageUrl = "https://picsum.photos/seed/ad-7/600/400",
                    location = "İTÜ Ayazağa"
                )
            ),
            reviews = listOf(
                SellerReview(
                    id = 1,
                    reviewerName = "Mehmet Türk",
                    reviewerInitials = "MT",
                    rating = 5.0f,
                    comment = "Ürün tanımlandığı gibi, çok hızlı teslim. Harika bir satıcı!",
                    date = "2 Mayıs 2026"
                ),
                SellerReview(
                    id = 2,
                    reviewerName = "Elif Demir",
                    reviewerInitials = "ED",
                    rating = 4.0f,
                    comment = "Güzel iletişim, ürün açıklaması ile uyumluydu. Tekrar alışveriş yaparım.",
                    date = "18 Nisan 2026"
                ),
                SellerReview(
                    id = 3,
                    reviewerName = "Can Arslan",
                    reviewerInitials = "CA",
                    rating = 5.0f,
                    comment = "Çok güvenilir bir satıcı. Ürün beklediğimden de iyi çıktı.",
                    date = "5 Nisan 2026"
                ),
                SellerReview(
                    id = 4,
                    reviewerName = "Zeynep Uğur",
                    reviewerInitials = "ZU",
                    rating = 4.5f,
                    comment = "Hızlı cevap verdi ve buluşma konusunda esnek davrandı.",
                    date = "22 Mart 2026"
                )
            )
        )
    }
}
