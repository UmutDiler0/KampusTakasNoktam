package com.takasr.kampstakasnoktam.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.takasr.kampstakasnoktam.data.ChatRepository
import com.takasr.kampstakasnoktam.data.network.ChatConversation
import com.takasr.kampstakasnoktam.data.network.ChatMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ChatDetailUiState {
    object Loading : ChatDetailUiState()
    data class Success(
        val conversation: ChatConversation,
        val messages: List<ChatMessage>
    ) : ChatDetailUiState()
    data class Error(val message: String) : ChatDetailUiState()
}

@HiltViewModel
class ChatDetailViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ChatDetailUiState>(ChatDetailUiState.Loading)
    val uiState: StateFlow<ChatDetailUiState> = _uiState.asStateFlow()

    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId: StateFlow<String?> = _currentUserId.asStateFlow()

    init {
        chatRepository.connectToChat()
        observeLiveMessages()
        loadCurrentUserId()
    }

    private fun observeLiveMessages() {
        viewModelScope.launch {
            chatRepository.incomingMessages.collectLatest { newMessage ->
                val currentState = _uiState.value
                if (currentState is ChatDetailUiState.Success && currentState.conversation.id == newMessage.conversationId) {
                    _uiState.value = currentState.copy(
                        messages = currentState.messages + newMessage
                    )
                }
            }
        }
    }

    private fun loadCurrentUserId() {
        viewModelScope.launch {
            chatRepository.getCurrentUserId().onSuccess { id ->
                _currentUserId.value = id
            }
        }
    }

    fun loadMessages(conversationId: Int) {
        _uiState.value = ChatDetailUiState.Loading
        viewModelScope.launch {
            chatRepository.getConversations().onSuccess { conversations ->
                val conversation = conversations.find { it.id == conversationId }
                if (conversation != null) {
                    chatRepository.getMessages(conversationId).onSuccess { messages ->
                        _uiState.value = ChatDetailUiState.Success(conversation, messages)
                    }.onFailure { error ->
                        _uiState.value = ChatDetailUiState.Error(error.message ?: "Mesajlar yüklenemedi")
                    }
                } else {
                    _uiState.value = ChatDetailUiState.Error("Konuşma bulunamadı")
                }
            }.onFailure { error ->
                _uiState.value = ChatDetailUiState.Error(error.message ?: "Konuşma bilgisi alınamadı")
            }
        }
    }

    fun sendLiveMessage(conversation: ChatConversation, text: String) {
        val currentUser = _currentUserId.value ?: return
        val targetUserId = if (conversation.user1Id == currentUser) {
            conversation.user2Id
        } else {
            conversation.user1Id
        }

        chatRepository.sendLiveMessage(targetUserId, text, conversation.id)
    }

    override fun onCleared() {
        super.onCleared()
        chatRepository.disconnectFromChat()
    }
}
