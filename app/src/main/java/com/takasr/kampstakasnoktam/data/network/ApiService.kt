package com.takasr.kampstakasnoktam.data.network

import com.takasr.kampstakasnoktam.data.network.AdCreateRequest
import com.takasr.kampstakasnoktam.ui.HomeAdItem
import kotlin.jvm.JvmSuppressWildcards
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService {

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): UserResponse

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("username") email: String,
        @Field("password") pass: String
    ): TokenResponse

    @GET("ads")
    suspend fun getAds(
        @Query("category") category: String? = null,
        @Query("condition") condition: String? = null,
        @Query("min_price") minPrice: Double? = null,
        @Query("max_price") maxPrice: Double? = null,
        @Query("is_swap") isSwap: Boolean? = null
    ): List<HomeAdItem>

    @POST("ads")
    suspend fun createAd(@Body request: AdCreateRequest): HomeAdItem

    @Multipart
    @POST("ads/{ad_id}/images")
    suspend fun uploadAdImages(
        @Path("ad_id") adId: Int,
        @Part files: List<MultipartBody.Part>
    ): HomeAdItem

    @PUT("ads/{ad_id}")
    suspend fun updateAd(
        @Path("ad_id") adId: Int,
        @Body updates: Map<String, @JvmSuppressWildcards Any>
    ): HomeAdItem

    @DELETE("ads/{ad_id}")
    suspend fun deleteAd(@Path("ad_id") adId: Int)

    @GET("users/me")
    suspend fun getMe(): UserResponse

    // --- Chat Endpoints ---
    @GET("chat/conversations")
    suspend fun getConversations(): List<ChatConversation>

    @GET("chat/conversations/{id}/messages")
    suspend fun getMessages(@Path("id") conversationId: Int): List<ChatMessage>

    @POST("chat/conversations")
    suspend fun startConversation(@Query("target_user_id") targetUserId: String): ChatConversation

    @POST("chat/messages")
    suspend fun sendMessage(@Body request: SendMessageRequest): ChatMessage
}
