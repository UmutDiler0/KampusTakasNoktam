package com.takasr.kampstakasnoktam.data

import com.takasr.kampstakasnoktam.data.network.AdCreateRequest
import com.takasr.kampstakasnoktam.data.network.ApiService
import com.takasr.kampstakasnoktam.ui.HomeAdItem
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
    ): Result<List<HomeAdItem>> {
        return try {
            val response = apiService.getAds(
                category = category,
                condition = condition,
                minPrice = minPrice,
                maxPrice = maxPrice,
                isSwap = isSwap
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createAd(request: AdCreateRequest): Result<HomeAdItem> {
        return try {
            val response = apiService.createAd(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateAd(adId: Int, updates: Map<String, Any>): Result<HomeAdItem> {
        return try {
            val response = apiService.updateAd(adId, updates)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadImages(adId: Int, files: List<MultipartBody.Part>): Result<HomeAdItem> {
        return try {
            val response = apiService.uploadAdImages(adId, files)
            Result.success(response)
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
