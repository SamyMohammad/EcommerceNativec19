package com.example.ecommercenativec19.feature.account.domain

/**
 * What the Account screen shows. [phone] and [address] are empty strings when the account has none. [address] is
 * one line: the design has a single address field, while the API keeps a list (see docs/api/account.md).
 */
data class Profile(
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
)

/** Fields the Account screen can reject. The two password fields belong to the change-password form. */
enum class AccountField { Name, Email, Phone, Address, CurrentPassword, NewPassword }

/** Only the fields that actually changed; null means "leave as is". */
data class ProfileChanges(
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val address: String? = null,
) {
    val isEmpty: Boolean get() = name == null && email == null && phone == null && address == null
}
