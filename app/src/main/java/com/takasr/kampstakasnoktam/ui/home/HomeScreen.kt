package com.takasr.kampstakasnoktam.ui.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.takasr.kampstakasnoktam.data.model.Advertisement
import coil.compose.AsyncImage
import com.takasr.kampstakasnoktam.R
import com.takasr.kampstakasnoktam.base.UiState
import com.takasr.kampstakasnoktam.ui.basket.BasketViewModel

enum class BottomNavTab {
    Home,
    Favorites,
    AddItem,
    MyAds,
    Profile
}

private data class BottomNavItem(
    val tab: BottomNavTab,
    val label: Int,
    val icon: ImageVector
)

// Only the 4 regular tabs — AddItem is rendered separately as FAB
private val regularNavItems = listOf(
    BottomNavItem(BottomNavTab.Home, R.string.nav_home, Icons.Default.Home),
    BottomNavItem(BottomNavTab.Favorites, R.string.nav_favorite, Icons.Default.Favorite),
    BottomNavItem(BottomNavTab.MyAds, R.string.nav_my_ads, Icons.Default.List),
    BottomNavItem(BottomNavTab.Profile, R.string.nav_profile, Icons.Default.Person)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onTabSelected: (BottomNavTab) -> Unit,
    onChatClick: () -> Unit,
    onBasketClick: () -> Unit,
    onItemClick: (Int) -> Unit,
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var showFilterSheet by rememberSaveable { mutableStateOf(false) }

    MainTabScaffold(
        selectedTab = BottomNavTab.Home,
        titleRes = R.string.nav_home,
        onTabSelected = onTabSelected,
        onChatClick = onChatClick,
        onBasketClick = onBasketClick,
        modifier = modifier
    ) {
        when (val state = uiState) {
            UiState.Idle,
            UiState.Loading -> LoadingContent()

            is UiState.Error -> EmptyTabContent(title = state.message)
            is UiState.Success -> {
                HomeContent(
                    uiData = state.data,
                    ads = state.data.filteredAds,
                    emptyStateText = stringResource(id = R.string.home_empty_ads),
                    showSearchAndFilter = true,
                    onQueryChanged = viewModel::onQueryChanged,
                    onItemClick = onItemClick,
                    onFavoriteToggle = viewModel::onFavoriteToggle,
                    onFilterClick = { showFilterSheet = true },
                    onAddToBasketClick = viewModel::onAddToBasket
                )

                if (showFilterSheet) {
                    FilterBottomSheet(
                        initialCategory = state.data.filterCategory,
                        initialCondition = state.data.filterCondition,
                        initialMinPrice = state.data.filterMinPrice,
                        initialMaxPrice = state.data.filterMaxPrice,
                        initialIsSwap = state.data.filterIsSwap,
                        onDismiss = { showFilterSheet = false },
                        onApply = { cat, cond, minP, maxP, swap ->
                            viewModel.applyFilters(cat, cond, minP, maxP, swap)
                            showFilterSheet = false
                        },
                        onClear = {
                            viewModel.clearFilters()
                            showFilterSheet = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FavoritesScreen(
    onTabSelected: (BottomNavTab) -> Unit,
    onChatClick: () -> Unit,
    onBasketClick: () -> Unit,
    onItemClick: (Int) -> Unit,
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    MainTabScaffold(
        selectedTab = BottomNavTab.Favorites,
        titleRes = R.string.nav_favorite,
        onTabSelected = onTabSelected,
        onChatClick = onChatClick,
        onBasketClick = onBasketClick,
        modifier = modifier
    ) {
        when (val state = uiState) {
            UiState.Idle,
            UiState.Loading -> LoadingContent()

            is UiState.Error -> EmptyTabContent(title = state.message)
            is UiState.Success -> HomeContent(
                uiData = state.data,
                ads = state.data.filteredFavoriteAds,
                emptyStateText = stringResource(id = R.string.home_empty_favorites),
                showSearchAndFilter = false,
                onQueryChanged = viewModel::onQueryChanged,
                onItemClick = onItemClick,
                onFavoriteToggle = viewModel::onFavoriteToggle,
                onFilterClick = { },
                onAddToBasketClick = viewModel::onAddToBasket
            )
        }
    }
}@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTabScaffold(
    selectedTab: BottomNavTab,
    titleRes: Int,
    onTabSelected: (BottomNavTab) -> Unit,
    onChatClick: () -> Unit,
    onBasketClick: () -> Unit,
    showTopBar: Boolean = true,
    modifier: Modifier = Modifier,
    fab: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val basketViewModel: BasketViewModel = hiltViewModel()
    val basketState by basketViewModel.uiState.collectAsState()
    val basketCount = (basketState as? UiState.Success)?.data?.basketItems?.size ?: 0

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            if (showTopBar) {
                CenterAlignedTopAppBar(
                    title = { Text(text = stringResource(id = titleRes)) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onBackground,
                        actionIconContentColor = MaterialTheme.colorScheme.primary
                    ),
                    actions = {
                        IconButton(onClick = onChatClick) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = stringResource(id = R.string.action_chat)
                            )
                        }
                        IconButton(onClick = onBasketClick) {
                            BadgedBox(
                                badge = {
                                    if (basketCount > 0) {
                                        Badge {
                                            Text(text = basketCount.toString())
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingBasket,
                                    contentDescription = stringResource(id = R.string.action_basket)
                                )
                            }
                        }
                    }
                )
            }
        },
        bottomBar = {
            FloatingBottomNav(
                selectedTab = selectedTab,
                onTabSelected = onTabSelected
            )
        },
        floatingActionButton = { fab?.invoke() }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            content()
        }
    }
}

// ─── Floating Bottom Navigation ───────────────────────────────────────────────

@Composable
private fun FloatingBottomNav(
    selectedTab: BottomNavTab,
    onTabSelected: (BottomNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        // Pill background
        Surface(
            modifier = Modifier
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(50),
                    ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                )
                .fillMaxWidth(),
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left two items: Home, Favorites
                regularNavItems.take(2).forEach { item ->
                    NavIconButton(
                        item = item,
                        isSelected = selectedTab == item.tab,
                        onClick = { onTabSelected(item.tab) }
                    )
                }

                // Center FAB — Add Item
                AddItemFab(onClick = { onTabSelected(BottomNavTab.AddItem) })

                // Right two items: MyAds, Profile
                regularNavItems.takeLast(2).forEach { item ->
                    NavIconButton(
                        item = item,
                        isSelected = selectedTab == item.tab,
                        onClick = { onTabSelected(item.tab) }
                    )
                }
            }
        }
    }
}

@Composable
private fun NavIconButton(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected)
            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
        else
            MaterialTheme.colorScheme.surface,
        animationSpec = spring(),
        label = "navBg"
    )
    val iconTint by animateColorAsState(
        targetValue = if (isSelected)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
        animationSpec = spring(),
        label = "navTint"
    )
    val iconSize by animateDpAsState(
        targetValue = if (isSelected) 26.dp else 22.dp,
        animationSpec = spring(),
        label = "navIconSize"
    )

    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(bgColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = stringResource(id = item.label),
            tint = iconTint,
            modifier = Modifier.size(iconSize)
        )
    }
}

@Composable
private fun AddItemFab(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier.size(56.dp),
        shape = CircleShape,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 6.dp,
            pressedElevation = 2.dp
        )
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(id = R.string.nav_add_item),
            modifier = Modifier.size(28.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterBottomSheet(
    initialCategory: String,
    initialCondition: String,
    initialMinPrice: String,
    initialMaxPrice: String,
    initialIsSwap: Boolean?,
    onDismiss: () -> Unit,
    onApply: (String, String, String, String, Boolean?) -> Unit,
    onClear: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    var category by remember { mutableStateOf(initialCategory) }
    var condition by remember { mutableStateOf(initialCondition) }
    var minPrice by remember { mutableStateOf(initialMinPrice) }
    var maxPrice by remember { mutableStateOf(initialMaxPrice) }
    var isSwap by remember { mutableStateOf(initialIsSwap) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        contentWindowInsets = { WindowInsets.navigationBars }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Filtrele",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
            )
            HorizontalDivider()

            // Kategori
            FilterLabel("Kategori")
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Örn: Elektronik, Kitap") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Durum
            FilterLabel("Durum")
            OutlinedTextField(
                value = condition,
                onValueChange = { condition = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Örn: Sıfır, Az Kullanılmış") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Fiyat Aralığı
            FilterLabel("Fiyat Aralığı (TL)")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = minPrice,
                    onValueChange = { minPrice = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Min") },
                    singleLine = true,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = maxPrice,
                    onValueChange = { maxPrice = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Max") },
                    singleLine = true,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Takas
            FilterLabel("Takas")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = isSwap == null,
                    onClick = { isSwap = null },
                    label = { Text("Tümü") }
                )
                FilterChip(
                    selected = isSwap == true,
                    onClick = { isSwap = true },
                    label = { Text("Takasa Açık") }
                )
                FilterChip(
                    selected = isSwap == false,
                    onClick = { isSwap = false },
                    label = { Text("Sadece Satış") }
                )
            }

            HorizontalDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion { onClear() }
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Temizle")
                }
                Button(
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            onApply(category, condition, minPrice, maxPrice, isSwap)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Uygula")
                }
            }
        }
    }
}

@Composable
private fun FilterLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
private fun HomeContent(
    uiData: HomeUiData,
    ads: List<Advertisement>,
    emptyStateText: String,
    showSearchAndFilter: Boolean,
    onQueryChanged: (String) -> Unit,
    onItemClick: (Int) -> Unit,
    onFavoriteToggle: (Int) -> Unit,
    onFilterClick: () -> Unit,
    onAddToBasketClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = dimensionResource(R.dimen.screen_horizontal_padding),
                vertical = dimensionResource(R.dimen.spacing_md)
            ),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_lg))
    ) {
        if (showSearchAndFilter) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_sm)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = uiData.searchQuery,
                    onValueChange = onQueryChanged,
                    modifier = Modifier.weight(1f),
                    placeholder = { Text(text = stringResource(id = R.string.hint_search_items)) },
                    shape = RoundedCornerShape(dimensionResource(R.dimen.radius_lg)),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )
                if (uiData.isFilterActive) {
                    FilledTonalIconButton(
                        onClick = onFilterClick,
                        modifier = Modifier.size(dimensionResource(R.dimen.spacing_xl) * 2)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = stringResource(id = R.string.action_filter),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                } else {
                    OutlinedIconButton(
                        onClick = onFilterClick,
                        modifier = Modifier.size(dimensionResource(R.dimen.spacing_xl) * 2)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = stringResource(id = R.string.action_filter)
                        )
                    }
                }
            }
        }

        if (ads.isEmpty()) {
            EmptyTabContent(title = emptyStateText)
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_md)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_md)),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = ads,
                    key = { item -> item.id }
                ) { item ->
                    HomeAdCard(
                        item = item,
                        onClick = { onItemClick(item.id) },
                        onFavoriteClick = { onFavoriteToggle(item.id) },
                        onAddToBasketClick = { onAddToBasketClick(item.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeAdCard(
    item: Advertisement,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onAddToBasketClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.78f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(dimensionResource(R.dimen.ad_card_corner_radius)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.ad_card_inner_padding)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_sm))
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(R.dimen.ad_card_image_height))
                )
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(dimensionResource(R.dimen.ad_card_favorite_btn_size))
                ) {
                    Icon(
                        imageVector = if (item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (item.isFavorite) {
                            stringResource(id = R.string.action_remove_favorite)
                        } else {
                            stringResource(id = R.string.action_add_favorite)
                        },
                        tint = if (item.isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${item.formattedPrice} TL",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = item.location,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = item.sellerName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalIconButton(onClick = onAddToBasketClick) {
                    Icon(
                        imageVector = Icons.Default.AddShoppingCart,
                        contentDescription = stringResource(id = R.string.action_add_basket)
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyTabContent(
    title: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
    }
}
