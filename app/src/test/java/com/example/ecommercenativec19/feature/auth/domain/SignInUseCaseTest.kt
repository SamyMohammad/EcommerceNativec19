package com.example.ecommercenativec19.feature.auth.domain

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SignInUseCaseTest {
    private val repository = FakeAuthRepository()
    private val signIn = SignInUseCase(repository)

    @Test
    fun `invalid input reports every failing field and sends nothing`() = runTest {
        val outcome = signIn("nope", "")

        assertEquals(
            AuthOutcome.Invalid(mapOf(AuthField.Email to FieldError.InvalidEmail, AuthField.Password to FieldError.Required)),
            outcome,
        )
        assertEquals(0, repository.signInCalls)
    }

    @Test
    fun `valid input is trimmed and sent, password untouched`() = runTest {
        val outcome = signIn("  user@example.com ", " pw ")

        assertEquals(AuthOutcome.Success, outcome)
        assertEquals("user@example.com" to " pw ", repository.lastSignIn)
    }

    @Test
    fun `repository failure is passed through`() = runTest {
        repository.result = AuthResult.Failure(AuthError.InvalidCredentials)

        assertEquals(AuthOutcome.Failed(AuthError.InvalidCredentials), signIn("user@example.com", "secret1"))
    }
}
