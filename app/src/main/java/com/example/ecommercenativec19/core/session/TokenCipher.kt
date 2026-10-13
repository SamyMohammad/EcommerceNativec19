package com.example.ecommercenativec19.core.session

/** Reversible encryption of the token for storage. An interface so JVM tests can use a fake instead of the Keystore. */
interface TokenCipher {
    /** Returns a storable string. Throws if the key is unusable. */
    fun encrypt(plainText: String): String

    /** Throws if [cipherText] is corrupt or was produced by another key (for example after a backup restore). */
    fun decrypt(cipherText: String): String
}
