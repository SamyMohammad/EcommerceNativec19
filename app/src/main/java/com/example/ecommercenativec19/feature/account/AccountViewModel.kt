package com.example.ecommercenativec19.feature.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercenativec19.core.common.AppResult
import com.example.ecommercenativec19.core.common.UiState
import com.example.ecommercenativec19.feature.account.domain.AccountOutcome
import com.example.ecommercenativec19.feature.account.domain.ChangePasswordUseCase
import com.example.ecommercenativec19.feature.account.domain.GetProfileUseCase
import com.example.ecommercenativec19.feature.account.domain.UpdateProfileUseCase
import com.example.ecommercenativec19.feature.auth.domain.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Loads the profile and saves one inline edit at a time. The profile is held only here (no cache), so it disappears
 * with the screen; signing out also removes this screen (navigation resets on the signed-out session).
 */
@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getProfile: GetProfileUseCase,
    private val updateProfile: UpdateProfileUseCase,
    private val changePassword: ChangePasswordUseCase,
    private val signOut: SignOutUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(AccountUiState())
    val state: StateFlow<AccountUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun onRetry() {
        if (_state.value.profile is UiState.Error) load()
    }

    fun onStartEditing(type: AccountFieldType) {
        val s = _state.value
        val profile = (s.profile as? UiState.Success)?.data ?: return
        if (s.isSaving) return
        if (type.isPassword) {
            _state.update { it.copy(editing = null, draft = "", fieldError = null, saveError = null, passwordForm = PasswordFormUi()) }
        } else {
            _state.update {
                it.copy(editing = type, draft = profile.valueOf(type), fieldError = null, saveError = null)
            }
        }
    }

    // Typing clears the feedback about the previous attempt, so a message never outlives the input it describes.
    fun onValueChange(value: String) = _state.update { it.copy(draft = value, fieldError = null, saveError = null) }

    fun onFinishEditing() {
        val s = _state.value
        val type = s.editing ?: return
        val profile = (s.profile as? UiState.Success)?.data ?: return
        if (s.isSaving) return
        _state.update { it.copy(isSaving = true, fieldError = null, saveError = null) }
        viewModelScope.launch {
            val outcome = updateProfile(
                current = profile,
                name = if (type == AccountFieldType.FullName) s.draft else profile.name,
                email = if (type == AccountFieldType.Email) s.draft else profile.email,
                phone = if (type == AccountFieldType.Mobile) s.draft else profile.phone,
                address = if (type == AccountFieldType.Address) s.draft else profile.address,
            )
            _state.update {
                when (outcome) {
                    is AccountOutcome.Success ->
                        it.copy(profile = UiState.Success(outcome.data), editing = null, draft = "", isSaving = false)
                    is AccountOutcome.Invalid ->
                        it.copy(isSaving = false, fieldError = outcome.errors.values.firstOrNull())
                    is AccountOutcome.Failed -> it.copy(isSaving = false, saveError = outcome.error)
                }
            }
        }
    }

    fun onCurrentPasswordChange(value: String) = updateForm {
        it.copy(currentPassword = value, errors = emptyMap(), submitError = null)
    }

    fun onNewPasswordChange(value: String) = updateForm {
        it.copy(newPassword = value, errors = emptyMap(), submitError = null)
    }

    fun onDismissPasswordForm() = _state.update { if (it.passwordForm?.isSaving == true) it else it.copy(passwordForm = null) }

    fun onSubmitPassword() {
        val form = _state.value.passwordForm ?: return
        if (form.isSaving) return
        updateForm { it.copy(isSaving = true, errors = emptyMap(), submitError = null) }
        viewModelScope.launch {
            val outcome = changePassword(form.currentPassword, form.newPassword)
            _state.update {
                when (outcome) {
                    // The use case has already stored the new token; the dialog and both passwords can go.
                    is AccountOutcome.Success -> it.copy(passwordForm = null)
                    is AccountOutcome.Invalid ->
                        it.copy(passwordForm = it.passwordForm?.copy(isSaving = false, errors = outcome.errors))
                    is AccountOutcome.Failed ->
                        it.copy(passwordForm = it.passwordForm?.copy(isSaving = false, submitError = outcome.error))
                }
            }
        }
    }

    fun onSignOut() {
        viewModelScope.launch { signOut() }
    }

    private fun updateForm(transform: (PasswordFormUi) -> PasswordFormUi) =
        _state.update { s -> s.copy(passwordForm = s.passwordForm?.let(transform)) }

    private fun load() {
        _state.update { it.copy(profile = UiState.Loading) }
        viewModelScope.launch {
            val result = getProfile()
            _state.update {
                it.copy(
                    profile = when (result) {
                        is AppResult.Success -> UiState.Success(result.data)
                        is AppResult.Failure -> UiState.Error(result.error)
                    },
                )
            }
        }
    }
}
