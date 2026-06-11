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
import androidx.compose.material.icons.filled.ChatBubbleOutline
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.takasr.kampstakasnoktam.R
import com.takasr.kampstakasnoktam.data.model.Advertisement

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
                title = { Text(itemDetail.title, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // ── Fotoğraflar ──────────────────────────────────────────────────
            ImagePagerSection(
                imageUrls = itemDetail.imageUrls,
                onImageClick = { index ->
                    selectedImageIndex = index
                    showFullScreenImage = true
                }
            )

            // ── Fiyat & Sepet ────────────────────────────────────────────────
            PriceAndBasketSection(
                price = itemDetail.price,
                onAddToBasket = { onAddToBasket(item.id) }
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            // ── Satıcı ───────────────────────────────────────────────────────
            SellerSection(
                itemDetail = itemDetail,
                onSellerClick = onSellerClick,
                onSendMessageClick = onSendMessageClick
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            // ── Açıklama ─────────────────────────────────────────────────────
            DescriptionSection(description = itemDetail.description)

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            // ── Ürün Bilgisi ─────────────────────────────────────────────────
            InformationSection(itemInformation = itemDetail.itemInformation)

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showFullScreenImage) {
        FullScreenImageDialog(
            imageUrls = itemDetail.imageUrls,
            initialIndex = selectedImageIndex,
            onDismiss = { showFullScreenImage = false }
        )
    }
}

private fun Advertisement.toItemDetailData(): ItemDetailData {
    val priceText = if (price % 1.0 == 0.0) "${price.toLong()} TL"
    else String.format("%.2f TL", price)

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
            append("Durum: $condition\n")
            append("Kategori: $category\n")
            append("Takas: ${if (isSwap) "Evet" else "Hayır"}\n")
            append("Konum: $location\n")
            append("Tarih: $createdAt")
        }
    )
}

// ─── Image Pager ──────────────────────────────────────────────────────────────

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
            modifier = Modifier.fillMaxWidth().height(300.dp)
        ) { page ->
            AsyncImage(
                model = imageUrls[page],
                contentDescription = null,
                modifier = Modifier.fillMaxSize().clickable { onImageClick(page) },
                contentScale = ContentScale.Crop
            )
        }

        if (imageUrls.size > 1) {
            Row(
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(imageUrls.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .size(width = if (isSelected) 20.dp else 6.dp, height = 6.dp)
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

// ─── Price & Basket ───────────────────────────────────────────────────────────

@Composable
private fun PriceAndBasketSection(
    price: String,
    onAddToBasket: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = price,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Button(onClick = onAddToBasket, shape = RoundedCornerShape(8.dp)) {
            Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
            Text(stringResource(R.string.action_add_basket))
        }
    }
}

// ─── Seller Section ───────────────────────────────────────────────────────────

@Composable
private fun SellerSection(
    itemDetail: ItemDetailData,
    onSellerClick: (String) -> Unit,
    onSendMessageClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Satıcı",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Card(
            modifier = Modifier.fillMaxWidth().clickable { onSellerClick(itemDetail.sellerId) },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.07f)
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier.size(44.dp).clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = itemDetail.sellerName.firstOrNull()?.toString() ?: "?",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
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
                Icon(Icons.Default.ChevronRight, contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary)
            }
        }

        Button(
            onClick = { onSendMessageClick(itemDetail.sellerId) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(Icons.Default.ChatBubbleOutline, contentDescription = null,
                modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Mesaj Gönder", fontWeight = FontWeight.SemiBold)
        }
    }
}

// ─── Description Section ──────────────────────────────────────────────────────

@Composable
private fun DescriptionSection(description: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Açıklama", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.3f
        )
    }
}

// ─── Information Section ──────────────────────────────────────────────────────

@Composable
private fun InformationSection(itemInformation: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Ürün Bilgisi", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        itemInformation.lines().forEach { line ->
            if (line.contains(":")) {
                val parts = line.split(":", limit = 2)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = parts[0] + ":",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(0.35f)
                    )
                    Text(
                        text = parts[1].trim(),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(0.65f)
                    )
                }
            }
        }
    }
}

// ─── Full Screen Image ────────────────────────────────────────────────────────

@Composable
private fun FullScreenImageDialog(
    imageUrls: List<String>,
    initialIndex: Int,
    onDismiss: () -> Unit
) {
    val pagerState = rememberPagerState(initialPage = initialIndex, pageCount = { imageUrls.size })

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
                AsyncImage(
                    model = imageUrls[page], contentDescription = null,
                    modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Fit
                )
            }
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.TopEnd).padding(16.dp).size(48.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
            }
            if (imageUrls.size > 1) {
                Row(
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 32.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    repeat(imageUrls.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .size(width = if (isSelected) 20.dp else 6.dp, height = 6.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) Color.White else Color.White.copy(alpha = 0.5f))
                        )
                    }
                }
            }
        }
    }
}
