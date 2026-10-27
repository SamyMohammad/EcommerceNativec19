package com.example.ecommercenativec19.feature.signin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * Sign in entry point. [onSignedIn] fires once the token is stored (see [SignInUiState.signedIn]);
 * [onCreateAccount] leads to Sign up. "Forgot password" has no prototype reaction and is rendered as plain text.
 */
@Composable
fun SignInRoute(
    onSignedIn: () -> Unit,
    onCreateAccount: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val currentOnSignedIn by rememberUpdatedState(onSignedIn)
    LaunchedEffect(state.signedIn) {
        if (state.signedIn) currentOnSignedIn()
    }
    SignInScreen(
        state = state,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
        onSignIn = viewModel::onSubmit,
        onCreateAccount = onCreateAccount,
        modifier = modifier,
    )
}
