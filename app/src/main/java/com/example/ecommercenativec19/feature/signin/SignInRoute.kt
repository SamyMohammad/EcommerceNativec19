package com.example.ecommercenativec19.feature.signin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.ecommercenativec19.fake.LocalFakeStore
import com.example.ecommercenativec19.feature.auth.AuthField
import com.example.ecommercenativec19.feature.auth.AuthValidation

/**
 * Sign in entry point. Nothing is checked against an account: any input that passes the client-side form rules
 * signs in. [onSignedIn] fires right after; [onCreateAccount] leads to Sign up. "Forgot password" has no prototype
 * reaction and is rendered as plain text.
 */
@Composable
fun SignInRoute(
    onSignedIn: () -> Unit,
    onCreateAccount: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val store = LocalFakeStore.current
    var state by remember { mutableStateOf(SignInUiState()) }
    SignInScreen(
        state = state,
        // Editing a field clears its own error, so feedback never outlives the input it describes.
        onEmailChange = { state = state.copy(email = it, fieldErrors = state.fieldErrors - AuthField.Email) },
        onPasswordChange = { state = state.copy(password = it, fieldErrors = state.fieldErrors - AuthField.Password) },
        onTogglePasswordVisibility = { state = state.copy(passwordVisible = !state.passwordVisible) },
        onSignIn = {
            val errors = buildMap {
                AuthValidation.email(state.email)?.let { put(AuthField.Email, it) }
                AuthValidation.existingPassword(state.password)?.let { put(AuthField.Password, it) }
            }
            state = state.copy(fieldErrors = errors)
            if (errors.isEmpty()) {
                store.signIn()
                onSignedIn()
            }
        },
        onCreateAccount = onCreateAccount,
        modifier = modifier,
    )
}
