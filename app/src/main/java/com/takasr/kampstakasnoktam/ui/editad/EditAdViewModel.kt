package com.takasr.kampstakasnoktam.ui.editad

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.takasr.kampstakasnoktam.data.AdRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditAdFormState(
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val isSwap: Boolean = false,
    val condition: String = "",
    val category: String = "",
    val location: String = ""
) {
    val isValid: Boolean
        get() = title.isNotBlank() && description.isNotBlank() &&
                condition.isNotBlank() && category.isNotBlank() && location.isNotBlank()
}

sealed class EditAdUiState {
    data object Loading : EditAdUiState()
    data object Idle : EditAdUiState()
    data object Success : EditAdUiState()
    data class Error(val message: String) : EditAdUiState()
}

@HiltViewModel
class EditAdViewModel @Inject constructor(
    private val adRepository: AdRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val adId: Int = checkNotNull(savedStateHandle["adId"])

    private val _formState = MutableStateFlow(EditAdFormState())
    val formState: StateFlow<EditAdFormState> = _formState.asStateFlow()

    private val _uiState = MutableStateFlow<EditAdUiState>(EditAdUiState.Loading)
    val uiState: StateFlow<EditAdUiState> = _uiState.asStateFlow()

    init {
        loadAd()
    }

    private fun loadAd() {
        _uiState.value = EditAdUiState.Loading
        viewModelScope.launch {
            adRepository.getAd(adId)
                .onSuccess { ad ->
                    _formState.value = EditAdFormState(
                        title = ad.title,
                        description = ad.description,
                        price = ad.formattedPrice,
                        isSwap = ad.isSwap,
                        condition = ad.condition,
                        category = ad.category,
                        location = ad.location
                    )
                    _uiState.value = EditAdUiState.Idle
                }
                .onFailure { e ->
                    _uiState.value = EditAdUiState.Error(e.message ?: "İlan yüklenemedi")
                }
        }
    }

    fun onTitleChanged(v: String) { _formState.value = _formState.value.copy(title = v) }
    fun onDescriptionChanged(v: String) { _formState.value = _formState.value.copy(description = v) }
    fun onPriceChanged(v: String) { _formState.value = _formState.value.copy(price = v) }
    fun onIsSwapChanged(v: Boolean) { _formState.value = _formState.value.copy(isSwap = v) }
    fun onConditionChanged(v: String) { _formState.value = _formState.value.copy(condition = v) }
    fun onCategoryChanged(v: String) { _formState.value = _formState.value.copy(category = v) }
    fun onLocationChanged(v: String) { _formState.value = _formState.value.copy(location = v) }

    fun resetError() { _uiState.value = EditAdUiState.Idle }

    fun submit() {
        val form = _formState.value
        if (!form.isValid) return
        _uiState.value = EditAdUiState.Loading
        viewModelScope.launch {
            val updates = buildMap<String, Any> {
                put("title", form.title)
                put("description", form.description)
                put("price", form.price.toDoubleOrNull() ?: 0.0)
                put("is_swap", form.isSwap)
                put("condition", form.condition)
                put("category", form.category)
                put("location", form.location)
            }
            adRepository.updateAd(adId, updates)
                .onSuccess { _uiState.value = EditAdUiState.Success }
                .onFailure { e -> _uiState.value = EditAdUiState.Error(e.message ?: "Güncelleme başarısız") }
        }
    }
}
