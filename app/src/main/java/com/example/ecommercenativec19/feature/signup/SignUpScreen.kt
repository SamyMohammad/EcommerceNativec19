package com.example.ecommercenativec19.feature.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.core.ui.AppButton
import com.example.ecommercenativec19.core.ui.AppButtonStyle
import com.example.ecommercenativec19.core.ui.AppDimens
import com.example.ecommercenativec19.core.ui.AppLogo
import com.example.ecommercenativec19.core.ui.AppLogoStyle
import com.example.ecommercenativec19.core.ui.AuthTextField
import com.example.ecommercenativec19.core.ui.LightStatusBarIconsEffect
import com.example.ecommercenativec19.core.ui.PasswordVisibilityToggle

/**
 * Sign up (Figma node 9:97): primary-color full-bleed screen with the white logo, four labelled fields (full name,
 * mobile number, e-mail, password) and the white "Sign up" button.
 *
 * The frame uses absolute positions; here they become vertical gaps between the nodes. The column scrolls so it
 * survives large fonts and the keyboard.
 */
@Composable
fun SignUpScreen(
    state: SignUpUiState,
    onFullNameChange: (String) -> Unit,
    onMobileNumberChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onSignUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LightStatusBarIconsEffect()
    val statusBarTop = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .verticalScroll(rememberScrollState())
            .imePadding()
            .navigationBarsPadding()
            .padding(horizontal = AppDimens.ScreenHorizontal),
    ) {
        // Logo top at y = 85 of the frame, never closer to the status bar than the shared header offset.
        Spacer(Modifier.height((LogoTop - statusBarTop).coerceAtLeast(AppDimens.HeaderTopOffset) + statusBarTop))
        AppLogo(style = AppLogoStyle.Hero, modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(Modifier.height(LogoToFirstLabelGap))
        AuthTextField(
            label = stringResource(R.string.signup_full_name_label),
            value = state.fullName,
            onValueChange = onFullNameChange,
            placeholder = stringResource(R.string.signup_full_name_placeholder),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            ),
        )
        Spacer(Modifier.height(FieldGap))
        AuthTextField(
            label = stringResource(R.string.signup_mobile_label),
            value = state.mobileNumber,
            onValueChange = onMobileNumberChange,
            placeholder = stringResource(R.string.signup_mobile_placeholder),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
        )
        Spacer(Modifier.height(FieldGap))
        AuthTextField(
            label = stringResource(R.string.signup_email_label),
            value = state.email,
            onValueChange = onEmailChange,
            placeholder = stringResource(R.string.signup_email_placeholder),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
        )
        Spacer(Modifier.height(FieldGap))
        AuthTextField(
            label = stringResource(R.string.signup_password_label),
            value = state.password,
            onValueChange = onPasswordChange,
            placeholder = stringResource(R.string.signup_password_placeholder),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onSignUp() }),
            visualTransformation = if (state.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                PasswordVisibilityToggle(
                    passwordVisible = state.passwordVisible,
                    onToggle = onTogglePasswordVisibility,
                )
            },
        )
        Spacer(Modifier.height(PasswordToButtonGap))
        AppButton(
            text = stringResource(R.string.signup_submit),
            onClick = onSignUp,
            style = AppButtonStyle.OnPrimary,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(AppDimens.ScreenHorizontal))
    }
}

/** Frame y of the logo's top edge. */
private val LogoTop = 85.dp

/** Logo bottom 156.1 -> "Full Name" label top 203. */
private val LogoToFirstLabelGap = 47.dp

/** Field bottom (309 / 447 / 585) -> next label top (341 / 479 / 617). */
private val FieldGap = 32.dp

/** Password field bottom 723 -> "Sign up" button top 779. */
private val PasswordToButtonGap = 56.dp
