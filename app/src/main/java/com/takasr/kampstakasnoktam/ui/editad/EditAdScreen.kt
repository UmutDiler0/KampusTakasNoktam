package com.takasr.kampstakasnoktam.ui.editad

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAdScreen(
    onBackClick: () -> Unit,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditAdViewModel = hiltViewModel()
) {
    val form by viewModel.formState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is EditAdUiState.Success -> onSuccess()
            is EditAdUiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetError()
            }
            else -> Unit
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = "İlanı Düzenle", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { innerPadding ->
        when (uiState) {
            is EditAdUiState.Loading -> {
                // Show loading only on initial load (form is empty); during submit show in button
                if (form.title.isBlank()) {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }
                    return@Scaffold
                }
            }
            else -> Unit
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── Başlık ───────────────────────────────────────────────────────
            SectionLabel("Başlık")
            OutlinedTextField(
                value = form.title,
                onValueChange = viewModel::onTitleChanged,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Ürünün adını yazın") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // ── Açıklama ─────────────────────────────────────────────────────
            SectionLabel("Açıklama")
            OutlinedTextField(
                value = form.description,
                onValueChange = viewModel::onDescriptionChanged,
                modifier = Modifier.fillMaxWidth().height(120.dp),
                placeholder = { Text("Ürününüzü detaylı açıklayın") },
                shape = RoundedCornerShape(12.dp)
            )

            // ── Fiyat & Takas ────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    SectionLabel("Fiyat (TL)")
                    OutlinedTextField(
                        value = form.price,
                        onValueChange = viewModel::onPriceChanged,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("0") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    SectionLabel("Takas")
                    Switch(checked = form.isSwap, onCheckedChange = viewModel::onIsSwapChanged)
                }
            }

            // ── Durum ────────────────────────────────────────────────────────
            SectionLabel("Durum")
            OutlinedTextField(
                value = form.condition,
                onValueChange = viewModel::onConditionChanged,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Örn: Sıfır, Az Kullanılmış, İyi") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // ── Kategori ─────────────────────────────────────────────────────
            SectionLabel("Kategori")
            OutlinedTextField(
                value = form.category,
                onValueChange = viewModel::onCategoryChanged,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Örn: Elektronik, Kitap, Giyim") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // ── Konum ────────────────────────────────────────────────────────
            SectionLabel("Konum")
            OutlinedTextField(
                value = form.location,
                onValueChange = viewModel::onLocationChanged,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Örn: İstanbul, Kadıköy") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ── Güncelle ─────────────────────────────────────────────────────
            Button(
                onClick = viewModel::submit,
                enabled = form.isValid && uiState !is EditAdUiState.Loading,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (uiState is EditAdUiState.Loading && form.title.isNotBlank()) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Güncelle", fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onBackground
    )
}
