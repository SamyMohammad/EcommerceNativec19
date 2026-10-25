package com.example.ecommercenativec19.feature.account.data

import com.example.ecommercenativec19.core.common.AppError
import com.example.ecommercenativec19.core.common.AppResult
import com.example.ecommercenativec19.core.network.apiErrorMessage
import com.example.ecommercenativec19.core.network.safeApiCall
import com.example.ecommercenativec19.core.session.SessionRepository
import com.example.ecommercenativec19.feature.account.domain.AccountRepository
import com.example.ecommercenativec19.feature.account.domain.AccountResult
import com.example.ecommercenativec19.feature.account.domain.Profile
import com.example.ecommercenativec19.feature.account.domain.ProfileChanges
import com.example.ecommercenativec19.feature.auth.domain.AuthError
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import retrofit2.Response

class AccountRepositoryImpl @Inject constructor(
    private val api: AccountApi,
    private val sessions: SessionRepository,
    private val json: Json,
) : AccountRepository {

    override suspend fun getProfile(): AppResult<Profile> = when (val result = safeApiCall { api.getMe().data }) {
        is AppResult.Failure -> result
        is AppResult.Success -> AppResult.Success(result.data.toProfile())
    }

    override suspend fun updateProfile(current: Profile, changes: ProfileChanges): AccountResult<Profile> {
        if (changes.name != null || changes.email != null || changes.phone != null) {
            val request = UpdateMeRequest(name = changes.name, email = changes.email, phone = changes.phone)
            val result = write { api.updateMe(request) }
            if (result is AccountResult.Failure) return result
        }
        val phone = changes.phone ?: current.phone
        if (changes.address != null) {
            val failure = replaceAddress(changes.address, phone)
            if (failure != null) return failure
        }
        return AccountResult.Success(
            Profile(
                name = changes.name ?: current.name,
                email = changes.email ?: current.email,
                phone = phone,
                address = changes.address ?: current.address,
            ),
        )
    }

    override suspend fun changePassword(currentPassword: String, newPassword: String): AccountResult<Unit> {
        // The design has no confirm-password field; the server only checks equality (same as sign-up).
        val request = ChangePasswordRequest(currentPassword, newPassword, rePassword = newPassword)
        // NonCancellable: once the server rotated the token, leaving the screen must not drop the new one on the floor.
        return withContext(NonCancellable) {
            val body = when (val result = write { api.changePassword(request) }) {
                is AccountResult.Failure -> return@withContext result
                is AccountResult.Success -> result.data
            }
            val token = body?.token?.takeIf { it.isNotBlank() }
                ?: return@withContext AccountResult.Failure(AuthError.Other(AppError.Parsing))
            try {
                // The old token is already dead server-side, so the new one must replace it before anything else runs.
                sessions.save(token)
                AccountResult.Success(Unit)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // The password changed but the new token could not be kept: the next request will sign the user out.
                AccountResult.Failure(AuthError.Other(AppError.Unknown))
            }
        }
    }

    /**
     * Adds the new address, then removes the previous ones: the design shows a single address while the API keeps a
     * list. Not atomic, and a failed removal is ignored on purpose: the new address is last in the list, which is the
     * one [toProfile] shows, so a leftover entry is harmless.
     */
    private suspend fun replaceAddress(details: String, phone: String): AccountResult.Failure? {
        val request = AddAddressRequest(name = ADDRESS_NAME, details = details, phone = phone.ifBlank { null })
        val all = when (val result = safeApiCall { api.addAddress(request).data }) {
            is AppResult.Failure -> return AccountResult.Failure(AuthError.Other(result.error))
            is AppResult.Success -> result.data
        }
        all.dropLast(1).forEach { old -> safeApiCall { api.removeAddress(old.id) } }
        return null
    }

    private suspend fun <T> write(call: suspend () -> Response<T>): AccountResult<T?> =
        when (val result = safeApiCall { call() }) {
            is AppResult.Failure -> AccountResult.Failure(AuthError.Other(result.error))
            is AppResult.Success ->
                if (result.data.isSuccessful) AccountResult.Success(result.data.body())
                else AccountResult.Failure(result.data.toAuthError())
        }

    /** The API reports a taken e-mail and a wrong current password as plain 400 validation errors. */
    private fun Response<*>.toAuthError(): AuthError =
        if (code() == HTTP_BAD_REQUEST) {
            apiErrorMessage(json)?.let(AuthError::Validation) ?: AuthError.Other(AppError.Server(code()))
        } else {
            AuthError.Other(AppError.Server(code()))
        }

    private fun UserDto.toProfile() = Profile(
        name = name,
        email = email,
        phone = phone.orEmpty(),
        address = addresses.lastOrNull()?.details.orEmpty(),
    )

    private companion object {
        const val HTTP_BAD_REQUEST = 400
        const val ADDRESS_NAME = "Home"
    }
}
