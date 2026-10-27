package com.example.ecommercenativec19.feature.auth.domain

import javax.inject.Inject

class SignInUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): AuthOutcome {
        val errors = buildMap {
            AuthValidation.email(email)?.let { put(AuthField.Email, it) }
            AuthValidation.existingPassword(password)?.let { put(AuthField.Password, it) }
        }
        if (errors.isNotEmpty()) return AuthOutcome.Invalid(errors)
        return when (val result = repository.signIn(email.trim(), password)) {
            AuthResult.Success -> AuthOutcome.Success
            is AuthResult.Failure -> AuthOutcome.Failed(result.error)
        }
    }
}
