package com.example.ecommercenativec19.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.ui.theme.extendedColors
import com.example.ecommercenativec19.ui.theme.extendedTypography

/**
 * Sign in / Sign up text field: label (Medium 18, white) above a 64dp white rounded field.
 * Designed for the primary-color auth background.
 *
 * Text direction follows the layout direction (start aligned); placeholder/typed text use the design's Light 18.
 * [trailingIcon] is an optional slot at the end of the field (e.g. [PasswordVisibilityToggle]); the slot is
 * given the design's 16dp end margin minus the touch inset, so pass a 48dp-target icon.
 */
@Composable
fun AuthTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    val shape = MaterialTheme.shapes.medium
    Column(modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            maxLines = 1,
            modifier = Modifier.textLineBox(MaterialTheme.typography.titleMedium),
        )
        Spacer(Modifier.height(AuthLabelToFieldGap))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(AppDimens.AuthFieldHeight)
                .clip(shape)
                .background(MaterialTheme.colorScheme.surface)
                .semantics { contentDescription = label },
            textStyle = MaterialTheme.extendedTypography.fieldInput.copy(color = MaterialTheme.extendedColors.inputText),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            singleLine = true,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = AppDimens.ScreenHorizontal,
                            end = if (trailingIcon != null) {
                                AppDimens.ScreenHorizontal - AppDimens.IconTouchInset
                            } else {
                                AppDimens.ScreenHorizontal
                            },
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = MaterialTheme.extendedTypography.fieldInput,
                                color = MaterialTheme.extendedColors.inputPlaceholder,
                                maxLines = 1,
                            )
                        }
                        innerTextField()
                    }
                    trailingIcon?.invoke()
                }
            },
        )
    }
}

/**
 * The eye icon at the end of the password fields (24dp icon, 48dp target).
 *
 * Only the "eye-slash" drawable was exported by the design, so both states show it; the state is exposed through
 * the content description. Combine with `VisualTransformation` in the caller.
 */
@Composable
fun PasswordVisibilityToggle(
    passwordVisible: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false, radius = AppDimens.MinTouchTarget / 2),
                role = Role.Button,
                onClick = onToggle,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_vuesax_outline_eye_slash_d9dea33b),
            contentDescription = stringResource(
                if (passwordVisible) R.string.core_password_hide else R.string.core_password_show,
            ),
            modifier = Modifier.size(AppDimens.IconSize),
        )
    }
}

/** Label bottom (y 351) to field top (y 375) in the sign in / sign up frames. */
private val AuthLabelToFieldGap = 24.dp
