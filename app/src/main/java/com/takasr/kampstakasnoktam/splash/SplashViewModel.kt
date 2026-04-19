package com.takasr.kampstakasnoktam.splash

import androidx.lifecycle.viewModelScope
import com.takasr.kampstakasnoktam.base.BaseViewModel
import com.takasr.kampstakasnoktam.datastore.OnboardingPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val onboardingPreferencesManager: OnboardingPreferencesManager
) : BaseViewModel<SplashUiData>(SplashUiData.Initial) {

    private val _navigationEvent = MutableSharedFlow<SplashNavigationTarget>()
    val navigationEvent: SharedFlow<SplashNavigationTarget> = _navigationEvent.asSharedFlow()

    init {
        resolveStartDestination()
    }

    private fun resolveStartDestination() {
        viewModelScope.launch {
            setLoading()
            delay(3000)
            val isOnboardingCompleted = onboardingPreferencesManager.isOnboardingCompleted.first()
            setSuccess(SplashUiData(isVisible = false))
            val target = if (isOnboardingCompleted) {
                SplashNavigationTarget.Auth
            } else {
                SplashNavigationTarget.Onboarding
            }
            _navigationEvent.emit(target)
        }
    }
}

sealed class SplashNavigationTarget {
    data object Onboarding : SplashNavigationTarget()
    data object Auth : SplashNavigationTarget()
}
