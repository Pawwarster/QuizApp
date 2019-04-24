package pl.pawwar.quizapp.utils.ext

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.Subject

inline fun <T> Flowable<T>.observeOnMain(): Flowable<T> =
        observeOn(AndroidSchedulers.mainThread())

inline fun <T> Flowable<T>.observeOnIo(): Flowable<T> =
        observeOn(Schedulers.io())

inline fun <T> Flowable<T>.subscribeOnIo(): Flowable<T> =
        subscribeOn(Schedulers.io())

inline fun <T> Flowable<T>.subscribeOnNewThread(): Flowable<T> =
        subscribeOn(Schedulers.newThread())

inline fun <T> Flowable<T>.ioMain(): Flowable<T> =
        subscribeOnIo().observeOnMain()


inline fun <T> Single<T>.observeOnMain(): Single<T> =
        observeOn(AndroidSchedulers.mainThread())


inline fun <T> Single<T>.subscribeOnIo(): Single<T> =
        subscribeOn(Schedulers.io())


inline fun <T> Observable<T>.observeOnMain(): Observable<T> =
        observeOn(AndroidSchedulers.mainThread())


inline fun <T> Observable<T>.subscribeOnIo(): Observable<T> =
        subscribeOn(Schedulers.io())


inline fun <T> Subject<T>.set(value: T) =
        onNext(value)

inline var <T> Subject<T>.next: T
    get() = throw IllegalAccessException("Not allowed to use as getter.")
    set(value: T) {
        onNext(value)
    }