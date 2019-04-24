package pl.pawwar.quizapp.utils.lce

import io.reactivex.Flowable
import pl.pawwar.quizapp.R
import pl.pawwar.quizapp.utils.ext.getString
import pl.pawwar.quizapp.utils.ext.unknownError
import timber.log.Timber
import java.net.UnknownHostException

data class Lce<out T>(val isLoading: Boolean = false, val data: T? = null, val error: LceException? = null) {

    val isError: Boolean
        get() = (!isLoading && error != null)

    val isSuccess: Boolean
        get() = (!isLoading && error == null)

    fun requireError() =
            error ?: LceException(unknownError())

    fun requireData() =
            data ?: throw LceException(unknownError())

    companion object {
        fun <T> data(data: T): Lce<T> = Lce(data = data)
        fun <T> error(error: LceException) = Lce<T>(error = error)
        fun <T> error(errorMessage: String) = Lce<T>(error = LceException(errorMessage))
        fun <T> loading(): Lce<T> = Lce(isLoading = true)
    }
}

fun <T> Flowable<T>.lce(): Flowable<Lce<T>> {
    return this.map { Lce.data(it) }
            .startWith(Lce.loading())
            .onErrorReturn {
                Timber.e(it)
                when (it) {
                    is UnknownHostException -> Lce.error(LceException(getString(R.string.error_internet_connection)))
                    else -> Lce.error((it as? LceException) ?: LceException(it.message ?: unknownError()))
                }
            }
}

open class LceException(message: String, cause: Throwable? = null, val errorCode: String = "") : RuntimeException(message, cause)