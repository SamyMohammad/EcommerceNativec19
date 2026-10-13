package com.example.ecommercenativec19.feature.account

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.core.ui.AppDimens
import com.example.ecommercenativec19.core.ui.AppIconButton
import com.example.ecommercenativec19.core.ui.textLineBox
import com.example.ecommercenativec19.ui.theme.generated.LocalAppTokens

/**
 * Label (Medium 18) above a 54dp bordered field (radius 15) showing the value (Medium 14) and the edit icon at
 * the end. While [isEditing] the value is a text field (focused), the edit icon finishes editing. The password
 * is masked with `*` like the design. The field grows with font scale (min height 54dp).
 */
@Composable
fun AccountFieldRow(
    field: AccountFieldUi,
    isEditing: Boolean,
    onStartEditing: () -> Unit,
    onFinishEditing: () -> Unit,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tokens = LocalAppTokens.current
    val label = stringResource(field.type.label)
    val shape = RoundedCornerShape(tokens.radiusSource02)
    Column(modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = tokens.typographySource00,
            color = tokens.colorSource12,
            modifier = Modifier.textLineBox(tokens.typographySource00),
        )
        Spacer(Modifier.height(LabelToFieldGap))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = FieldMinHeight)
                .border(1.dp, tokens.colorSource14, shape)
                .padding(
                    start = AppDimens.ScreenHorizontal,
                    end = AppDimens.ScreenHorizontal - AppDimens.IconTouchInset,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val visualTransformation =
                if (field.type.isPassword) PasswordVisualTransformation('*') else VisualTransformation.None
            Box(Modifier.weight(1f)) {
                if (isEditing) {
                    val focusRequester = remember { FocusRequester() }
                    LaunchedEffect(Unit) { focusRequester.requestFocus() }
                    BasicTextField(
                        value = field.value,
                        onValueChange = onValueChange,
                        modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                        textStyle = tokens.typographySource15.copy(color = tokens.colorSource12),
                        cursorBrush = SolidColor(tokens.colorSource00),
                        singleLine = true,
                        visualTransformation = visualTransformation,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { onFinishEditing() }),
                    )
                } else {
                    Text(
                        text = if (field.type.isPassword) "*".repeat(field.value.length) else field.value,
                        style = tokens.typographySource15,
                        color = tokens.colorSource12,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.textLineBox(tokens.typographySource15),
                    )
                }
            }
            AppIconButton(
                painter = painterResource(R.drawable.ic_edit11_796bff72),
                contentDescription = stringResource(
                    if (isEditing) R.string.account_edit_done else R.string.account_edit_field,
                    label,
                ),
                onClick = if (isEditing) onFinishEditing else onStartEditing,
            )
        }
    }
}

/** Label line bottom (y 198) to the field top (y 214). */
private val LabelToFieldGap = 16.dp
private val FieldMinHeight = 54.dp
