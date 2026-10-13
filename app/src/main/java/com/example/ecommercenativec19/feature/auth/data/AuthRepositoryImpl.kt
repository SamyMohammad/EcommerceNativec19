package com.example.ecommercenativec19.feature.auth.data

import com.example.ecommercenativec19.core.common.AppError
import com.example.ecommercenativec19.core.common.AppResult
import com.example.ecommercenativec19.core.network.apiErrorMessage
import com.example.ecommercenativec19.core.network.safeApiCall
import com.example.ecommercenativec19.core.session.SessionRepository
import com.example.ecommercenativec19.feature.auth.domain.AuthError
import com.example.ecommercenativec19.feature.auth.domain.AuthRepository
import com.example.ecommercenativec19.feature.auth.domain.AuthResult
import com.example.ecommercenativec19.feature.auth.domain.SignUpData
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.serialization.json.Json
import retrofit2.Response

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val sessions: SessionRepository,
    private val json: Json,
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): AuthResult =
        authenticate { api.signIn(SignInRequest(email = email, password = password)) }

    override suspend fun signUp(data: SignUpData): AuthResult = authenticate {
        api.signUp(
            SignUpRequest(
                name = data.name,
                email = data.email,
                password = data.password,
                // The design has no confirm-password field; the server only checks equality.
                rePassword = data.password,
                phone = data.phone,
            ),
        )
    }

    private suspend fun authenticate(call: suspend () -> Response<AuthResponseDto>): AuthResult =
        when (val result = safeApiCall { call() }) {
            is AppResult.Failure -> AuthResult.Failure(AuthError.Other(result.error))
            is AppResult.Success -> result.data.toAuthResult()
        }

    private suspend fun Response<AuthResponseDto>.toAuthResult(): AuthResult {
        if (!isSuccessful) return AuthResult.Failure(toAuthError())
        val token = body()?.token?.takeIf { it.isNotBlank() }
            ?: return AuthResult.Failure(AuthError.Other(AppError.Parsing))
        return try {
            sessions.save(token)
            AuthResult.Success
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            // Keystore or disk failure: the user is signed in on the server but we cannot keep the token.
            AuthResult.Failure(AuthError.Other(AppError.Unknown))
        }
    }

    private fun Response<*>.toAuthError(): AuthError = when (code()) {
        HTTP_BAD_REQUEST -> apiErrorMessage(json)?.let(AuthError::Validation) ?: AuthError.Other(AppError.Server(code()))
        HTTP_UNAUTHORIZED -> AuthError.InvalidCredentials
        HTTP_CONFLICT -> AuthError.EmailAlreadyUsed
        else -> AuthError.Other(AppError.Server(code()))
    }

    private companion object {
        const val HTTP_BAD_REQUEST = 400
        const val HTTP_UNAUTHORIZED = 401
        const val HTTP_CONFLICT = 409
    }
}
