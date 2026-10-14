package com.example.ecommercenativec19.feature.auth

enum class AuthField { Name, Phone, Email, Password }

/** Why a field was rejected. The UI maps each to copy. */
enum class FieldError { Required, InvalidEmail, NameTooShort, InvalidPhone, PasswordTooShort }

/** Client-side form rules: the only check the UI-only build runs before it "signs in". */
object AuthValidation {
    const val MIN_NAME_LENGTH = 3
    const val MIN_PASSWORD_LENGTH = 6

    private val EMAIL = Regex("""^[^\s@]+@[^\s@]+\.[^\s@]+$""")

    // Egyptian mobile numbers: 01[0125] followed by 8 digits.
    private val EGYPT_PHONE = Regex("""^01[0125]\d{8}$""")

    fun name(value: String): FieldError? = when {
        value.isBlank() -> FieldError.Required
        value.trim().length < MIN_NAME_LENGTH -> FieldError.NameTooShort
        else -> null
    }

    fun email(value: String): FieldError? = when {
        value.isBlank() -> FieldError.Required
        !EMAIL.matches(value.trim()) -> FieldError.InvalidEmail
        else -> null
    }

    fun phone(value: String): FieldError? = when {
        value.isBlank() -> FieldError.Required
        !EGYPT_PHONE.matches(value.trim()) -> FieldError.InvalidPhone
        else -> null
    }

    /** Sign-up rule: at least [MIN_PASSWORD_LENGTH] characters and nothing else. */
    fun newPassword(value: String): FieldError? = when {
        value.isEmpty() -> FieldError.Required
        value.length < MIN_PASSWORD_LENGTH -> FieldError.PasswordTooShort
        else -> null
    }

    /** Sign-in only needs a non-empty password. */
    fun existingPassword(value: String): FieldError? = if (value.isEmpty()) FieldError.Required else null
}
