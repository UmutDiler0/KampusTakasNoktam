package com.takasr.kampstakasnoktam.data.network

import com.google.gson.Gson
import com.takasr.kampstakasnoktam.datastore.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketProvider @Inject constructor(
    private val client: OkHttpClient,
    private val tokenManager: TokenManager,
    private val gson: Gson
) {
    private var webSocket: WebSocket? = null
    private val _messages = MutableSharedFlow<ChatMessage>(extraBufferCapacity = 10)
    val messages: SharedFlow<ChatMessage> = _messages

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun connect() {
        if (webSocket != null) return

        scope.launch {
            val token = tokenManager.accessToken.firstOrNull() ?: return@launch
            val request = Request.Builder()
                .url("wss://kampustakasnoktam.keserbaros.com/chat/ws?token=$token")
                .build()

            webSocket = client.newWebSocket(request, object : WebSocketListener() {
                override fun onMessage(webSocket: WebSocket, text: String) {
                    try {
                        val message = gson.fromJson(text, ChatMessage::class.java)
                        scope.launch { _messages.emit(message) }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    webSocket.close(1000, null)
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    this@WebSocketProvider.webSocket = null
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    this@WebSocketProvider.webSocket = null
                }
            })
        }
    }

    fun sendMessage(targetUserId: String, text: String, conversationId: Int) {
        val request = SendMessageRequest(targetUserId, text, conversationId)
        val json = gson.toJson(request)
        webSocket?.send(json)
    }

    fun disconnect() {
        webSocket?.close(1000, "User disconnected")
        webSocket = null
    }
}
