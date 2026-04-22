package com.takasr.kampstakasnoktam.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import coil.compose.AsyncImage
import com.takasr.kampstakasnoktam.R
import com.takasr.kampstakasnoktam.base.UiState

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

private val bottomNavItems = listOf(
    BottomNavItem(BottomNavTab.Home, R.string.nav_home, Icons.Default.Home),
    BottomNavItem(BottomNavTab.Favorites, R.string.nav_favorite, Icons.Default.Favorite),
    BottomNavItem(BottomNavTab.AddItem, R.string.nav_add_item, Icons.Default.AddCircle),
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
    viewModel: HomeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

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
            is UiState.Success -> HomeContent(
                uiData = state.data,
                onQueryChanged = viewModel::onQueryChanged,
                onItemClick = onItemClick,
                onFavoriteToggle = viewModel::onFavoriteToggle,
                onFilterClick = { },
                onAddToBasketClick = { }
            )
        }
    }
}

@Composable
fun FavoritesScreen(
    onTabSelected: (BottomNavTab) -> Unit,
    onChatClick: () -> Unit,
    onBasketClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    MainTabScaffold(
        selectedTab = BottomNavTab.Favorites,
        titleRes = R.string.nav_favorite,
        onTabSelected = onTabSelected,
        onChatClick = onChatClick,
        onBasketClick = onBasketClick,
        modifier = modifier
    ) {
        EmptyTabContent(title = stringResource(id = R.string.nav_favorite))
    }
}

@Composable
fun AddItemScreen(
    onTabSelected: (BottomNavTab) -> Unit,
    onChatClick: () -> Unit,
    onBasketClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    MainTabScaffold(
        selectedTab = BottomNavTab.AddItem,
        titleRes = R.string.nav_add_item,
        onTabSelected = onTabSelected,
        onChatClick = onChatClick,
        onBasketClick = onBasketClick,
        modifier = modifier
    ) {
        EmptyTabContent(title = stringResource(id = R.string.nav_add_item))
    }
}

@Composable
fun MyAdsScreen(
    onTabSelected: (BottomNavTab) -> Unit,
    onChatClick: () -> Unit,
    onBasketClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    MainTabScaffold(
        selectedTab = BottomNavTab.MyAds,
        titleRes = R.string.nav_my_ads,
        onTabSelected = onTabSelected,
        onChatClick = onChatClick,
        onBasketClick = onBasketClick,
        modifier = modifier
    ) {
        EmptyTabContent(title = stringResource(id = R.string.nav_my_ads))
    }
}

@Composable
fun ProfileScreen(
    onTabSelected: (BottomNavTab) -> Unit,
    onChatClick: () -> Unit,
    onBasketClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    MainTabScaffold(
        selectedTab = BottomNavTab.Profile,
        titleRes = R.string.nav_profile,
        onTabSelected = onTabSelected,
        onChatClick = onChatClick,
        onBasketClick = onBasketClick,
        modifier = modifier
    ) {
        EmptyTabContent(title = stringResource(id = R.string.nav_profile))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainTabScaffold(
    selectedTab: BottomNavTab,
    titleRes: Int,
    onTabSelected: (BottomNavTab) -> Unit,
    onChatClick: () -> Unit,
    onBasketClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
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
                        Icon(
                            imageVector = Icons.Default.ShoppingBasket,
                            contentDescription = stringResource(id = R.string.action_basket)
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        selected = selectedTab == item.tab,
                        onClick = { onTabSelected(item.tab) },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = stringResource(id = item.label)
                            )
                        },
                        label = { Text(text = stringResource(id = item.label)) }
                    )
                }
            }
        }
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

@Composable
private fun HomeContent(
    uiData: HomeUiData,
    onQueryChanged: (String) -> Unit,
    onItemClick: (Int) -> Unit,
    onFavoriteToggle: (Int) -> Unit,
    onFilterClick: () -> Unit,
    onAddToBasketClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = uiData.searchQuery,
                onValueChange = onQueryChanged,
                modifier = Modifier.weight(1f),
                placeholder = { Text(text = stringResource(id = R.string.hint_search_items)) },
                shape = RoundedCornerShape(18.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )
            OutlinedIconButton(
                onClick = onFilterClick,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = stringResource(id = R.string.action_filter)
                )
            }
        }

        if (uiData.filteredAds.isEmpty()) {
            EmptyTabContent(title = stringResource(id = R.string.home_empty_ads))
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = uiData.filteredAds,
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
    item: HomeAdItem,
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
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                )
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(34.dp)
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
                    text = item.price,
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
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
