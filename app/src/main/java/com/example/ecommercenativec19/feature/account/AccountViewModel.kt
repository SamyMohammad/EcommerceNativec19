package com.example.ecommercenativec19.feature.account

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Local UI state of the Account screen: which field is being edited and its text. Nothing is validated or
 * persisted (no profile contract exists).
 */
class AccountViewModel(
    initial: AccountUiState = AccountUiState(
        welcomeName = AccountSampleData.WELCOME_NAME,
        headerEmail = AccountSampleData.HEADER_EMAIL,
        fields = AccountSampleData.fields,
    ),
) : ViewModel() {
    private val _state = MutableStateFlow(initial)
    val state: StateFlow<AccountUiState> = _state.asStateFlow()

    fun onStartEditing(type: AccountFieldType) = _state.update { it.copy(editing = type) }

    fun onFinishEditing() = _state.update { it.copy(editing = null) }

    fun onValueChange(type: AccountFieldType, value: String) = _state.update { s ->
        s.copy(fields = s.fields.map { if (it.type == type) it.copy(value = value) else it })
    }
}
