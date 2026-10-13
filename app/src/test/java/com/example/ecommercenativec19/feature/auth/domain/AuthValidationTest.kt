package com.example.ecommercenativec19.feature.auth.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AuthValidationTest {

    @Test
    fun `name needs at least 3 characters after trimming`() {
        assertEquals(FieldError.Required, AuthValidation.name("  "))
        assertEquals(FieldError.NameTooShort, AuthValidation.name(" ab "))
        assertNull(AuthValidation.name("abc"))
    }

    @Test
    fun `email must look like an address`() {
        assertEquals(FieldError.Required, AuthValidation.email(""))
        assertEquals(FieldError.InvalidEmail, AuthValidation.email("nope"))
        assertEquals(FieldError.InvalidEmail, AuthValidation.email("a@b"))
        assertNull(AuthValidation.email("user@example.com"))
        assertNull(AuthValidation.email("  user@example.com "))
    }

    @Test
    fun `phone accepts only Egyptian mobile numbers`() {
        assertEquals(FieldError.Required, AuthValidation.phone(""))
        assertEquals(FieldError.InvalidPhone, AuthValidation.phone("0101234567"))
        assertEquals(FieldError.InvalidPhone, AuthValidation.phone("01312345678"))
        assertEquals(FieldError.InvalidPhone, AuthValidation.phone("010123456789"))
        listOf("01012345678", "01112345678", "01212345678", "01512345678").forEach {
            assertNull(it, AuthValidation.phone(it))
        }
    }

    @Test
    fun `new password needs at least 6 characters and has no other rule`() {
        assertEquals(FieldError.Required, AuthValidation.newPassword(""))
        assertEquals(FieldError.PasswordTooShort, AuthValidation.newPassword("abcde"))
        assertNull(AuthValidation.newPassword("abcdef"))
    }

    @Test
    fun `password is not trimmed`() {
        assertNull(AuthValidation.newPassword("      "))
    }

    @Test
    fun `existing password only has to be present`() {
        assertEquals(FieldError.Required, AuthValidation.existingPassword(""))
        assertNull(AuthValidation.existingPassword("a"))
    }
}
