package com.takasr.kampstakasnoktam.ui.itemdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.takasr.kampstakasnoktam.base.UiState
import com.takasr.kampstakasnoktam.data.AdRepository
import com.takasr.kampstakasnoktam.data.BasketRepository
import com.takasr.kampstakasnoktam.data.ChatRepository
import com.takasr.kampstakasnoktam.data.model.Advertisement
import com.takasr.kampstakasnoktam.data.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemDetailViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val adRepository: AdRepository,
    private val basketRepository: BasketRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Advertisement>>(UiState.Loading)
    val uiState: StateFlow<UiState<Advertisement>> = _uiState.asStateFlow()

    private val _navigateToChat = MutableSharedFlow<Int>()
    val navigateToChat: SharedFlow<Int> = _navigateToChat.asSharedFlow()

    fun initAd(adId: Int, localItem: Advertisement?) {
        if (localItem != null) {
            _uiState.value = UiState.Success(localItem)
        } else if (_uiState.value !is UiState.Success) {
            loadAd(adId)
        }
    }

    private fun loadAd(adId: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            adRepository.getAd(adId)
                .onSuccess { ad -> _uiState.value = UiState.Success(ad) }
                .onFailure { e -> _uiState.value = UiState.Error(e.message ?: "İlan yüklenemedi") }
        }
    }

    fun onAddToBasket(item: Advertisement) {
        basketRepository.addToBasket(item)
    }

    fun onSendMessageClick(targetUserId: String) {
        viewModelScope.launch {
            chatRepository.startConversation(targetUserId).onSuccess { conversation ->
                _navigateToChat.emit(conversation.id)
            }
        }
    }
}
