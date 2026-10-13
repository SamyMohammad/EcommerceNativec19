package com.example.ecommercenativec19.feature.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.core.ui.AppButton
import com.example.ecommercenativec19.core.ui.AppButtonStyle
import com.example.ecommercenativec19.ui.theme.generated.LocalAppTokens

/** Renders [content] only for [LoadStatus.Content]; the other statuses get a spinner, a retry prompt or an empty note. */
@Composable
internal fun StatusPane(
    status: LoadStatus,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val tokens = LocalAppTokens.current
    Box(modifier = modifier) {
        if (status == LoadStatus.Content) {
            content()
            return@Box
        }
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (status) {
                LoadStatus.Loading -> CircularProgressIndicator(color = tokens.colorSource00)
                LoadStatus.Error -> {
                    Text(
                        text = stringResource(R.string.categories_error),
                        style = tokens.typographySource11,
                        color = tokens.colorSource12,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 12.dp),
                    )
                    AppButton(
                        text = stringResource(R.string.categories_retry),
                        onClick = onRetry,
                        style = AppButtonStyle.PrimarySmall,
                        modifier = Modifier.padding(horizontal = 8.dp),
                    )
                }
                LoadStatus.Empty -> Text(
                    text = stringResource(R.string.categories_empty),
                    style = tokens.typographySource11,
                    color = tokens.colorSource12,
                    textAlign = TextAlign.Center,
                )
                LoadStatus.Content -> Unit
            }
        }
    }
}
