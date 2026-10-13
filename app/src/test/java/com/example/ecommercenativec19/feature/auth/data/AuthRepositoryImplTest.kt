package com.example.ecommercenativec19.feature.auth.data

import com.example.ecommercenativec19.core.common.AppError
import com.example.ecommercenativec19.core.session.FakeSessionRepository
import com.example.ecommercenativec19.core.session.SessionState
import com.example.ecommercenativec19.feature.auth.domain.AuthError
import com.example.ecommercenativec19.feature.auth.domain.AuthResult
import com.example.ecommercenativec19.feature.auth.domain.SignUpData
import java.io.IOException
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response

class AuthRepositoryImplTest {

    private class FakeAuthApi : AuthApi {
        var response: () -> Response<AuthResponseDto> = { Response.success(AuthResponseDto("jwt-1")) }
        var lastSignIn: SignInRequest? = null
        var lastSignUp: SignUpRequest? = null

        override suspend fun signIn(request: SignInRequest): Response<AuthResponseDto> {
            lastSignIn = request
            return response()
        }

        override suspend fun signUp(request: SignUpRequest): Response<AuthResponseDto> {
            lastSignUp = request
            return response()
        }
    }

    private val api = FakeAuthApi()
    private val sessions = FakeSessionRepository()
    private val repository = AuthRepositoryImpl(api, sessions, Json { ignoreUnknownKeys = true })

    private fun errorResponse(code: Int, body: String): () -> Response<AuthResponseDto> = {
        Response.error(code, body.toResponseBody("application/json".toMediaType()))
    }

    @Test
    fun `sign in success stores the token`() = runTest {
        val result = repository.signIn("user@example.com", "secret1")

        assertEquals(AuthResult.Success, result)
        assertEquals(SessionState.SignedIn("jwt-1"), sessions.state.value)
        assertEquals(SignInRequest("user@example.com", "secret1"), api.lastSignIn)
    }

    @Test
    fun `sign up sends the password as rePassword too and stores the token`() = runTest {
        val result = repository.signUp(SignUpData("C19 Test", "user@example.com", "01000000000", "secret1"))

        assertEquals(AuthResult.Success, result)
        assertEquals(SessionState.SignedIn("jwt-1"), sessions.state.value)
        assertEquals(
            SignUpRequest("C19 Test", "user@example.com", "secret1", "secret1", "01000000000"),
            api.lastSignUp,
        )
    }

    @Test
    fun `401 maps to invalid credentials and stores nothing`() = runTest {
        api.response = errorResponse(401, """{"statusMsg":"fail","message":"Incorrect email or password"}""")

        val result = repository.signIn("user@example.com", "wrong")

        assertEquals(AuthResult.Failure(AuthError.InvalidCredentials), result)
        assertEquals(SessionState.SignedOut, sessions.state.value)
    }

    @Test
    fun `409 maps to email already used`() = runTest {
        api.response = errorResponse(409, """{"statusMsg":"fail","message":"Account Already Exists"}""")

        val result = repository.signUp(SignUpData("C19 Test", "user@example.com", "01000000000", "secret1"))

        assertEquals(AuthResult.Failure(AuthError.EmailAlreadyUsed), result)
    }

    @Test
    fun `400 carries the server validation message`() = runTest {
        api.response = errorResponse(
            400,
            """{"message":"fail","errors":{"value":"x","msg":"must be at least 6 chars","param":"password","location":"body"}}""",
        )

        val result = repository.signUp(SignUpData("C19 Test", "user@example.com", "01000000000", "secret1"))

        assertEquals(AuthResult.Failure(AuthError.Validation("must be at least 6 chars")), result)
    }

    @Test
    fun `400 with an unreadable body falls back to a server error`() = runTest {
        api.response = errorResponse(400, "<html>oops</html>")

        val result = repository.signIn("user@example.com", "secret1")

        assertEquals(AuthResult.Failure(AuthError.Other(AppError.Server(400))), result)
    }

    @Test
    fun `other status codes map to a server error`() = runTest {
        api.response = errorResponse(500, "{}")

        val result = repository.signIn("user@example.com", "secret1")

        assertEquals(AuthResult.Failure(AuthError.Other(AppError.Server(500))), result)
    }

    @Test
    fun `network failure maps to a network error`() = runTest {
        api.response = { throw IOException("offline") }

        val result = repository.signIn("user@example.com", "secret1")

        assertEquals(AuthResult.Failure(AuthError.Other(AppError.Network)), result)
    }

    @Test
    fun `success without a token is a parsing error`() = runTest {
        api.response = { Response.success(null) }

        val result = repository.signIn("user@example.com", "secret1")

        assertEquals(AuthResult.Failure(AuthError.Other(AppError.Parsing)), result)
        assertEquals(SessionState.SignedOut, sessions.state.value)
    }

    @Test
    fun `failing to store the token is reported as an unknown error`() = runTest {
        sessions.saveError = IllegalStateException("keystore unavailable")

        val result = repository.signIn("user@example.com", "secret1")

        assertEquals(AuthResult.Failure(AuthError.Other(AppError.Unknown)), result)
    }

    @Test
    fun `request and response DTOs do not print secrets`() {
        assertEquals(false, SignInRequest("user@example.com", "hunter22").toString().contains("hunter22"))
        assertEquals(false, SignUpRequest("n", "e@x.co", "hunter22", "hunter22", "010").toString().contains("hunter22"))
        assertEquals(false, AuthResponseDto("jwt-secret").toString().contains("jwt-secret"))
    }
}
