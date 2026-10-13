package com.example.ecommercenativec19.feature.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Sign up entry point.
 *
 * Missing contract: the design has no registration/backend contract, no validation rules and no session state.
 * [onSignUp] is therefore invoked as-is when "Sign up" (or the keyboard Done action) is used; the typed text is
 * kept only in [SignUpViewModel].
 */
@Composable
fun SignUpRoute(
    onSignUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = viewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    SignUpScreen(
        state = state,
        onFullNameChange = viewModel::onFullNameChange,
        onMobileNumberChange = viewModel::onMobileNumberChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
        onSignUp = onSignUp,
        modifier = modifier,
    )
}
