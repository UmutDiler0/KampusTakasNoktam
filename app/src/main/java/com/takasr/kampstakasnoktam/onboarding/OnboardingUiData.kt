package com.takasr.kampstakasnoktam.onboarding

import com.takasr.kampstakasnoktam.R
import com.takasr.kampstakasnoktam.base.UiData

data class OnboardingUiData(
    val pages: List<OnboardingPage> = emptyList(),
    val currentPage: Int = 0
) : UiData {
    companion object {
        val Initial = OnboardingUiData(
            pages = listOf(
                OnboardingPage(
                    titleRes = R.string.onboarding_title_1,
                    descriptionRes = R.string.onboarding_desc_1
                ),
                OnboardingPage(
                    titleRes = R.string.onboarding_title_2,
                    descriptionRes = R.string.onboarding_desc_2
                ),
                OnboardingPage(
                    titleRes = R.string.onboarding_title_3,
                    descriptionRes = R.string.onboarding_desc_3
                )
            ),
            currentPage = 0
        )
    }
}
