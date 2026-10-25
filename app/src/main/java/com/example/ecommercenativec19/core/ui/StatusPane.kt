package com.example.ecommercenativec19.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.core.common.UiState

/**
 * Renders [content] only for [UiState.Success]; the other states get a spinner, a retry prompt or [emptyText].
 * The empty copy is a parameter because "nothing here" means something different on every screen.
 */
@Composable
fun <T> StatusPane(
    state: UiState<T>,
    onRetry: () -> Unit,
    emptyText: String,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit,
) {
    Box(modifier = modifier) {
        when (state) {
            is UiState.Success -> content(state.data)
            UiState.Loading -> CenteredStatus { CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) }
            is UiState.Error -> CenteredStatus {
                Text(
                    text = stringResource(state.error.toMessageRes()),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 12.dp),
                )
                AppButton(
                    text = stringResource(R.string.core_retry),
                    onClick = onRetry,
                    style = AppButtonStyle.PrimarySmall,
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
            }
            UiState.Empty -> CenteredStatus {
                Text(
                    text = emptyText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun CenteredStatus(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content,
    )
}
