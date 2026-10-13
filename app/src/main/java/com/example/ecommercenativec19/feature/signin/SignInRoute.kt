package com.example.ecommercenativec19.feature.signin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

/**
 * Sign in entry point.
 *
 * Missing contract: the design has no authentication/backend contract, no validation rules and no session state.
 * [onSignIn] is therefore invoked as-is when "Login" (or the keyboard Done action) is used; the typed text is kept
 * only in [SignInViewModel]. "Forgot password" has no prototype reaction and is rendered as plain text.
 * [onCreateAccount] leads to Sign up.
 */
@Composable
fun SignInRoute(
    onSignIn: () -> Unit,
    onCreateAccount: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    SignInScreen(
        state = state,
        onUserNameChange = viewModel::onUserNameChange,
        onPasswordChange = viewModel::onPasswordChange,
        onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
        onSignIn = onSignIn,
        onCreateAccount = onCreateAccount,
        modifier = modifier,
    )
}
