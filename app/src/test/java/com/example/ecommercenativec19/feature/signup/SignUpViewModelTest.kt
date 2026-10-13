package com.example.ecommercenativec19.feature.signup

import com.example.ecommercenativec19.feature.auth.domain.AuthError
import com.example.ecommercenativec19.feature.auth.domain.AuthField
import com.example.ecommercenativec19.feature.auth.domain.AuthResult
import com.example.ecommercenativec19.feature.auth.domain.FakeAuthRepository
import com.example.ecommercenativec19.feature.auth.domain.FieldError
import com.example.ecommercenativec19.feature.auth.domain.SignUpData
import com.example.ecommercenativec19.feature.auth.domain.SignUpUseCase
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SignUpViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val repository = FakeAuthRepository()
    private lateinit var vm: SignUpViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        vm = SignUpViewModel(SignUpUseCase(repository))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun fillValid() {
        vm.onFullNameChange("C19 Test")
        vm.onMobileNumberChange("01000000000")
        vm.onEmailChange("user@example.com")
        vm.onPasswordChange("secret1")
    }

    @Test
    fun initialStateIsEmptyAndPasswordHidden() {
        assertEquals(SignUpUiState(), vm.uiState.value)
    }

    @Test
    fun eachFieldUpdatesOnlyItself() {
        vm.onFullNameChange("Ahmed Ali")
        vm.onMobileNumberChange("0100")
        vm.onEmailChange("a@b.c")
        vm.onPasswordChange("secret")
        assertEquals(
            SignUpUiState(fullName = "Ahmed Ali", mobileNumber = "0100", email = "a@b.c", password = "secret"),
            vm.uiState.value,
        )
    }

    @Test
    fun passwordVisibilityToggles() {
        vm.onTogglePasswordVisibility()
        assertTrue(vm.uiState.value.passwordVisible)
        vm.onTogglePasswordVisibility()
        assertFalse(vm.uiState.value.passwordVisible)
    }

    @Test
    fun `invalid input shows every field error and does not call the repository`() = runTest(dispatcher) {
        vm.onFullNameChange("ab")
        vm.onMobileNumberChange("123")
        vm.onEmailChange("nope")
        vm.onPasswordChange("abc")

        vm.onSubmit()
        advanceUntilIdle()

        assertEquals(
            mapOf(
                AuthField.Name to FieldError.NameTooShort,
                AuthField.Phone to FieldError.InvalidPhone,
                AuthField.Email to FieldError.InvalidEmail,
                AuthField.Password to FieldError.PasswordTooShort,
            ),
            vm.uiState.value.fieldErrors,
        )
        assertEquals(0, repository.signUpCalls)
    }

    @Test
    fun `editing a field clears only its own error`() = runTest(dispatcher) {
        vm.onSubmit()
        advanceUntilIdle()
        assertEquals(4, vm.uiState.value.fieldErrors.size)

        vm.onMobileNumberChange("0100")

        assertFalse(AuthField.Phone in vm.uiState.value.fieldErrors)
        assertEquals(3, vm.uiState.value.fieldErrors.size)
    }

    @Test
    fun `submitting is true while the request is in flight and taps are ignored`() = runTest(dispatcher) {
        repository.gate = CompletableDeferred()
        fillValid()

        vm.onSubmit()
        advanceUntilIdle()
        vm.onSubmit()
        advanceUntilIdle()

        assertTrue(vm.uiState.value.isSubmitting)
        assertEquals(1, repository.signUpCalls)
    }

    @Test
    fun `success sends the typed data and marks the state signed up`() = runTest(dispatcher) {
        fillValid()

        vm.onSubmit()
        advanceUntilIdle()

        assertTrue(vm.uiState.value.signedUp)
        assertFalse(vm.uiState.value.isSubmitting)
        assertEquals(SignUpData("C19 Test", "user@example.com", "01000000000", "secret1"), repository.lastSignUp)
    }

    @Test
    fun `email already used is shown as a submit error`() = runTest(dispatcher) {
        repository.result = AuthResult.Failure(AuthError.EmailAlreadyUsed)
        fillValid()

        vm.onSubmit()
        advanceUntilIdle()

        assertEquals(AuthError.EmailAlreadyUsed, vm.uiState.value.submitError)
        assertFalse(vm.uiState.value.signedUp)
    }

    @Test
    fun `state toString does not print the password`() {
        vm.onPasswordChange("hunter22")

        assertFalse(vm.uiState.value.toString().contains("hunter22"))
    }
}
