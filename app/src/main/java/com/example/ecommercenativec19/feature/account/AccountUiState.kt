package com.example.ecommercenativec19.feature.account

import androidx.annotation.StringRes
import com.example.ecommercenativec19.R

/** The five profile fields in design order. */
enum class AccountFieldType(@param:StringRes val label: Int, val isPassword: Boolean = false) {
    FullName(R.string.account_label_full_name),
    Email(R.string.account_label_email),
    Password(R.string.account_label_password, isPassword = true),
    Mobile(R.string.account_label_mobile),
    Address(R.string.account_label_address),
}

data class AccountFieldUi(val type: AccountFieldType, val value: String)

/**
 * [welcomeName] and [headerEmail] are the header texts as given (placeholder data); they do not follow edits of
 * the fields. [editing] is the field currently being edited, if any.
 */
data class AccountUiState(
    val welcomeName: String = "",
    val headerEmail: String = "",
    val fields: List<AccountFieldUi> = emptyList(),
    val editing: AccountFieldType? = null,
)
