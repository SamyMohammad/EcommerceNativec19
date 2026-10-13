package com.example.ecommercenativec19.feature.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercenativec19.feature.auth.domain.AuthField
import com.example.ecommercenativec19.feature.auth.domain.AuthOutcome
import com.example.ecommercenativec19.feature.auth.domain.SignInUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel(private val signIn: SignInUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    // Editing a field clears its own error and the stale submit error, so feedback never outlives the input it describes.
    fun onEmailChange(value: String) =
        _uiState.update { it.copy(email = value, fieldErrors = it.fieldErrors - AuthField.Email, submitError = null) }

    fun onPasswordChange(value: String) =
        _uiState.update { it.copy(password = value, fieldErrors = it.fieldErrors - AuthField.Password, submitError = null) }

    fun onTogglePasswordVisibility() = _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }

    fun onSubmit() {
        val current = _uiState.value
        if (current.isSubmitting) return
        _uiState.update { it.copy(isSubmitting = true, fieldErrors = emptyMap(), submitError = null) }
        viewModelScope.launch {
            val outcome = signIn(current.email, current.password)
            _uiState.update {
                when (outcome) {
                    AuthOutcome.Success -> it.copy(isSubmitting = false, signedIn = true)
                    is AuthOutcome.Invalid -> it.copy(isSubmitting = false, fieldErrors = outcome.errors)
                    is AuthOutcome.Failed -> it.copy(isSubmitting = false, submitError = outcome.error)
                }
            }
        }
    }
}
