package com.takasr.kampstakasnoktam.onboarding

import androidx.annotation.DrawableRes
import com.takasr.kampstakasnoktam.R

data class OnboardingPage(
    val titleRes: Int,
    val descriptionRes: Int,
    @param:DrawableRes val imageRes: Int = R.drawable.ic_launcher_foreground
)
