package com.example.ecommercenativec19.feature.account.domain

import com.example.ecommercenativec19.core.common.AppResult
import javax.inject.Inject

/**
 * Fetched on demand, not observed: the profile is not cached anywhere, so it cannot outlive the signed-in user, and
 * the Account screen is the only reader.
 */
class GetProfileUseCase @Inject constructor(private val repository: AccountRepository) {
    suspend operator fun invoke(): AppResult<Profile> = repository.getProfile()
}
