package com.takasr.kampstakasnoktam.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.takasr.kampstakasnoktam.R
import com.takasr.kampstakasnoktam.base.UiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun OnboardingRoute(
    onNavigateAuth: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val data = (state as? UiState.Success<OnboardingUiData>)?.data ?: OnboardingUiData.Initial
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = data.currentPage) {
        data.pages.size
    }

    LaunchedEffect(pagerState.currentPage) {
        viewModel.onPageChanged(pagerState.currentPage)
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest { target ->
            when (target) {
                OnboardingNavigationTarget.Auth -> onNavigateAuth()
            }
        }
    }

    OnboardingScreen(
        data = data,
        currentPage = pagerState.currentPage,
        onSkipClick = viewModel::completeOnboarding,
        onNextClick = {
            if (pagerState.currentPage == data.pages.lastIndex) {
                viewModel.completeOnboarding()
            } else {
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            }
        },
        pagerContent = {
            HorizontalPager(state = pagerState) { page ->
                OnboardingPageContent(page = data.pages[page])
            }
        }
    )
}

@Composable
fun OnboardingScreen(
    data: OnboardingUiData,
    currentPage: Int,
    onSkipClick: () -> Unit,
    onNextClick: () -> Unit,
    pagerContent: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(data.pages.size) { index ->
                val isSelected = index == currentPage
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(
                            if (isSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                            }
                        )
                )

                if (index != data.pages.lastIndex) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.weight(1f)) {
            pagerContent()
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onSkipClick) {
                Text(text = stringResource(id = R.string.action_skip))
            }
            Button(onClick = onNextClick) {
                Text(
                    text = if (currentPage == data.pages.lastIndex) {
                        stringResource(id = R.string.action_get_started)
                    } else {
                        stringResource(id = R.string.action_continue)
                    }
                )
            }
        }
    }
}

@Composable
private fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = page.imageRes),
            contentDescription = null,
            modifier = Modifier.height(180.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(id = page.titleRes),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(id = page.descriptionRes),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

