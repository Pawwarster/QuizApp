package pl.pawwar.quizapp.inject

import org.koin.dsl.module.applicationContext
import pl.pawwar.quizapp.utils.crypto.CryptoTools
import pl.pawwar.quizapp.utils.crypto.FileCryptoHelper

val cryptoModule = applicationContext {

    bean { /* SecretKey() by */ CryptoTools.generateDefaultKey() }
    bean("decryptCipher") { /* Cipher decrypt mode by */ CryptoTools.getDecryptCipher(get()) }
    bean("encryptCipher") { /* Cipher encrypt mode by */ CryptoTools.getEncryptCipher(get()) }
    bean { FileCryptoHelper(get(), get("decryptCipher"), get("encryptCipher")) }

}