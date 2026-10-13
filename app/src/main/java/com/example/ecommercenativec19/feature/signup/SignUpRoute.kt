package com.example.ecommercenativec19.feature.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

/**
 * Sign up entry point. The API signs the new user in, so [onSignedUp] fires once the token is stored
 * (see [SignUpUiState.signedUp]) and the app goes straight to Home.
 */
@Composable
fun SignUpRoute(
    onSignedUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val currentOnSignedUp by rememberUpdatedState(onSignedUp)
    LaunchedEffect(state.signedUp) {
        if (state.signedUp) currentOnSignedUp()
    }
    SignUpScreen(
        state = state,
        onFullNameChange = viewModel::onFullNameChange,
        onMobileNumberChange = viewModel::onMobileNumberChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
        onSignUp = viewModel::onSubmit,
        modifier = modifier,
    )
}
