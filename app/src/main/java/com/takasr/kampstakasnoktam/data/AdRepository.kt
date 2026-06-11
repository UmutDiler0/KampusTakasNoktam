package com.takasr.kampstakasnoktam.data

import com.takasr.kampstakasnoktam.data.model.Advertisement
import com.takasr.kampstakasnoktam.data.model.toAdvertisement
import com.takasr.kampstakasnoktam.data.network.AdCreateRequest
import com.takasr.kampstakasnoktam.data.network.ApiService
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getAds(
        category: String? = null,
        condition: String? = null,
        minPrice: Double? = null,
        maxPrice: Double? = null,
        isSwap: Boolean? = null
    ): Result<List<Advertisement>> = runCatching {
        apiService.getAds(category, condition, minPrice, maxPrice, isSwap)
            .map { it.toAdvertisement() }
    }

    suspend fun discoverAds(
        category: String? = null,
        condition: String? = null,
        minPrice: Double? = null,
        maxPrice: Double? = null,
        isSwap: Boolean? = null
    ): Result<List<Advertisement>> = runCatching {
        apiService.discoverAds(category, condition, minPrice, maxPrice, isSwap)
            .map { it.toAdvertisement() }
    }

    suspend fun getMyAds(): Result<List<Advertisement>> = runCatching {
        apiService.getMyAds().map { it.toAdvertisement() }
    }

    suspend fun getAd(adId: Int): Result<Advertisement> = runCatching {
        apiService.getAd(adId).toAdvertisement()
    }

    suspend fun createAd(request: AdCreateRequest): Result<Advertisement> = runCatching {
        apiService.createAd(request).toAdvertisement()
    }

    suspend fun updateAd(adId: Int, updates: Map<String, Any>): Result<Advertisement> = runCatching {
        apiService.updateAd(adId, updates).toAdvertisement()
    }

    suspend fun uploadImages(adId: Int, files: List<MultipartBody.Part>): Result<Advertisement> = runCatching {
        apiService.uploadAdImages(adId, files).toAdvertisement()
    }

    suspend fun deleteAd(adId: Int): Result<Unit> = runCatching {
        apiService.deleteAd(adId)
    }
}
