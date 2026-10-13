package com.example.ecommercenativec19.feature.account.domain

import com.example.ecommercenativec19.core.common.AppError
import com.example.ecommercenativec19.feature.auth.domain.AuthError
import com.example.ecommercenativec19.feature.auth.domain.FieldError
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class UpdateProfileUseCaseTest {
    private val repository = FakeAccountRepository()
    private val useCase = UpdateProfileUseCase(repository)
    private val current = AccountFixtures.profile

    private suspend fun update(
        name: String = current.name,
        email: String = current.email,
        phone: String = current.phone,
        address: String = current.address,
        from: Profile = current,
    ) = useCase(from, name, email, phone, address)

    @Test
    fun `nothing changed sends nothing and succeeds`() = runTest {
        val outcome = update()

        assertEquals(AccountOutcome.Success(current), outcome)
        assertEquals(0, repository.updateCalls)
    }

    @Test
    fun `only the changed field is sent, trimmed`() = runTest {
        val outcome = update(name = "  Janet Doe ")

        assertEquals(ProfileChanges(name = "Janet Doe"), repository.lastChanges)
        assertEquals(AccountOutcome.Success(current.copy(name = "Janet Doe")), outcome)
    }

    @Test
    fun `an unchanged e-mail is never sent`() = runTest {
        update(phone = "01112345678")

        assertNull(repository.lastChanges?.email)
    }

    @Test
    fun `short name is rejected without a request`() = runTest {
        val outcome = update(name = "Jo")

        assertEquals(AccountOutcome.Invalid(mapOf(AccountField.Name to FieldError.NameTooShort)), outcome)
        assertEquals(0, repository.updateCalls)
    }

    @Test
    fun `invalid e-mail and phone are rejected`() = runTest {
        val outcome = update(email = "nope", phone = "123")

        assertEquals(
            AccountOutcome.Invalid(
                mapOf(AccountField.Email to FieldError.InvalidEmail, AccountField.Phone to FieldError.InvalidPhone),
            ),
            outcome,
        )
    }

    @Test
    fun `blank address is rejected`() = runTest {
        val outcome = update(address = "  ")

        assertEquals(AccountOutcome.Invalid(mapOf(AccountField.Address to FieldError.Required)), outcome)
    }

    @Test
    fun `a stored value that breaks today's rules does not block editing another field`() = runTest {
        val legacy = current.copy(phone = "+201000000000")

        val outcome = update(from = legacy, phone = legacy.phone, name = "Janet Doe")

        assertEquals(ProfileChanges(name = "Janet Doe"), repository.lastChanges)
        assertEquals(AccountOutcome.Success(legacy.copy(name = "Janet Doe")), outcome)
    }

    @Test
    fun `adding a first address is accepted`() = runTest {
        val noAddress = current.copy(address = "")

        val outcome = update(from = noAddress, address = "1 Example Road")

        assertEquals(ProfileChanges(address = "1 Example Road"), repository.lastChanges)
        assertEquals(AccountOutcome.Success(noAddress.copy(address = "1 Example Road")), outcome)
    }

    @Test
    fun `repository failure is passed on`() = runTest {
        val error = AuthError.Validation("E-mail already in use")
        repository.updateResult = AccountResult.Failure(error)

        val outcome = update(email = "other@example.com")

        assertEquals(AccountOutcome.Failed(error), outcome)
    }

    @Test
    fun `transport failure is passed on`() = runTest {
        repository.updateResult = AccountResult.Failure(AuthError.Other(AppError.Network))

        assertEquals(AccountOutcome.Failed(AuthError.Other(AppError.Network)), update(name = "Janet Doe"))
    }
}
