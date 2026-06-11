package com.takasr.kampstakasnoktam.ui.itemdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.takasr.kampstakasnoktam.R
import com.takasr.kampstakasnoktam.data.model.Advertisement
import kotlinx.coroutines.launch

// Data model for detailed item information
data class ItemDetailData(
    val id: Int,
    val sellerId: String,
    val title: String,
    val price: String,
    val sellerName: String,
    val location: String,
    val imageUrls: List<String>,
    val description: String,
    val sellerDescription: String,
    val itemInformation: String,
    val isFavorite: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(
    item: Advertisement?,
    onBackClick: () -> Unit = {},
    onAddToBasket: (Int) -> Unit = {},
    onSellerClick: (sellerId: String) -> Unit = {},
    onSendMessageClick: (targetUserId: String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (item == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val itemDetail = remember(item) { item.toItemDetailData() }

    var showFullScreenImage by remember { mutableStateOf(false) }
    var selectedImageIndex by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(itemDetail.title) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Image Pager Section
            ImagePagerSection(
                imageUrls = itemDetail.imageUrls,
                onImageClick = { index ->
                    selectedImageIndex = index
                    showFullScreenImage = true
                }
            )

            // Price and Add to Basket Section
            PriceAndBasketSection(
                price = itemDetail.price,
                onAddToBasket = { onAddToBasket(item.id) }
            )

            // Item Details Tabs
            ItemDetailsTabs(
                itemDetail = itemDetail,
                onSellerClick = onSellerClick,
                onSendMessageClick = onSendMessageClick
            )
        }
    }

    // Full Screen Image Dialog
    if (showFullScreenImage) {
        FullScreenImageDialog(
            imageUrls = itemDetail.imageUrls,
            initialIndex = selectedImageIndex,
            onDismiss = { showFullScreenImage = false }
        )
    }
}

private fun Advertisement.toItemDetailData(): ItemDetailData {
    val priceText = if (price % 1.0 == 0.0) {
        "${price.toLong()} TL"
    } else {
        String.format("%.2f TL", price)
    }

    val urls = imageUrls.map { url ->
        if (url.startsWith("http")) url else "https://kampustakasnoktam.keserbaros.com$url"
    }

    return ItemDetailData(
        id = id,
        sellerId = sellerId,
        title = title,
        price = priceText,
        sellerName = sellerName,
        location = location,
        imageUrls = if (urls.isNotEmpty()) urls else listOf("https://picsum.photos/seed/${id}/600/400"),
        description = description,
        sellerDescription = sellerName,
        itemInformation = buildString {
            append("Condition: $condition\n")
            append("Category: $category\n")
            append("Swap: ${if (isSwap) "Yes" else "No"}\n")
            append("Created At: $createdAt\n")
            append("Active: ${if (isActive) "Yes" else "No"}")
        }
    )
}

@Composable
private fun ImagePagerSection(
    imageUrls: List<String>,
    onImageClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { imageUrls.size })

    Box(modifier = modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) { page ->
            AsyncImage(
                model = imageUrls[page],
                contentDescription = "Item image ${page + 1}",
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onImageClick(page) },
                contentScale = ContentScale.Crop
            )
        }

        // Custom Page Indicator
        if (imageUrls.size > 1) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(imageUrls.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .size(
                                width = if (isSelected) 24.dp else 8.dp,
                                height = 8.dp
                            )
                            .clip(CircleShape)
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun PriceAndBasketSection(
    price: String,
    onAddToBasket: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = price,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Button(
            onClick = onAddToBasket,
            modifier = Modifier.height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                Icons.Default.ShoppingCart,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.action_add_basket),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ItemDetailsTabs(
    itemDetail: ItemDetailData,
    onSellerClick: (String) -> Unit,
    onSendMessageClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column(modifier = modifier.fillMaxWidth()) {
        PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                text = { Text("Description") }
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                text = { Text("Seller") }
            )
            Tab(
                selected = selectedTabIndex == 2,
                onClick = { selectedTabIndex = 2 },
                text = { Text("Information") }
            )
        }

        when (selectedTabIndex) {
            0 -> ItemDescriptionTab(itemDetail.description)
            1 -> SellerDescriptionTab(
                itemDetail = itemDetail,
                onSellerClick = onSellerClick,
                onSendMessageClick = onSendMessageClick
            )
            2 -> ItemInformationTab(itemDetail.itemInformation)
        }
    }
}

@Composable
private fun ItemDescriptionTab(description: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Item Description",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.2f
        )
    }
}

@Composable
private fun SellerDescriptionTab(
    itemDetail: ItemDetailData,
    onSellerClick: (String) -> Unit,
    onSendMessageClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "About the Seller",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            
            TextButton(onClick = { onSendMessageClick(itemDetail.sellerId.toString()) }) {
                Text("Send Message")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Clickable seller header ─ navigates to SellerScreen
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSellerClick(itemDetail.sellerId) },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.07f)
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = itemDetail.sellerName.first().toString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = itemDetail.sellerName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = itemDetail.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Satıcı Profili",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = itemDetail.sellerDescription,
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.2f
        )
    }
}

@Composable
private fun ItemInformationTab(itemInformation: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Item Information",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Parse the item information and display as key-value pairs
        val infoLines = itemInformation.lines()
        infoLines.forEach { line ->
            if (line.contains(":")) {
                val (key, value) = line.split(":", limit = 2)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "$key:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(0.4f)
                    )
                    Text(
                        text = value.trim(),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(0.6f)
                    )
                }
            } else if (line.isNotBlank()) {
                Text(
                    text = line,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun FullScreenImageDialog(
    imageUrls: List<String>,
    initialIndex: Int,
    onDismiss: () -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = initialIndex,
        pageCount = { imageUrls.size }
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                AsyncImage(
                    model = imageUrls[page],
                    contentDescription = "Full screen image ${page + 1}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            // Close button
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(48.dp)
                    .background(
                        Color.Black.copy(alpha = 0.5f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }

            // Page indicator for full screen
            if (imageUrls.size > 1) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(imageUrls.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .size(
                                    width = if (isSelected) 24.dp else 8.dp,
                                    height = 8.dp
                                )
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) Color.White
                                    else Color.White.copy(alpha = 0.5f)
                                )
                        )
                    }
                }
            }
        }
    }
}
