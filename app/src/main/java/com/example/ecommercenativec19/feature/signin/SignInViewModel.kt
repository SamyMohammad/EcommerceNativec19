package com.example.ecommercenativec19.feature.signin

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** Holds the typed field text of Sign in. No validation and no authentication (no contract exists yet). */
class SignInViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    fun onUserNameChange(value: String) = _uiState.update { it.copy(userName = value) }

    fun onPasswordChange(value: String) = _uiState.update { it.copy(password = value) }

    fun onTogglePasswordVisibility() = _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
}
