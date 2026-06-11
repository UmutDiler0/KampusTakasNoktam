package com.takasr.kampstakasnoktam.ui.seller

import androidx.lifecycle.viewModelScope
import com.takasr.kampstakasnoktam.base.BaseViewModel
import com.takasr.kampstakasnoktam.data.model.toUser
import com.takasr.kampstakasnoktam.data.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SellerViewModel @Inject constructor(
    private val apiService: ApiService
) : BaseViewModel<SellerUiData>(SellerUiData.empty()) {

    fun loadSeller(sellerId: String) {
        setLoading()
        viewModelScope.launch {
            try {
                val user = apiService.getSellerProfile(sellerId).toUser()
                setSuccess(SellerUiData(user = user))
            } catch (e: Exception) {
                setError(e.message ?: "Kullanıcı bilgileri alınamadı")
            }
        }
    }
}
