package com.example.ecommercenativec19.feature.signin

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SignInViewModelTest {
    @Test
    fun initialStateIsEmptyAndPasswordHidden() {
        assertEquals(SignInUiState(), SignInViewModel().uiState.value)
    }

    @Test
    fun typedTextIsStored() {
        val vm = SignInViewModel()
        vm.onUserNameChange("ahmed")
        vm.onPasswordChange("secret")
        assertEquals(SignInUiState(userName = "ahmed", password = "secret"), vm.uiState.value)
    }

    @Test
    fun passwordVisibilityToggles() {
        val vm = SignInViewModel()
        vm.onTogglePasswordVisibility()
        assertTrue(vm.uiState.value.passwordVisible)
        vm.onTogglePasswordVisibility()
        assertFalse(vm.uiState.value.passwordVisible)
    }
}
