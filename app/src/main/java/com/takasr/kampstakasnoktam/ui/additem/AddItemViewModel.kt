package com.takasr.kampstakasnoktam.ui.additem

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.takasr.kampstakasnoktam.data.AdRepository
import com.takasr.kampstakasnoktam.data.network.AdCreateRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

data class AddItemFormState(
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val isSwap: Boolean = false,
    val condition: String = "",
    val category: String = "",
    val location: String = "",
    val selectedImages: List<Uri> = emptyList()
) {
    val isValid: Boolean
        get() = title.isNotBlank() && description.isNotBlank() &&
                condition.isNotBlank() && category.isNotBlank() && location.isNotBlank()
}

sealed class AddItemUiState {
    object Idle : AddItemUiState()
    object Loading : AddItemUiState()
    object Success : AddItemUiState()
    data class Error(val message: String) : AddItemUiState()
}

@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val adRepository: AdRepository
) : ViewModel() {

    private val _formState = MutableStateFlow(AddItemFormState())
    val formState: StateFlow<AddItemFormState> = _formState.asStateFlow()

    private val _uiState = MutableStateFlow<AddItemUiState>(AddItemUiState.Idle)
    val uiState: StateFlow<AddItemUiState> = _uiState.asStateFlow()

    fun onTitleChanged(value: String) = _formState.update { it.copy(title = value) }
    fun onDescriptionChanged(value: String) = _formState.update { it.copy(description = value) }
    fun onPriceChanged(value: String) = _formState.update { it.copy(price = value.filter { c -> c.isDigit() || c == '.' }) }
    fun onIsSwapChanged(value: Boolean) = _formState.update { it.copy(isSwap = value) }
    fun onConditionChanged(value: String) = _formState.update { it.copy(condition = value) }
    fun onCategoryChanged(value: String) = _formState.update { it.copy(category = value) }
    fun onLocationChanged(value: String) = _formState.update { it.copy(location = value) }
    fun onImagesSelected(uris: List<Uri>) = _formState.update { it.copy(selectedImages = it.selectedImages + uris) }
    fun onImageRemoved(uri: Uri) = _formState.update { it.copy(selectedImages = it.selectedImages - uri) }

    fun submit(context: Context) {
        val form = _formState.value
        if (!form.isValid) return

        _uiState.value = AddItemUiState.Loading
        viewModelScope.launch {
            val request = AdCreateRequest(
                title = form.title.trim(),
                description = form.description.trim(),
                price = form.price.toDoubleOrNull() ?: 0.0,
                isSwap = form.isSwap,
                condition = form.condition.trim(),
                category = form.category.trim(),
                location = form.location.trim()
            )

            adRepository.createAd(request).onSuccess { ad ->
                if (form.selectedImages.isNotEmpty()) {
                    val parts = form.selectedImages.mapNotNull { uri ->
                        try {
                            val bytes = context.contentResolver.openInputStream(uri)?.readBytes() ?: return@mapNotNull null
                            val mime = context.contentResolver.getType(uri) ?: "image/jpeg"
                            val reqBody = bytes.toRequestBody(mime.toMediaTypeOrNull())
                            MultipartBody.Part.createFormData("files", "image_${System.currentTimeMillis()}.jpg", reqBody)
                        } catch (e: Exception) { null }
                    }
                    if (parts.isNotEmpty()) {
                        adRepository.uploadImages(ad.id, parts)
                    }
                }
                _uiState.value = AddItemUiState.Success
            }.onFailure { e ->
                _uiState.value = AddItemUiState.Error(e.message ?: "İlan oluşturulamadı")
            }
        }
    }

    fun resetError() {
        _uiState.value = AddItemUiState.Idle
    }
}

private fun <T> MutableStateFlow<T>.update(transform: (T) -> T) {
    value = transform(value)
}
