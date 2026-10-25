package com.example.ecommercenativec19.feature.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.ecommercenativec19.core.ui.AppButton
import com.example.ecommercenativec19.core.ui.AppButtonStyle
import com.example.ecommercenativec19.core.ui.AppDimens
import com.example.ecommercenativec19.core.ui.AppLogoBar
import com.example.ecommercenativec19.core.ui.StatusPane
import com.example.ecommercenativec19.core.ui.textLineBox
import com.example.ecommercenativec19.feature.account.domain.Profile
import com.example.ecommercenativec19.feature.auth.ui.message
import com.example.ecommercenativec19.feature.auth.ui.toMessageRes

/**
 * Stateless Account: logo, welcome header, the five profile fields, a sign-out button (not in the design, see
 * docs/design-decisions/account.md), bottom bar (Account tab selected). The profile region follows [com.example.ecommercenativec19.core.common.UiState].
 */
@Composable
fun AccountScreen(
    state: AccountUiState,
    onRetry: () -> Unit,
    onStartEditing: (AccountFieldType) -> Unit,
    onFinishEditing: () -> Unit,
    onValueChange: (String) -> Unit,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onSubmitPassword: () -> Unit,
    onDismissPasswordForm: () -> Unit,
    onSignOut: () -> Unit,
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
                .padding(padding),
        ) {
            AppLogoBar()
            StatusPane(
                state = state.profile,
                onRetry = onRetry,
                emptyText = "",
                modifier = Modifier.weight(1f),
            ) { profile ->
                ProfileContent(
                    profile = profile,
                    state = state,
                    onStartEditing = onStartEditing,
                    onFinishEditing = onFinishEditing,
                    onValueChange = onValueChange,
                    onSignOut = onSignOut,
                )
            }
        }
    }
    state.passwordForm?.let { form ->
        ChangePasswordDialog(
            form = form,
            onCurrentPasswordChange = onCurrentPasswordChange,
            onNewPasswordChange = onNewPasswordChange,
            onSubmit = onSubmitPassword,
            onDismiss = onDismissPasswordForm,
        )
    }
}

@Composable
private fun ProfileContent(
    profile: Profile,
    state: AccountUiState,
    onStartEditing: (AccountFieldType) -> Unit,
    onFinishEditing: () -> Unit,
    onValueChange: (String) -> Unit,
    onSignOut: () -> Unit,
) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        Spacer(Modifier.height(LogoToWelcomeGap))
        Column(
            modifier = Modifier.padding(horizontal = AppDimens.ScreenHorizontal),
            verticalArrangement = Arrangement.spacedBy(WelcomeToEmailGap),
        ) {
            Text(
                text = stringResource(R.string.account_welcome, profile.name.firstWord()),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.textLineBox(MaterialTheme.typography.titleMedium),
            )
            Text(
                text = profile.email,
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
            state.rows(profile).forEach { field ->
                val isEditing = state.editing == field.type
                AccountFieldRow(
                    field = field,
                    isEditing = isEditing,
                    onStartEditing = { onStartEditing(field.type) },
                    onFinishEditing = onFinishEditing,
                    onValueChange = onValueChange,
                    errorText = if (isEditing) state.fieldError?.let { stringResource(it.toMessageRes()) } else null,
                    enabled = !state.isSaving,
                )
            }
            state.saveError?.let { error ->
                Text(
                    text = error.message(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
            AppButton(
                text = stringResource(R.string.account_sign_out),
                onClick = onSignOut,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Spacer(Modifier.height(BottomSpacing))
    }
}

/** The design greets by first name ("Welcome, Jane"). */
private fun String.firstWord(): String = trim().substringBefore(' ')

/** Logo bottom (y 72) to the welcome text (y 96). */
private val LogoToWelcomeGap = 24.dp

/** Welcome line bottom (y 114) to the e-mail line (y 122). */
private val WelcomeToEmailGap = 8.dp

/** E-mail line bottom (y 140) to the first label (y 180). */
private val HeaderToFirstLabelGap = 40.dp

/** Field bottom (y 268) to the next label (y 292). */
private val FieldGroupGap = 24.dp

private val BottomSpacing = 24.dp
