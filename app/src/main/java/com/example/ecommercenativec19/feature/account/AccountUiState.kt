package com.example.ecommercenativec19.feature.account

import androidx.annotation.StringRes
import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.core.common.LoadState
import com.example.ecommercenativec19.feature.auth.FieldError

/** The five profile fields in design order. */
enum class AccountFieldType(@param:StringRes val label: Int, val isPassword: Boolean = false) {
    FullName(R.string.account_label_full_name),
    Email(R.string.account_label_email),
    Password(R.string.account_label_password, isPassword = true),
    Mobile(R.string.account_label_mobile),
    Address(R.string.account_label_address),
}

data class AccountFieldUi(val type: AccountFieldType, val value: String)

/** The profile has no password to show, so the row displays a fixed mask (22 characters, as in the design). */
internal const val PASSWORD_MASK = "**********************"

/**
 * [editing] is the field being edited inline (never the password) and [draft] its text; [fieldError] belongs to it.
 * [passwordForm] is non-null while the change-password dialog is open.
 */
data class AccountUiState(
    val profile: LoadState<Profile> = LoadState.Loading,
    val editing: AccountFieldType? = null,
    val draft: String = "",
    val fieldError: FieldError? = null,
    val passwordForm: PasswordFormUi? = null,
)

/** The two passwords live only here while the dialog is open. */
data class PasswordFormUi(
    val currentPassword: String = "",
    val newPassword: String = "",
) {
    // Keeps the passwords out of logs and assertion messages.
    override fun toString(): String = "PasswordFormUi(***)"
}

internal fun Profile.valueOf(type: AccountFieldType): String = when (type) {
    AccountFieldType.FullName -> name
    AccountFieldType.Email -> email
    AccountFieldType.Password -> PASSWORD_MASK
    AccountFieldType.Mobile -> phone
    AccountFieldType.Address -> address
}

/** Copy of this profile with [type] set to [value]; the password row has nothing to store. */
internal fun Profile.with(type: AccountFieldType, value: String): Profile = when (type) {
    AccountFieldType.FullName -> copy(name = value)
    AccountFieldType.Email -> copy(email = value)
    AccountFieldType.Mobile -> copy(phone = value)
    AccountFieldType.Address -> copy(address = value)
    AccountFieldType.Password -> this
}

/** The rows in design order, with the field being edited showing its draft instead of the saved value. */
internal fun AccountUiState.rows(profile: Profile): List<AccountFieldUi> =
    AccountFieldType.entries.map { type ->
        AccountFieldUi(type, if (type == editing) draft else profile.valueOf(type))
    }
