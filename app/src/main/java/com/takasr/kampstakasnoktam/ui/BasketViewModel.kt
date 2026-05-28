package com.takasr.kampstakasnoktam.ui

import androidx.lifecycle.viewModelScope
import com.takasr.kampstakasnoktam.base.BaseViewModel
import com.takasr.kampstakasnoktam.base.UiData
import com.takasr.kampstakasnoktam.data.BasketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BasketUiData(
    val basketItems: List<HomeAdItem> = emptyList(),
    val suggestions: List<HomeAdItem> = emptyList(),
    val totalPrice: Double = 0.0
) : UiData

@HiltViewModel
class BasketViewModel @Inject constructor(
    private val basketRepository: BasketRepository
) : BaseViewModel<BasketUiData>(BasketUiData()) {

    init {
        viewModelScope.launch {
            basketRepository.basketItems.collectLatest { items ->
                updateData { it.copy(
                    basketItems = items,
                    totalPrice = items.sumOf { item -> item.price }
                ) }
                updateSuggestions(items)
            }
        }
    }

    private fun updateSuggestions(basketItems: List<HomeAdItem>) {
        // In a real app, this would come from a repository of all items.
        // For now, I'll use the hardcoded items from HomeUiData.
        val allAds = HomeUiData.Initial.ads
        val favorites = allAds.filter { it.isFavorite && !basketItems.any { b -> b.id == it.id } }
        
        val suggestions = if (favorites.isNotEmpty()) {
            favorites
        } else if (basketItems.isNotEmpty()) {
            val categories = basketItems.map { it.category }.distinct()
            allAds.filter { it.category in categories && !basketItems.any { b -> b.id == it.id } }
        } else {
            emptyList()
        }
        
        updateData { it.copy(suggestions = suggestions) }
    }

    fun onRemoveFromBasket(itemId: Int) {
        basketRepository.removeFromBasket(itemId)
    }

    fun onAddToBasket(item: HomeAdItem) {
        basketRepository.addToBasket(item)
    }
}
