package com.example.ecommercenativec19.feature.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.core.ui.AppButton
import com.example.ecommercenativec19.core.ui.AppButtonStyle
import com.example.ecommercenativec19.core.ui.AppDimens
import com.example.ecommercenativec19.core.ui.AppLogo
import com.example.ecommercenativec19.core.ui.AppLogoStyle
import com.example.ecommercenativec19.core.ui.AuthErrorText
import com.example.ecommercenativec19.core.ui.AuthTextField
import com.example.ecommercenativec19.core.ui.LightStatusBarIconsEffect
import com.example.ecommercenativec19.core.ui.PasswordVisibilityToggle
import com.example.ecommercenativec19.core.ui.textLineBox
import com.example.ecommercenativec19.feature.auth.domain.AuthField
import com.example.ecommercenativec19.feature.auth.ui.message
import com.example.ecommercenativec19.feature.auth.ui.messageFor
import com.example.ecommercenativec19.ui.theme.extendedTypography

/**
 * Sign in (Figma node 14:47): primary-color full-bleed screen with the white logo, title, two fields, a
 * "Forgot password" label, the white "Login" button and the "Create Account" link.
 *
 * The frame uses absolute positions; here they become vertical gaps between the nodes. The column scrolls so it
 * survives large fonts and the keyboard.
 */
@Composable
fun SignInScreen(
    state: SignInUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onSignIn: () -> Unit,
    onCreateAccount: () -> Unit,
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
        // Logo top at y = 91 of the frame, never closer to the status bar than the shared header offset.
        Spacer(Modifier.height((LogoTop - statusBarTop).coerceAtLeast(AppDimens.HeaderTopOffset) + statusBarTop))
        AppLogo(style = AppLogoStyle.Hero, modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(Modifier.height(LogoToTitleGap))
        Text(
            text = stringResource(R.string.signin_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.textLineBox(MaterialTheme.typography.headlineMedium).semantics { heading() },
        )
        Spacer(Modifier.height(TitleToSubtitleGap))
        Text(
            text = stringResource(R.string.signin_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.textLineBox(MaterialTheme.typography.bodyLarge),
        )
        Spacer(Modifier.height(SubtitleToFirstFieldGap))
        AuthTextField(
            label = stringResource(R.string.signin_email_label),
            value = state.email,
            onValueChange = onEmailChange,
            placeholder = stringResource(R.string.signin_email_placeholder),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            errorText = state.fieldErrors.messageFor(AuthField.Email),
        )
        Spacer(Modifier.height(FieldGap))
        AuthTextField(
            label = stringResource(R.string.signin_password_label),
            value = state.password,
            onValueChange = onPasswordChange,
            placeholder = stringResource(R.string.signin_password_placeholder),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onSignIn() }),
            errorText = state.fieldErrors.messageFor(AuthField.Password),
            visualTransformation = if (state.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                PasswordVisibilityToggle(
                    passwordVisible = state.passwordVisible,
                    onToggle = onTogglePasswordVisibility,
                )
            },
        )
        // No reaction in the prototype (missing contract): plain end-aligned text, not interactive.
        Spacer(Modifier.height(PasswordToForgotGap))
        Text(
            text = stringResource(R.string.signin_forgot_password),
            style = MaterialTheme.extendedTypography.inlineLink,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth().textLineBox(MaterialTheme.extendedTypography.inlineLink),
        )
        Spacer(Modifier.height(ForgotToButtonGap))
        state.submitError?.let {
            AuthErrorText(it.message())
            Spacer(Modifier.height(SubmitErrorGap))
        }
        AppButton(
            text = stringResource(R.string.signin_login),
            onClick = onSignIn,
            style = AppButtonStyle.OnPrimary,
            enabled = !state.isSubmitting,
            // The design places the Login button at x = 17 (one px right of the 16 margin), same 398 width.
            modifier = Modifier.fillMaxWidth().offset(x = LoginButtonOffsetX),
        )
        // The link is padded to a 48dp touch target; the gap is reduced by that padding so the text stays at y = 763.
        Spacer(Modifier.height(ButtonToLinkGap - LinkTouchPadding))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = AppDimens.MinTouchTarget)
                .clickable(role = Role.Button, onClick = onCreateAccount),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.signin_create_account),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
            )
        }
        Spacer(Modifier.height(AppDimens.ScreenHorizontal))
    }
}

/** The Login button's frame x (17) minus the 16 screen margin. */
private val LoginButtonOffsetX = 1.dp

/** Frame y of the logo's top edge. */
private val LogoTop = 91.dp

/** Logo bottom 162.1 -> title top 249. */
private val LogoToTitleGap = 87.dp

/** Title top 249 -> subtitle top 275, minus the 18dp title line. */
private val TitleToSubtitleGap = 8.dp

/** Subtitle bottom 293 -> "User Name" label top 333. */
private val SubtitleToFirstFieldGap = 40.dp

/** Field bottom (439) -> next label top (471). */
private val FieldGap = 32.dp

/** Password field bottom 577 -> "Forgot password" top 593. */
private val PasswordToForgotGap = 16.dp

/** "Forgot password" bottom 611 -> Login top 667. */
private val ForgotToButtonGap = 56.dp

/** Space between the submit error and the Login button. */
private val SubmitErrorGap = 12.dp

/** Login bottom 731 -> link text top 763. */
private val ButtonToLinkGap = 32.dp

/** (48dp target - 18dp text line) / 2: space the touch target adds above and below the link text. */
private val LinkTouchPadding: Dp = (AppDimens.MinTouchTarget - 18.dp) / 2
