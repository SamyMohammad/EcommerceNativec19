package com.example.ecommercenativec19.feature.signin

import com.example.ecommercenativec19.core.common.AppError
import com.example.ecommercenativec19.feature.auth.domain.AuthError
import com.example.ecommercenativec19.feature.auth.domain.AuthField
import com.example.ecommercenativec19.feature.auth.domain.AuthResult
import com.example.ecommercenativec19.feature.auth.domain.FakeAuthRepository
import com.example.ecommercenativec19.feature.auth.domain.FieldError
import com.example.ecommercenativec19.feature.auth.domain.SignInUseCase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SignInViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val repository = FakeAuthRepository()
    private lateinit var vm: SignInViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        vm = SignInViewModel(SignInUseCase(repository))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun fill(email: String = "user@example.com", password: String = "secret1") {
        vm.onEmailChange(email)
        vm.onPasswordChange(password)
    }

    @Test
    fun initialStateIsEmptyAndPasswordHidden() {
        assertEquals(SignInUiState(), vm.uiState.value)
    }

    @Test
    fun typedTextIsStored() {
        fill("a@b.co", "secret")
        assertEquals(SignInUiState(email = "a@b.co", password = "secret"), vm.uiState.value)
    }

    @Test
    fun passwordVisibilityToggles() {
        vm.onTogglePasswordVisibility()
        assertTrue(vm.uiState.value.passwordVisible)
        vm.onTogglePasswordVisibility()
        assertFalse(vm.uiState.value.passwordVisible)
    }

    @Test
    fun `invalid input shows field errors and does not call the repository`() = runTest(dispatcher) {
        fill(email = "nope", password = "")

        vm.onSubmit()
        advanceUntilIdle()

        val state = vm.uiState.value
        assertEquals(
            mapOf(AuthField.Email to FieldError.InvalidEmail, AuthField.Password to FieldError.Required),
            state.fieldErrors,
        )
        assertFalse(state.isSubmitting)
        assertEquals(0, repository.signInCalls)
    }

    @Test
    fun `editing a field clears only its own error`() = runTest(dispatcher) {
        fill(email = "nope", password = "")
        vm.onSubmit()
        advanceUntilIdle()

        vm.onEmailChange("user@example.com")

        assertEquals(mapOf(AuthField.Password to FieldError.Required), vm.uiState.value.fieldErrors)
    }

    @Test
    fun `submitting is true while the request is in flight`() = runTest(dispatcher) {
        repository.gate = CompletableDeferred()
        fill()

        vm.onSubmit()
        advanceUntilIdle()

        assertTrue(vm.uiState.value.isSubmitting)
        assertFalse(vm.uiState.value.signedIn)
    }

    @Test
    fun `a second tap while submitting is ignored`() = runTest(dispatcher) {
        repository.gate = CompletableDeferred()
        fill()

        vm.onSubmit()
        advanceUntilIdle()
        vm.onSubmit()
        advanceUntilIdle()

        assertEquals(1, repository.signInCalls)
    }

    @Test
    fun `success marks the state signed in`() = runTest(dispatcher) {
        fill()

        vm.onSubmit()
        advanceUntilIdle()

        assertTrue(vm.uiState.value.signedIn)
        assertFalse(vm.uiState.value.isSubmitting)
        assertNull(vm.uiState.value.submitError)
    }

    @Test
    fun `wrong credentials show a submit error and stay on the screen`() = runTest(dispatcher) {
        repository.result = AuthResult.Failure(AuthError.InvalidCredentials)
        fill()

        vm.onSubmit()
        advanceUntilIdle()

        assertEquals(AuthError.InvalidCredentials, vm.uiState.value.submitError)
        assertFalse(vm.uiState.value.signedIn)
        assertFalse(vm.uiState.value.isSubmitting)
    }

    @Test
    fun `server or network error is surfaced and can be retried`() = runTest(dispatcher) {
        repository.result = AuthResult.Failure(AuthError.Other(AppError.Network))
        fill()
        vm.onSubmit()
        advanceUntilIdle()
        assertEquals(AuthError.Other(AppError.Network), vm.uiState.value.submitError)

        repository.result = AuthResult.Success
        vm.onSubmit()
        advanceUntilIdle()

        assertTrue(vm.uiState.value.signedIn)
        assertNull(vm.uiState.value.submitError)
    }

    @Test
    fun `typing after a failure clears the stale submit error`() = runTest(dispatcher) {
        repository.result = AuthResult.Failure(AuthError.InvalidCredentials)
        fill()
        vm.onSubmit()
        advanceUntilIdle()

        vm.onPasswordChange("another1")

        assertNull(vm.uiState.value.submitError)
    }

    @Test
    fun `state toString does not print the password`() {
        vm.onPasswordChange("hunter22")

        assertFalse(vm.uiState.value.toString().contains("hunter22"))
    }
}
