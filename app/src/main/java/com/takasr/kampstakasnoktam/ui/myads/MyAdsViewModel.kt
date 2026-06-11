package com.takasr.kampstakasnoktam.ui.myads

import androidx.lifecycle.viewModelScope
import com.takasr.kampstakasnoktam.base.BaseViewModel
import com.takasr.kampstakasnoktam.base.UiData
import com.takasr.kampstakasnoktam.data.AdRepository
import com.takasr.kampstakasnoktam.data.model.Advertisement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MyAdsUiData(
    val ads: List<Advertisement> = emptyList()
) : UiData

@HiltViewModel
class MyAdsViewModel @Inject constructor(
    private val adRepository: AdRepository
) : BaseViewModel<MyAdsUiData>(MyAdsUiData()) {

    fun loadMyAds() {
        setLoading()
        viewModelScope.launch {
            adRepository.getMyAds()
                .onSuccess { ads -> setSuccess(MyAdsUiData(ads = ads)) }
                .onFailure { e -> setError(e.message ?: "İlanlar yüklenemedi") }
        }
    }

    fun deleteAd(adId: Int) {
        viewModelScope.launch {
            adRepository.deleteAd(adId).onSuccess {
                updateData { current -> current.copy(ads = current.ads.filter { it.id != adId }) }
            }
        }
    }
}
