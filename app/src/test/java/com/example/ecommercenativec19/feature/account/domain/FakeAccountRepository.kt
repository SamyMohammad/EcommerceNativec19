package com.example.ecommercenativec19.feature.account.domain

import com.example.ecommercenativec19.core.common.AppResult
import kotlinx.coroutines.CompletableDeferred

/** Records calls and answers with the configured results; set [gate] to hold an update/password call suspended. */
class FakeAccountRepository(
    var profileResult: AppResult<Profile> = AppResult.Success(AccountFixtures.profile),
    var updateResult: AccountResult<Profile>? = null,
    var passwordResult: AccountResult<Unit> = AccountResult.Success(Unit),
) : AccountRepository {
    var gate: CompletableDeferred<Unit>? = null
    var getProfileCalls = 0
    var updateCalls = 0
    var lastChanges: ProfileChanges? = null
    var passwordCalls = 0
    var lastPasswords: Pair<String, String>? = null

    override suspend fun getProfile(): AppResult<Profile> {
        getProfileCalls++
        return profileResult
    }

    override suspend fun updateProfile(current: Profile, changes: ProfileChanges): AccountResult<Profile> {
        updateCalls++
        lastChanges = changes
        gate?.await()
        return updateResult ?: AccountResult.Success(
            current.copy(
                name = changes.name ?: current.name,
                email = changes.email ?: current.email,
                phone = changes.phone ?: current.phone,
                address = changes.address ?: current.address,
            ),
        )
    }

    override suspend fun changePassword(currentPassword: String, newPassword: String): AccountResult<Unit> {
        passwordCalls++
        lastPasswords = currentPassword to newPassword
        gate?.await()
        return passwordResult
    }
}

object AccountFixtures {
    /** Placeholder data, the same as the Account screenshot. */
    val profile = Profile(
        name = "Jane Doe",
        email = "jane.doe@example.com",
        phone = "01000000000",
        address = "123 Example Street, Cairo",
    )
}
