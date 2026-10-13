package com.example.ecommercenativec19.feature.auth.domain

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SignUpUseCaseTest {
    private val repository = FakeAuthRepository()
    private val signUp = SignUpUseCase(repository)

    @Test
    fun `invalid input reports every failing field and sends nothing`() = runTest {
        val outcome = signUp(name = "ab", phone = "123", email = "nope", password = "abc")

        assertEquals(
            AuthOutcome.Invalid(
                mapOf(
                    AuthField.Name to FieldError.NameTooShort,
                    AuthField.Phone to FieldError.InvalidPhone,
                    AuthField.Email to FieldError.InvalidEmail,
                    AuthField.Password to FieldError.PasswordTooShort,
                ),
            ),
            outcome,
        )
        assertEquals(0, repository.signUpCalls)
    }

    @Test
    fun `valid input is trimmed and sent`() = runTest {
        val outcome = signUp(name = " C19 Test ", phone = "01000000000", email = " user@example.com ", password = "abcdef")

        assertEquals(AuthOutcome.Success, outcome)
        assertEquals(SignUpData("C19 Test", "user@example.com", "01000000000", "abcdef"), repository.lastSignUp)
    }

    @Test
    fun `email already used is passed through`() = runTest {
        repository.result = AuthResult.Failure(AuthError.EmailAlreadyUsed)

        val outcome = signUp(name = "C19 Test", phone = "01000000000", email = "user@example.com", password = "abcdef")

        assertEquals(AuthOutcome.Failed(AuthError.EmailAlreadyUsed), outcome)
    }

    @Test
    fun `SignUpData toString hides the password`() {
        val text = SignUpData("C19 Test", "user@example.com", "01000000000", "hunter22").toString()

        assertEquals(false, text.contains("hunter22"))
    }
}
