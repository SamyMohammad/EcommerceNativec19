package com.example.ecommercenativec19.feature.auth.ui

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.feature.auth.AuthField
import com.example.ecommercenativec19.feature.auth.FieldError

@StringRes
fun FieldError.toMessageRes(): Int = when (this) {
    FieldError.Required -> R.string.auth_error_required
    FieldError.InvalidEmail -> R.string.auth_error_invalid_email
    FieldError.NameTooShort -> R.string.auth_error_name_too_short
    FieldError.InvalidPhone -> R.string.auth_error_invalid_phone
    FieldError.PasswordTooShort -> R.string.auth_error_password_too_short
}

/** Message under [field], or null when the field has no error. */
@Composable
fun Map<AuthField, FieldError>.messageFor(field: AuthField): String? =
    get(field)?.let { stringResource(it.toMessageRes()) }
