package pl.pawwar.quizapp.utils.crypto

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream

class FileCryptoHelper(
        val app: Application,
        private val decryptCipher: Cipher,
        private val encryptCipher: Cipher
) {

    fun getEncryptedFileWriter(fileName: String): OutputStreamWriter {
        val output = CipherOutputStream(app.openFileOutput(fileName, Context.MODE_PRIVATE), encryptCipher)
        return output.writer()
    }

    fun getEncryptedFileReader(fileName: String): InputStreamReader {
        val input = CipherInputStream(app.openFileInput(fileName), decryptCipher)
        return input.reader()
    }

    inline fun <reified T> loadJson(fileName: String): T? {
        val fileReader = getEncryptedFileReader(fileName)
        return try {
            Gson().fromJson(fileReader, T::class.java)
        } catch (ignore: Throwable) {
            null
        }
    }

    fun <T> saveJson(fileName: String, instance: T?) {
        val fileWriter = getEncryptedFileWriter(fileName)
        Gson().toJson(instance, fileWriter)
        fileWriter.close()
    }

    fun deleteJson(fileName: String) {
        app.deleteFile(fileName)
    }

}