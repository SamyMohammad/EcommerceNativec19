package com.example.ecommercenativec19.feature.account

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.ecommercenativec19.core.common.LoadState
import com.example.ecommercenativec19.fake.LocalFakeStore
import com.example.ecommercenativec19.feature.auth.FieldError

/**
 * Account entry point (Account tab selected). Inline edits write to the in-memory profile. The change-password
 * dialog only closes: there is no account to change it on. [onSignedOut] fires after the store is signed out and
 * returns the app to Sign in.
 */
@Composable
fun AccountRoute(onSignedOut: () -> Unit, modifier: Modifier = Modifier) {
    val store = LocalFakeStore.current
    var editing by remember { mutableStateOf<AccountFieldType?>(null) }
    var draft by remember { mutableStateOf("") }
    var fieldError by remember { mutableStateOf<FieldError?>(null) }
    var passwordForm by remember { mutableStateOf<PasswordFormUi?>(null) }
    val profile = store.profile
    AccountScreen(
        state = AccountUiState(
            profile = LoadState.Content(profile),
            editing = editing,
            draft = draft,
            fieldError = fieldError,
            passwordForm = passwordForm,
        ),
        onRetry = {},
        onStartEditing = { type ->
            fieldError = null
            if (type.isPassword) {
                editing = null
                draft = ""
                passwordForm = PasswordFormUi()
            } else {
                editing = type
                draft = profile.valueOf(type)
            }
        },
        onFinishEditing = {
            val type = editing
            if (type != null) {
                if (draft.isBlank()) {
                    fieldError = FieldError.Required
                } else {
                    store.updateProfile(profile.with(type, draft.trim()))
                    editing = null
                    draft = ""
                }
            }
        },
        // Typing clears the feedback about the previous attempt, so a message never outlives the input it describes.
        onValueChange = {
            draft = it
            fieldError = null
        },
        onCurrentPasswordChange = { passwordForm = passwordForm?.copy(currentPassword = it) },
        onNewPasswordChange = { passwordForm = passwordForm?.copy(newPassword = it) },
        onSubmitPassword = { passwordForm = null },
        onDismissPasswordForm = { passwordForm = null },
        onSignOut = {
            store.signOut()
            onSignedOut()
        },
        modifier = modifier,
    )
}
