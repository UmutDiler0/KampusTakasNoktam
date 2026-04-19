package com.takasr.kampstakasnoktam.onboarding

import androidx.lifecycle.viewModelScope
import com.takasr.kampstakasnoktam.base.BaseViewModel
import com.takasr.kampstakasnoktam.base.UiState
import com.takasr.kampstakasnoktam.datastore.OnboardingPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingPreferencesManager: OnboardingPreferencesManager
) : BaseViewModel<OnboardingUiData>(OnboardingUiData.Initial) {

    private val _navigationEvent = MutableSharedFlow<OnboardingNavigationTarget>()
    val navigationEvent: SharedFlow<OnboardingNavigationTarget> = _navigationEvent.asSharedFlow()

    fun onPageChanged(page: Int) {
        updateData { current ->
            current.copy(currentPage = page)
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            setLoading()
            onboardingPreferencesManager.setOnboardingCompleted(completed = true)
            setSuccess((uiState.value as? UiState.Success<OnboardingUiData>)?.data ?: OnboardingUiData.Initial)
            _navigationEvent.emit(OnboardingNavigationTarget.Auth)
        }
    }
}

sealed class OnboardingNavigationTarget {
    data object Auth : OnboardingNavigationTarget()
}
