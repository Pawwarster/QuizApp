package pl.pawwar.quizapp.utils.ext

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer

fun <T> MutableLiveData<T>.observeNonNull(lifecycleOwner: LifecycleOwner, action: (T) -> Unit) {
    observe(lifecycleOwner, Observer {
        it?.let(action)
    })
}