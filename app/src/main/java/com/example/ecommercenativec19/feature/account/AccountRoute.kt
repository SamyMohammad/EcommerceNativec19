package com.example.ecommercenativec19.feature.account

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

/** Account entry point (Account tab selected). The prototype defines no edit/save behavior; edits stay local. */
@Composable
fun AccountRoute(modifier: Modifier = Modifier) {
    val viewModel: AccountViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    AccountScreen(
        state = state,
        onStartEditing = viewModel::onStartEditing,
        onFinishEditing = viewModel::onFinishEditing,
        onValueChange = viewModel::onValueChange,
        modifier = modifier,
    )
}
