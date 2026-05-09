package com.takasr.kampstakasnoktam.ui.seller

import com.takasr.kampstakasnoktam.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SellerViewModel @Inject constructor() : BaseViewModel<SellerUiData>(SellerUiData.mock(0)) {

    /**
     * Load seller data for the given [sellerId].
     * In a real app this would call a repository which fetches from network/DB.
     */
    fun loadSeller(sellerId: Int) {
        setLoading()
        // TODO: replace with real repository call
        setSuccess(SellerUiData.mock(sellerId))
    }
}
