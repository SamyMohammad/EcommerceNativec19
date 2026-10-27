package com.example.ecommercenativec19.feature.account

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * Account entry point (Account tab selected). Signing out needs no callback here: AppNavDisplay reacts to the
 * session turning signed-out.
 */
@Composable
fun AccountRoute(modifier: Modifier = Modifier) {
    val viewModel: AccountViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    AccountScreen(
        state = state,
        onRetry = viewModel::onRetry,
        onStartEditing = viewModel::onStartEditing,
        onFinishEditing = viewModel::onFinishEditing,
        onValueChange = viewModel::onValueChange,
        onCurrentPasswordChange = viewModel::onCurrentPasswordChange,
        onNewPasswordChange = viewModel::onNewPasswordChange,
        onSubmitPassword = viewModel::onSubmitPassword,
        onDismissPasswordForm = viewModel::onDismissPasswordForm,
        onSignOut = viewModel::onSignOut,
        modifier = modifier,
    )
}
