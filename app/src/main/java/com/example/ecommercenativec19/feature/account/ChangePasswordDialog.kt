package com.example.ecommercenativec19.feature.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.feature.account.domain.AccountField
import com.example.ecommercenativec19.feature.auth.ui.message
import com.example.ecommercenativec19.feature.auth.ui.toMessageRes

/**
 * The inline field edit cannot collect the current password the API requires, so the password row opens this
 * dialog instead (a deviation from the design, which has only the pencil). The dialog cannot be dismissed while the
 * request is running.
 */
@Composable
fun ChangePasswordDialog(
    form: PasswordFormUi,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.account_password_dialog_title)) },
        text = {
            Column {
                PasswordInput(
                    label = stringResource(R.string.account_password_current),
                    value = form.currentPassword,
                    onValueChange = onCurrentPasswordChange,
                    error = form.errors[AccountField.CurrentPassword]?.let { stringResource(it.toMessageRes()) },
                    enabled = !form.isSaving,
                    imeAction = ImeAction.Next,
                    onDone = {},
                )
                PasswordInput(
                    label = stringResource(R.string.account_password_new),
                    value = form.newPassword,
                    onValueChange = onNewPasswordChange,
                    error = form.errors[AccountField.NewPassword]?.let { stringResource(it.toMessageRes()) },
                    enabled = !form.isSaving,
                    imeAction = ImeAction.Done,
                    onDone = onSubmit,
                    modifier = Modifier.padding(top = 12.dp),
                )
                form.submitError?.let {
                    Text(
                        text = it.message(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 12.dp),
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onSubmit, enabled = !form.isSaving) {
                Text(stringResource(R.string.account_password_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !form.isSaving) {
                Text(stringResource(R.string.account_password_cancel))
            }
        },
    )
}

@Composable
private fun PasswordInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    error: String?,
    enabled: Boolean,
    imeAction: ImeAction,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = error != null,
        supportingText = error?.let { { Text(it) } },
        enabled = enabled,
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = imeAction),
        keyboardActions = KeyboardActions(onDone = { onDone() }),
        modifier = modifier.fillMaxWidth(),
    )
}
