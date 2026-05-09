package com.takasr.kampstakasnoktam.ui.seller

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.takasr.kampstakasnoktam.R
import com.takasr.kampstakasnoktam.base.UiState
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerScreen(
    sellerId: Int,
    onBackClick: () -> Unit,
    onAdClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SellerViewModel = hiltViewModel()
) {
    LaunchedEffect(sellerId) {
        viewModel.loadSeller(sellerId)
    }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.seller_profile_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.seller_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { innerPadding ->
        when (val state = uiState) {
            UiState.Idle, UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            is UiState.Success -> {
                SellerContent(
                    data = state.data,
                    onAdClick = onAdClick,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

// ─── Main Content ─────────────────────────────────────────────────────────────

@Composable
private fun SellerContent(
    data: SellerUiData,
    onAdClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_md))
    ) {
        // ── Profile Hero Card ────────────────────────────────────────────────
        item {
            SellerProfileHero(seller = data.seller)
        }

        // ── Contact & Verification ───────────────────────────────────────────
        item {
            SellerContactSection(seller = data.seller)
        }

        // ── Rating summary ───────────────────────────────────────────────────
        item {
            SellerRatingSummary(seller = data.seller)
        }

        // ── Active Ads ───────────────────────────────────────────────────────
        item {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xs)))
            SectionTitle(
                title = stringResource(R.string.seller_section_ads),
                badge = "${data.ads.size}"
            )
        }

        item {
            SellerAdsRow(ads = data.ads, onAdClick = onAdClick)
        }

        // ── Reviews ──────────────────────────────────────────────────────────
        item {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xs)))
            SectionTitle(
                title = stringResource(R.string.seller_section_reviews),
                badge = "${data.reviews.size}"
            )
        }

        items(data.reviews, key = { it.id }) { review ->
            ReviewCard(review = review)
        }

        item { Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xl))) }
    }
}

// ─── Profile Hero ─────────────────────────────────────────────────────────────

@Composable
private fun SellerProfileHero(
    seller: SellerProfile,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.20f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(
                horizontal = dimensionResource(R.dimen.seller_hero_horizontal_padding),
                vertical = dimensionResource(R.dimen.seller_hero_vertical_padding)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_md))
        ) {
            // Avatar
            Box(contentAlignment = Alignment.BottomEnd) {
                if (seller.profileImageUrl != null) {
                    AsyncImage(
                        model = seller.profileImageUrl,
                        contentDescription = seller.fullName,
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.seller_avatar_size))
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.seller_avatar_size))
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.secondary
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = seller.initials,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                // Verified badge on avatar
                if (seller.isEmailVerified) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = stringResource(R.string.seller_verified),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.seller_avatar_badge_size))
                            .background(
                                MaterialTheme.colorScheme.background,
                                CircleShape
                            )
                    )
                }
            }

            // Name
            Text(
                text = seller.fullName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            // University
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_xs))
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(dimensionResource(R.dimen.seller_section_icon_size))
                )
                Text(
                    text = seller.university,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f)
                )
            }

            // Member since
            Text(
                text = stringResource(R.string.seller_member_since, seller.memberSince),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f)
            )
        }
    }
}

// ─── Contact & Verification Section ──────────────────────────────────────────

@Composable
private fun SellerContactSection(
    seller: SellerProfile,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.screen_horizontal_padding)),
        shape = RoundedCornerShape(dimensionResource(R.dimen.seller_card_corner_radius)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(R.dimen.seller_card_inner_horizontal_padding),
                    vertical = dimensionResource(R.dimen.seller_card_inner_vertical_padding)
                ),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_lg))
        ) {
            Text(
                text = stringResource(R.string.seller_contact_title),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            // Email row with verified badge
            ContactRow(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(dimensionResource(R.dimen.seller_contact_icon_size))
                    )
                },
                label = stringResource(R.string.seller_label_email),
                value = seller.email,
                trailing = {
                    if (seller.isEmailVerified) {
                        VerifiedBadge()
                    } else {
                        UnverifiedBadge()
                    }
                }
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

            // Phone row
            ContactRow(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(dimensionResource(R.dimen.seller_contact_icon_size))
                    )
                },
                label = stringResource(R.string.seller_label_phone),
                value = seller.phone
            )
        }
    }
}

@Composable
private fun ContactRow(
    icon: @Composable () -> Unit,
    label: String,
    value: String,
    trailing: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_md))
    ) {
        icon()
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        trailing?.invoke()
    }
}

@Composable
private fun VerifiedBadge() {
    Surface(
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = dimensionResource(R.dimen.spacing_md),
                vertical = dimensionResource(R.dimen.spacing_xs)
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_xs))
        ) {
            Icon(
                imageVector = Icons.Default.VerifiedUser,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(dimensionResource(R.dimen.seller_verified_icon_size))
            )
            Text(
                text = stringResource(R.string.seller_verified),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun UnverifiedBadge() {
    Surface(
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.error.copy(alpha = 0.10f)
    ) {
        Text(
            text = stringResource(R.string.seller_not_verified),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(
                horizontal = dimensionResource(R.dimen.spacing_md),
                vertical = dimensionResource(R.dimen.spacing_xs)
            )
        )
    }
}

// ─── Rating Summary ───────────────────────────────────────────────────────────

@Composable
private fun SellerRatingSummary(
    seller: SellerProfile,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.screen_horizontal_padding)),
        shape = RoundedCornerShape(dimensionResource(R.dimen.seller_card_corner_radius)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(R.dimen.seller_card_inner_horizontal_padding),
                    vertical = dimensionResource(R.dimen.seller_card_inner_vertical_padding)
                ),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatItem(label = stringResource(R.string.seller_stat_rating), value = "%.1f".format(seller.rating)) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(dimensionResource(R.dimen.seller_stat_icon_size))
                )
            }
            VerticalSeparator()
            StatItem(label = stringResource(R.string.seller_stat_sales), value = "${seller.totalSales}")
            VerticalSeparator()
            StatItem(label = stringResource(R.string.seller_stat_reviews), value = "${seller.totalReviews}")
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_xs))
    ) {
        if (icon != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_xs))
            ) {
                icon()
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        } else {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun VerticalSeparator() {
    Box(
        modifier = Modifier
            .width(dimensionResource(R.dimen.spacing_xs) / 4) // 1dp equivalent via smallest token fraction
            .height(dimensionResource(R.dimen.seller_separator_height))
            .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    )
}

// ─── Seller Ads Row ───────────────────────────────────────────────────────────

@Composable
private fun SellerAdsRow(
    ads: List<SellerAd>,
    onAdClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (ads.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.spacing_lg)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.seller_no_active_ads),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    } else {
        LazyRow(
            modifier = modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.screen_horizontal_padding)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_md))
        ) {
            items(ads, key = { it.id }) { ad ->
                SellerAdCard(ad = ad, onClick = { onAdClick(ad.id) })
            }
        }
    }
}

@Composable
private fun SellerAdCard(
    ad: SellerAd,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(dimensionResource(R.dimen.seller_ad_card_width))
            .aspectRatio(0.75f),
        onClick = onClick,
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_lg)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.ad_card_inner_padding)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_sm))
        ) {
            AsyncImage(
                model = ad.imageUrl,
                contentDescription = ad.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.seller_ad_card_image_height))
                    .clip(RoundedCornerShape(dimensionResource(R.dimen.ad_card_image_corner_radius)))
            )
            Text(
                text = ad.title,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = ad.price,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// ─── Review Card ──────────────────────────────────────────────────────────────

@Composable
private fun ReviewCard(
    review: SellerReview,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(R.dimen.screen_horizontal_padding),
                vertical = dimensionResource(R.dimen.spacing_xs)
            ),
        shape = RoundedCornerShape(dimensionResource(R.dimen.seller_card_corner_radius)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(R.dimen.seller_review_horizontal_padding),
                    vertical = dimensionResource(R.dimen.seller_review_vertical_padding)
                ),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_sm))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_md))
            ) {
                // Reviewer avatar
                if (review.reviewerImageUrl != null) {
                    AsyncImage(
                        model = review.reviewerImageUrl,
                        contentDescription = review.reviewerName,
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.seller_reviewer_avatar_size))
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.seller_reviewer_avatar_size))
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = review.reviewerInitials,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = review.reviewerName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = review.date,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }

                // Star rating
                StarRating(rating = review.rating)
            }

            Text(
                text = review.comment,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.3f
            )
        }
    }
}

@Composable
private fun StarRating(
    rating: Float,
    modifier: Modifier = Modifier
) {
    val fullStars = rating.roundToInt().coerceIn(0, 5)
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_xs) / 2)) {
        repeat(5) { index ->
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (index < fullStars) {
                    MaterialTheme.colorScheme.tertiary
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                },
                modifier = Modifier.size(dimensionResource(R.dimen.seller_star_icon_size))
            )
        }
    }
}

// ─── Helpers ──────────────────────────────────────────────────────────────────

@Composable
private fun SectionTitle(
    title: String,
    badge: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(R.dimen.screen_horizontal_padding),
                vertical = dimensionResource(R.dimen.spacing_xs)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_sm))
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f)
        )
        Surface(
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
        ) {
            Text(
                text = badge,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(R.dimen.spacing_sm),
                    vertical = dimensionResource(R.dimen.spacing_xs) / 2
                )
            )
        }
    }
}
