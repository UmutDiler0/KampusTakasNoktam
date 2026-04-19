package com.takasr.kampstakasnoktam.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<T : UiData>(initialData: T) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<T>> = MutableStateFlow(UiState.Success(initialData))
    val uiState: StateFlow<UiState<T>> = _uiState.asStateFlow()

    protected fun setIdle() {
        _uiState.value = UiState.Idle
    }

    protected fun setLoading() {
        _uiState.value = UiState.Loading
    }

    protected fun setError(message: String) {
        _uiState.value = UiState.Error(message)
    }

    protected fun setSuccess(data: T) {
        _uiState.value = UiState.Success(data)
    }

    protected fun updateData(transform: (T) -> T) {
        val currentData = (_uiState.value as? UiState.Success<T>)?.data ?: return
        _uiState.value = UiState.Success(transform(currentData))
    }
}
