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
    ): Result<List<Advertisement>> {
        return try {
            val response = apiService.getAds(
                category = category,
                condition = condition,
                minPrice = minPrice,
                maxPrice = maxPrice,
                isSwap = isSwap
            )
            Result.success(response.map { it.toAdvertisement() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createAd(request: AdCreateRequest): Result<Advertisement> {
        return try {
            Result.success(apiService.createAd(request).toAdvertisement())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateAd(adId: Int, updates: Map<String, Any>): Result<Advertisement> {
        return try {
            Result.success(apiService.updateAd(adId, updates).toAdvertisement())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadImages(adId: Int, files: List<MultipartBody.Part>): Result<Advertisement> {
        return try {
            Result.success(apiService.uploadAdImages(adId, files).toAdvertisement())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteAd(adId: Int): Result<Unit> {
        return try {
            apiService.deleteAd(adId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
