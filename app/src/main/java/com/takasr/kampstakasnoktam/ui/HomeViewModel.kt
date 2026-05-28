package com.takasr.kampstakasnoktam.ui

import com.takasr.kampstakasnoktam.base.BaseViewModel
import com.takasr.kampstakasnoktam.data.BasketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val basketRepository: BasketRepository
) : BaseViewModel<HomeUiData>(HomeUiData.Initial) {

    fun onQueryChanged(query: String) {
        updateData { current ->
            current.copy(searchQuery = query)
        }
    }

    fun onFavoriteToggle(itemId: Int) {
        updateData { current ->
            current.copy(
                ads = current.ads.map { item ->
                    if (item.id == itemId) {
                        item.copy(isFavorite = !item.isFavorite)
                    } else {
                        item
                    }
                }
            )
        }
    }

    fun onAddToBasket(itemId: Int) {
        val item = (uiState.value as? com.takasr.kampstakasnoktam.base.UiState.Success)?.data?.ads?.find { it.id == itemId }
        item?.let {
            basketRepository.addToBasket(it)
        }
    }
}
