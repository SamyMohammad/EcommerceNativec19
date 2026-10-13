package com.example.ecommercenativec19.core.session

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * AES-256/GCM with a non-exportable key in the Android Keystore. Stored format: Base64(iv || ciphertext+tag).
 *
 * Used instead of `androidx.security:security-crypto`, which is deprecated; the Keystore API it wrapped is
 * stable and this is all the app needs. The key is not part of backups, so a restored blob fails to decrypt and
 * the session is simply cleared.
 */
class AndroidKeystoreTokenCipher : TokenCipher {

    override fun encrypt(plainText: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateKey())
        val encrypted = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(cipher.iv + encrypted, Base64.NO_WRAP)
    }

    override fun decrypt(cipherText: String): String {
        val bytes = Base64.decode(cipherText, Base64.NO_WRAP)
        require(bytes.size > IV_SIZE_BYTES) { "Cipher text too short" }
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, getOrCreateKey(), GCMParameterSpec(TAG_SIZE_BITS, bytes, 0, IV_SIZE_BYTES))
        val plain = cipher.doFinal(bytes, IV_SIZE_BYTES, bytes.size - IV_SIZE_BYTES)
        return String(plain, Charsets.UTF_8)
    }

    private fun getOrCreateKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        (keyStore.getKey(KEY_ALIAS, null) as? SecretKey)?.let { return it }
        val generator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        generator.init(
            KeyGenParameterSpec.Builder(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(KEY_SIZE_BITS)
                .build(),
        )
        return generator.generateKey()
    }

    private companion object {
        const val ANDROID_KEYSTORE = "AndroidKeyStore"
        const val KEY_ALIAS = "session_token_key"
        const val TRANSFORMATION = "AES/GCM/NoPadding"
        const val KEY_SIZE_BITS = 256
        const val IV_SIZE_BYTES = 12
        const val TAG_SIZE_BITS = 128
    }
}
