package com.davioooh.qsearch.utils

import java.security.Key
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class AesEncryption(private val key: ByteArray) : TokenEncryption {

    override fun encrypt(txt: String): String {
        val key = generateKey()
        val c = Cipher.getInstance(ALGORITHM)
        c.init(Cipher.ENCRYPT_MODE, key)
        val encVal = c.doFinal(txt.toByteArray())
        return Base64.getEncoder().encodeToString(encVal)
    }

    override fun decrypt(txt: String): String {
        val key = generateKey()
        val c = Cipher.getInstance(ALGORITHM)
        c.init(Cipher.DECRYPT_MODE, key)
        val decodedValue = Base64.getDecoder().decode(txt)
        val decValue = c.doFinal(decodedValue)
        return String(decValue)
    }

    private fun generateKey(): Key {
        return SecretKeySpec(key, ALGORITHM)
    }

    companion object {
        private const val ALGORITHM = "AES"
    }
}
