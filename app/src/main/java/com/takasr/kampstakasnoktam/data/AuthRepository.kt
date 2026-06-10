package com.takasr.kampstakasnoktam.data

import com.takasr.kampstakasnoktam.data.network.ApiService
import com.takasr.kampstakasnoktam.data.network.RegisterRequest
import com.takasr.kampstakasnoktam.data.network.TokenResponse
import com.takasr.kampstakasnoktam.data.network.UserResponse
import com.takasr.kampstakasnoktam.datastore.TokenManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {
    suspend fun login(email: String, pass: String): Result<TokenResponse> {
        return try {
            val response = apiService.login(email, pass)
            tokenManager.saveToken(response.accessToken)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(fullName: String, email: String, pass: String): Result<UserResponse> {
        return try {
            val request = RegisterRequest(fullName, email, pass)
            val response = apiService.register(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        tokenManager.clearToken()
    }
}
