package com.example.ecommercenativec19.feature.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.ecommercenativec19.fake.LocalFakeStore
import com.example.ecommercenativec19.feature.auth.AuthField
import com.example.ecommercenativec19.feature.auth.AuthValidation

/**
 * Sign up entry point. No account is created: valid form input signs the user in and [onSignedUp] sends the app
 * straight to Home.
 */
@Composable
fun SignUpRoute(
    onSignedUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val store = LocalFakeStore.current
    var state by remember { mutableStateOf(SignUpUiState()) }
    SignUpScreen(
        state = state,
        // Editing a field clears its own error, so feedback never outlives the input it describes.
        onFullNameChange = { state = state.copy(fullName = it, fieldErrors = state.fieldErrors - AuthField.Name) },
        onMobileNumberChange = { state = state.copy(mobileNumber = it, fieldErrors = state.fieldErrors - AuthField.Phone) },
        onEmailChange = { state = state.copy(email = it, fieldErrors = state.fieldErrors - AuthField.Email) },
        onPasswordChange = { state = state.copy(password = it, fieldErrors = state.fieldErrors - AuthField.Password) },
        onTogglePasswordVisibility = { state = state.copy(passwordVisible = !state.passwordVisible) },
        onSignUp = {
            val errors = buildMap {
                AuthValidation.name(state.fullName)?.let { put(AuthField.Name, it) }
                AuthValidation.phone(state.mobileNumber)?.let { put(AuthField.Phone, it) }
                AuthValidation.email(state.email)?.let { put(AuthField.Email, it) }
                AuthValidation.newPassword(state.password)?.let { put(AuthField.Password, it) }
            }
            state = state.copy(fieldErrors = errors)
            if (errors.isEmpty()) {
                store.signIn()
                onSignedUp()
            }
        },
        modifier = modifier,
    )
}
