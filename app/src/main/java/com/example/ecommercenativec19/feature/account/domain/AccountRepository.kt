package com.example.ecommercenativec19.feature.account.domain

import com.example.ecommercenativec19.core.common.AppResult

interface AccountRepository {
    suspend fun getProfile(): AppResult<Profile>

    /** Sends only [changes]; returns [current] with them applied (the update endpoint does not echo the phone). */
    suspend fun updateProfile(current: Profile, changes: ProfileChanges): AccountResult<Profile>

    /** On success the new session token has already been stored; the old one is dead server-side. */
    suspend fun changePassword(currentPassword: String, newPassword: String): AccountResult<Unit>
}
