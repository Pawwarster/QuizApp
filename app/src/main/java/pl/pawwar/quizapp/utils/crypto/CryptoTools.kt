package pl.pawwar.quizapp.utils.crypto

import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

object CryptoTools {

    private val ENC_TYPE = "AES/ECB/PKCS5Padding"
    private val Ty2w6sY2Jb = "d8,Y@FpzBR)9u{x)<6VAXz?t0au0s>f3"

    fun generateDefaultKey() = generateKey(Ty2w6sY2Jb)

    private fun generateKey(password: String): SecretKey {
        var key = password.toByteArray()
        val maxLen = maxKeyLen()
        if (key.size > maxLen) {
            key = key.copyOf(maxLen)
        }
        return SecretKeySpec(key, "AES")
    }

    private fun maxKeyLen() = Cipher.getMaxAllowedKeyLength(ENC_TYPE) / 8

    fun getDecryptCipher(key: SecretKey): Cipher {
        val cipher = Cipher.getInstance(ENC_TYPE)
        cipher.init(Cipher.DECRYPT_MODE, key)
        return cipher
    }

    fun getEncryptCipher(key: SecretKey): Cipher {
        val cipher = Cipher.getInstance(ENC_TYPE)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        return cipher
    }
}