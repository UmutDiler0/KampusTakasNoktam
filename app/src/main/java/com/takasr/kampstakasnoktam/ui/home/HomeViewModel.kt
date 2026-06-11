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
        val current = (uiState.value as? com.takasr.kampstakasnoktam.base.UiState.Success)?.data
        val filters = current ?: HomeUiData.Initial
        setLoading()
        viewModelScope.launch {
            adRepository.discoverAds(
                category = filters.filterCategory.takeIf { it.isNotBlank() },
                condition = filters.filterCondition.takeIf { it.isNotBlank() },
                minPrice = filters.filterMinPrice.toDoubleOrNull(),
                maxPrice = filters.filterMaxPrice.toDoubleOrNull(),
                isSwap = filters.filterIsSwap
            ).onSuccess { ads ->
                setSuccess((current ?: HomeUiData.Initial).copy(ads = ads))
            }.onFailure { error ->
                setError(error.message ?: "İlanlar yüklenirken bir hata oluştu")
            }
        }
    }

    fun applyFilters(
        category: String,
        condition: String,
        minPrice: String,
        maxPrice: String,
        isSwap: Boolean?
    ) {
        setLoading()
        viewModelScope.launch {
            adRepository.discoverAds(
                category = category.takeIf { it.isNotBlank() },
                condition = condition.takeIf { it.isNotBlank() },
                minPrice = minPrice.toDoubleOrNull(),
                maxPrice = maxPrice.toDoubleOrNull(),
                isSwap = isSwap
            ).onSuccess { ads ->
                setSuccess(
                    HomeUiData(
                        ads = ads,
                        filterCategory = category,
                        filterCondition = condition,
                        filterMinPrice = minPrice,
                        filterMaxPrice = maxPrice,
                        filterIsSwap = isSwap
                    )
                )
            }.onFailure { error ->
                setError(error.message ?: "Filtreleme başarısız")
            }
        }
    }

    fun clearFilters() {
        setLoading()
        viewModelScope.launch {
            adRepository.discoverAds()
                .onSuccess { ads -> setSuccess(HomeUiData(ads = ads)) }
                .onFailure { error -> setError(error.message ?: "İlanlar yüklenirken bir hata oluştu") }
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
