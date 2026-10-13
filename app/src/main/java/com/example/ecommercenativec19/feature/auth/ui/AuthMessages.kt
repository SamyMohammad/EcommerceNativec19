package com.example.ecommercenativec19.feature.auth.ui

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.core.ui.toMessageRes
import com.example.ecommercenativec19.feature.auth.domain.AuthError
import com.example.ecommercenativec19.feature.auth.domain.AuthField
import com.example.ecommercenativec19.feature.auth.domain.FieldError

// Domain results carry no Android resources; this file is the one place that turns them into copy.

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

/** Submit failure text. A server validation message is shown as sent (the API only speaks English). */
@Composable
fun AuthError.message(): String = when (this) {
    AuthError.InvalidCredentials -> stringResource(R.string.auth_error_invalid_credentials)
    AuthError.EmailAlreadyUsed -> stringResource(R.string.auth_error_email_taken)
    is AuthError.Validation -> message
    is AuthError.Other -> stringResource(error.toMessageRes())
}
