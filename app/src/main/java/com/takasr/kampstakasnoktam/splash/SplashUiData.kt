package com.takasr.kampstakasnoktam.splash

import com.takasr.kampstakasnoktam.base.UiData

data class SplashUiData(
    val isVisible: Boolean = true
) : UiData {
    companion object {
        val Initial = SplashUiData()
    }
}
