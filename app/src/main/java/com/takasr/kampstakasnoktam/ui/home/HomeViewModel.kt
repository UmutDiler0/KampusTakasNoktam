package com.takasr.kampstakasnoktam.ui.home

import androidx.lifecycle.viewModelScope
import com.takasr.kampstakasnoktam.base.BaseViewModel
import com.takasr.kampstakasnoktam.data.AdRepository
import com.takasr.kampstakasnoktam.data.BasketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val adRepository: AdRepository,
    private val basketRepository: BasketRepository
) : BaseViewModel<HomeUiData>(HomeUiData.Initial) {

    init {
        refreshAds()
    }

    fun refreshAds() {
        setLoading()
        viewModelScope.launch {
            adRepository.getAds().onSuccess { ads ->
                setSuccess(HomeUiData(ads = ads))
            }.onFailure { error ->
                setError(error.message ?: "İlanlar yüklenirken bir hata oluştu")
            }
        }
    }

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
