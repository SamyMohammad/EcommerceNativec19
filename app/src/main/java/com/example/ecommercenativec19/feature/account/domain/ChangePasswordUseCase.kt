package com.example.ecommercenativec19.feature.account.domain

import com.example.ecommercenativec19.feature.auth.domain.AuthValidation
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(private val repository: AccountRepository) {

    /**
     * The new-password rule is checked here because the server does not enforce it on this endpoint (it accepted a
     * 3-character password), so this is the only length guard. The current password only needs to be non-empty.
     */
    suspend operator fun invoke(currentPassword: String, newPassword: String): AccountOutcome<Unit> {
        val errors = buildMap {
            AuthValidation.existingPassword(currentPassword)?.let { put(AccountField.CurrentPassword, it) }
            AuthValidation.newPassword(newPassword)?.let { put(AccountField.NewPassword, it) }
        }
        if (errors.isNotEmpty()) return AccountOutcome.Invalid(errors)
        return when (val result = repository.changePassword(currentPassword, newPassword)) {
            is AccountResult.Success -> AccountOutcome.Success(Unit)
            is AccountResult.Failure -> AccountOutcome.Failed(result.error)
        }
    }
}
