package com.example.ecommercenativec19.feature.account.domain

import com.example.ecommercenativec19.feature.auth.domain.AuthError
import com.example.ecommercenativec19.feature.auth.domain.FieldError
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ChangePasswordUseCaseTest {
    private val repository = FakeAccountRepository()
    private val useCase = ChangePasswordUseCase(repository)

    @Test
    fun `valid passwords reach the repository`() = runTest {
        val outcome = useCase("old-pass", "new-pass")

        assertEquals(AccountOutcome.Success(Unit), outcome)
        assertEquals("old-pass" to "new-pass", repository.lastPasswords)
    }

    @Test
    fun `empty current password is rejected without a request`() = runTest {
        val outcome = useCase("", "new-pass")

        assertEquals(AccountOutcome.Invalid(mapOf(AccountField.CurrentPassword to FieldError.Required)), outcome)
        assertEquals(0, repository.passwordCalls)
    }

    @Test
    fun `new password shorter than 6 is rejected because the server would accept it`() = runTest {
        val outcome = useCase("old-pass", "abc")

        assertEquals(AccountOutcome.Invalid(mapOf(AccountField.NewPassword to FieldError.PasswordTooShort)), outcome)
        assertEquals(0, repository.passwordCalls)
    }

    @Test
    fun `server rejection such as a wrong current password is passed on`() = runTest {
        val error = AuthError.Validation("Incorrect current password")
        repository.passwordResult = AccountResult.Failure(error)

        assertEquals(AccountOutcome.Failed(error), useCase("wrong-pass", "new-pass"))
    }
}
