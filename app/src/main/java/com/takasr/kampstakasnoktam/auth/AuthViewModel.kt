package com.takasr.kampstakasnoktam.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.takasr.kampstakasnoktam.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    object Success : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _uiState.value = AuthUiState.Error("E-posta ve şifre boş bırakılamaz")
            return
        }
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            authRepository.login(email, pass).onSuccess {
                _uiState.value = AuthUiState.Success
            }.onFailure { error ->
                _uiState.value = AuthUiState.Error(error.message ?: "Giriş başarısız")
            }
        }
    }

    fun register(fullName: String, email: String, pass: String) {
        if (fullName.isBlank() || email.isBlank() || pass.isBlank()) {
            _uiState.value = AuthUiState.Error("Tüm alanları doldurunuz")
            return
        }
        if (!email.endsWith("edu.tr")) {
            _uiState.value = AuthUiState.Error("Lütfen .edu.tr uzantılı bir e-posta kullanın")
            return
        }
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            authRepository.register(fullName, email, pass).onSuccess {
                // Kayıt sonrası otomatik giriş yapabiliriz veya login ekranına atabiliriz.
                // Burada basitlik için login'e yönlendireceğiz.
                _uiState.value = AuthUiState.Success
            }.onFailure { error ->
                _uiState.value = AuthUiState.Error(error.message ?: "Kayıt başarısız")
            }
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}
