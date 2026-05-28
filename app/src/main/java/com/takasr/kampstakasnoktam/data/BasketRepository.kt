package com.takasr.kampstakasnoktam.data

import com.takasr.kampstakasnoktam.ui.HomeAdItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BasketRepository @Inject constructor() {
    private val _basketItems = MutableStateFlow<List<HomeAdItem>>(emptyList())
    val basketItems: StateFlow<List<HomeAdItem>> = _basketItems.asStateFlow()

    fun addToBasket(item: HomeAdItem) {
        if (!_basketItems.value.any { it.id == item.id }) {
            _basketItems.value = _basketItems.value + item
        }
    }

    fun removeFromBasket(itemId: Int) {
        _basketItems.value = _basketItems.value.filter { it.id != itemId }
    }

    fun clearBasket() {
        _basketItems.value = emptyList()
    }
}
