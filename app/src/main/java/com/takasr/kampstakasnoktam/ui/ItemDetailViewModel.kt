package com.takasr.kampstakasnoktam.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.takasr.kampstakasnoktam.data.ChatRepository
import com.takasr.kampstakasnoktam.data.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemDetailViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val apiService: ApiService
) : ViewModel() {

    private val _navigateToChat = MutableSharedFlow<Int>()
    val navigateToChat: SharedFlow<Int> = _navigateToChat.asSharedFlow()

    fun onSendMessageClick(targetUserId: String) {
        viewModelScope.launch {
            chatRepository.startConversation(targetUserId).onSuccess { conversation ->
                _navigateToChat.emit(conversation.id)
            }
        }
    }
}
