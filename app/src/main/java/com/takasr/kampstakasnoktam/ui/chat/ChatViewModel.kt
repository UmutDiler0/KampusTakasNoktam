package com.takasr.kampstakasnoktam.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.takasr.kampstakasnoktam.data.ChatRepository
import com.takasr.kampstakasnoktam.data.model.Conversation
import com.takasr.kampstakasnoktam.data.model.toConversation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ChatUiState {
    object Loading : ChatUiState()
    data class Success(val conversations: List<Conversation>) : ChatUiState()
    data class Error(val message: String) : ChatUiState()
}

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ChatUiState>(ChatUiState.Loading)
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        refreshConversations()
    }

    fun refreshConversations() {
        _uiState.value = ChatUiState.Loading
        viewModelScope.launch {
            chatRepository.getConversations().onSuccess { conversations ->
                _uiState.value = ChatUiState.Success(conversations.map { it.toConversation() })
            }.onFailure { error ->
                _uiState.value = ChatUiState.Error(error.message ?: "Mesajlar yüklenemedi")
            }
        }
    }
}
