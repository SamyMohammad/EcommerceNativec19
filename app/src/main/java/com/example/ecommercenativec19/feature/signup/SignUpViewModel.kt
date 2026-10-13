package com.example.ecommercenativec19.feature.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercenativec19.feature.auth.domain.AuthField
import com.example.ecommercenativec19.feature.auth.domain.AuthOutcome
import com.example.ecommercenativec19.feature.auth.domain.SignUpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(private val signUp: SignUpUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    // Editing a field clears its own error and the stale submit error, so feedback never outlives the input it describes.
    fun onFullNameChange(value: String) =
        _uiState.update { it.copy(fullName = value, fieldErrors = it.fieldErrors - AuthField.Name, submitError = null) }

    fun onMobileNumberChange(value: String) =
        _uiState.update { it.copy(mobileNumber = value, fieldErrors = it.fieldErrors - AuthField.Phone, submitError = null) }

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
            val outcome = signUp(
                name = current.fullName,
                phone = current.mobileNumber,
                email = current.email,
                password = current.password,
            )
            _uiState.update {
                when (outcome) {
                    AuthOutcome.Success -> it.copy(isSubmitting = false, signedUp = true)
                    is AuthOutcome.Invalid -> it.copy(isSubmitting = false, fieldErrors = outcome.errors)
                    is AuthOutcome.Failed -> it.copy(isSubmitting = false, submitError = outcome.error)
                }
            }
        }
    }
}
