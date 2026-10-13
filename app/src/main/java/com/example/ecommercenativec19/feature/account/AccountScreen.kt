package com.example.ecommercenativec19.feature.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.core.ui.AppDimens
import com.example.ecommercenativec19.core.ui.AppLogoBar
import com.example.ecommercenativec19.core.ui.textLineBox

/** Stateless Account: logo, welcome header, the five profile fields, bottom bar (Account tab selected). */
@Composable
fun AccountScreen(
    state: AccountUiState,
    onStartEditing: (AccountFieldType) -> Unit,
    onFinishEditing: () -> Unit,
    onValueChange: (AccountFieldType, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
        ) {
            AppLogoBar()
            Spacer(Modifier.height(LogoToWelcomeGap))
            Column(
                modifier = Modifier.padding(horizontal = AppDimens.ScreenHorizontal),
                verticalArrangement = Arrangement.spacedBy(WelcomeToEmailGap),
            ) {
                Text(
                    text = stringResource(R.string.account_welcome, state.welcomeName),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.textLineBox(MaterialTheme.typography.titleMedium),
                )
                Text(
                    text = state.headerEmail,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.textLineBox(MaterialTheme.typography.labelLarge),
                )
            }
            Spacer(Modifier.height(HeaderToFirstLabelGap))
            Column(
                modifier = Modifier.padding(horizontal = AppDimens.ScreenHorizontal),
                verticalArrangement = Arrangement.spacedBy(FieldGroupGap),
            ) {
                state.fields.forEach { field ->
                    AccountFieldRow(
                        field = field,
                        isEditing = state.editing == field.type,
                        onStartEditing = { onStartEditing(field.type) },
                        onFinishEditing = onFinishEditing,
                        onValueChange = { onValueChange(field.type, it) },
                    )
                }
            }
            Spacer(Modifier.height(BottomSpacing))
        }
    }
}

/** Logo bottom (y 72) to the welcome text (y 96). */
private val LogoToWelcomeGap = 24.dp

/** Welcome line bottom (y 114) to the e-mail line (y 122). */
private val WelcomeToEmailGap = 8.dp

/** E-mail line bottom (y 140) to the first label (y 180). */
private val HeaderToFirstLabelGap = 40.dp

/** Field bottom (y 268) to the next label (y 292). */
private val FieldGroupGap = 24.dp

private val BottomSpacing = 24.dp
