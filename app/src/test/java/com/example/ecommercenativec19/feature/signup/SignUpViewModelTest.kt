package com.example.ecommercenativec19.feature.signup

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SignUpViewModelTest {
    @Test
    fun initialStateIsEmptyAndPasswordHidden() {
        assertEquals(SignUpUiState(), SignUpViewModel().uiState.value)
    }

    @Test
    fun eachFieldUpdatesOnlyItself() {
        val vm = SignUpViewModel()
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
        val vm = SignUpViewModel()
        vm.onTogglePasswordVisibility()
        assertTrue(vm.uiState.value.passwordVisible)
        vm.onTogglePasswordVisibility()
        assertFalse(vm.uiState.value.passwordVisible)
    }
}
