package pl.pawwar.quizapp.utils.crypto

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import timber.log.Timber
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private inline fun <T> SharedPreferences.delegate(
        defaultValue: T, key: String? = null,
        crossinline getter: SharedPreferences.(String, T) -> T,
        crossinline setter: SharedPreferences.Editor.(String, T) -> SharedPreferences.Editor
): ReadWriteProperty<Any, T> =
        object : ReadWriteProperty<Any, T> {
            private var cache: T? = null

            override fun getValue(thisRef: Any, property: KProperty<*>): T =
                    cache ?: getter(key ?: property.name, defaultValue)

            override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
                cache = value
                edit().setter(key ?: property.name, value).apply()
            }
        }

fun SharedPreferences.int(def: Int = 0, key: String? = null): ReadWriteProperty<Any, Int> =
        delegate(def, key, SharedPreferences::getInt, SharedPreferences.Editor::putInt)

fun SharedPreferences.long(def: Long = 0, key: String? = null): ReadWriteProperty<Any, Long> =
        delegate(def, key, SharedPreferences::getLong, SharedPreferences.Editor::putLong)

fun SharedPreferences.float(def: Float = 0f, key: String? = null): ReadWriteProperty<Any, Float> =
        delegate(def, key, SharedPreferences::getFloat, SharedPreferences.Editor::putFloat)

fun SharedPreferences.boolean(def: Boolean = false, key: String? = null): ReadWriteProperty<Any, Boolean> =
        delegate(def, key, SharedPreferences::getBoolean, SharedPreferences.Editor::putBoolean)

fun SharedPreferences.stringSet(def: Set<String> = emptySet(), key: String? = null): ReadWriteProperty<Any, Set<String>> =
        delegate(def, key, SharedPreferences::getStringSet, SharedPreferences.Editor::putStringSet)

fun SharedPreferences.string(def: String = "", key: String? = null): ReadWriteProperty<Any, String> =
        delegate(def, key, SharedPreferences::getString, SharedPreferences.Editor::putString)

inline fun <reified T> encryptedPersistentProperty(fileCryptoHelper: FileCryptoHelper, defaultValue: T, cacheValue: Boolean = true) =
        object : ReadWriteProperty<Any, T> {
            private val gson = Gson()
            private var cache: T? = null

            override fun getValue(thisRef: Any, property: KProperty<*>): T {
                val fileName = "${thisRef.javaClass.simpleName}.${property.name}"
                cache?.let {
                    Timber.d("Preference. getValue $fileName returning cached value $it")
                    return it
                }
                return try {
                    val fileReader = fileCryptoHelper.getEncryptedFileReader(fileName)
                    gson.fromJson<T>(fileReader, object : TypeToken<T>() {}.type)
                            ?: defaultValue
                                    .also {
                                        Timber.d("Preference. getValue $fileName returning stored value $it")
                                    }
                } catch (e: Throwable) {
                    defaultValue
                            .also {
                                Timber.d("Preference. getValue $fileName returning default value $it")
                            }
                }.also {
                    // Once value is restored (or used default one) and cache is enabled, then save value to cache.
                    if (cacheValue) {
                        cache = it
                    }
                }
            }

            override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
                val fileName = "${thisRef.javaClass.simpleName}.${property.name}"
                Timber.d("Preference. setValue $fileName to $value")
                if (cacheValue) {
                    cache = value
                }
                val fileWriter = fileCryptoHelper.getEncryptedFileWriter(fileName)
                gson.toJson(value, fileWriter)
                fileWriter.close()
            }
        }