package com.takasr.kampstakasnoktam.data

import com.takasr.kampstakasnoktam.data.network.ApiService
import com.takasr.kampstakasnoktam.data.network.ChatConversation
import com.takasr.kampstakasnoktam.data.network.ChatMessage
import com.takasr.kampstakasnoktam.data.network.WebSocketProvider
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val apiService: ApiService,
    private val webSocketProvider: WebSocketProvider
) {
    val incomingMessages: SharedFlow<ChatMessage> = webSocketProvider.messages

    fun connectToChat() = webSocketProvider.connect()
    fun disconnectFromChat() = webSocketProvider.disconnect()

    fun sendLiveMessage(targetUserId: String, text: String, conversationId: Int) {
        webSocketProvider.sendMessage(targetUserId, text, conversationId)
    }

    suspend fun getConversations(): Result<List<ChatConversation>> {
        return try {
            val response = apiService.getConversations()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMessages(conversationId: Int): Result<List<ChatMessage>> {
        return try {
            val response = apiService.getMessages(conversationId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun startConversation(targetUserId: String): Result<ChatConversation> {
        return try {
            val response = apiService.startConversation(targetUserId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCurrentUserId(): Result<String> {
        return try {
            val response = apiService.getMe()
            Result.success(response.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
