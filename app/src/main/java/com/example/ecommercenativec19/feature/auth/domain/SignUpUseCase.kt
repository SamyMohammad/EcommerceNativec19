package com.example.ecommercenativec19.feature.auth.domain

import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(name: String, phone: String, email: String, password: String): AuthOutcome {
        val errors = buildMap {
            AuthValidation.name(name)?.let { put(AuthField.Name, it) }
            AuthValidation.phone(phone)?.let { put(AuthField.Phone, it) }
            AuthValidation.email(email)?.let { put(AuthField.Email, it) }
            AuthValidation.newPassword(password)?.let { put(AuthField.Password, it) }
        }
        if (errors.isNotEmpty()) return AuthOutcome.Invalid(errors)
        val data = SignUpData(name = name.trim(), email = email.trim(), phone = phone.trim(), password = password)
        return when (val result = repository.signUp(data)) {
            AuthResult.Success -> AuthOutcome.Success
            is AuthResult.Failure -> AuthOutcome.Failed(result.error)
        }
    }
}
