package com.example.ecommercenativec19.feature.account.domain

import com.example.ecommercenativec19.feature.auth.domain.AuthValidation
import com.example.ecommercenativec19.feature.auth.domain.FieldError

class UpdateProfileUseCase(private val repository: AccountRepository) {

    /**
     * Compares each edited value with [current] and validates and sends only what changed:
     * - the server rejects an unchanged e-mail as "already in use";
     * - an older stored value (a phone in the +20 form, which the server accepts) must not block editing another field.
     * Nothing is sent when nothing changed.
     */
    suspend operator fun invoke(
        current: Profile,
        name: String,
        email: String,
        phone: String,
        address: String,
    ): AccountOutcome<Profile> {
        val changes = ProfileChanges(
            name = name.trim().takeIf { it != current.name },
            email = email.trim().takeIf { it != current.email },
            phone = phone.trim().takeIf { it != current.phone },
            address = address.trim().takeIf { it != current.address },
        )
        val errors = buildMap<AccountField, FieldError> {
            changes.name?.let(AuthValidation::name)?.let { put(AccountField.Name, it) }
            changes.email?.let(AuthValidation::email)?.let { put(AccountField.Email, it) }
            changes.phone?.let(AuthValidation::phone)?.let { put(AccountField.Phone, it) }
            // The API offers no way to clear an address through this flow, so a blank one is an input error.
            if (changes.address != null && changes.address.isBlank()) put(AccountField.Address, FieldError.Required)
        }
        if (errors.isNotEmpty()) return AccountOutcome.Invalid(errors)
        if (changes.isEmpty) return AccountOutcome.Success(current)
        return when (val result = repository.updateProfile(current, changes)) {
            is AccountResult.Success -> AccountOutcome.Success(result.data)
            is AccountResult.Failure -> AccountOutcome.Failed(result.error)
        }
    }
}
