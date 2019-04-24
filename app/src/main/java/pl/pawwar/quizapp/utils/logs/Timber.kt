package pl.pawwar.quizapp.utils.logs

import org.koin.log.Logger
import pl.pawwar.quizapp.BuildConfig
import timber.log.Timber

fun initTimber() {
    if (BuildConfig.DEBUG) {
        Timber.plant(Timber.DebugTree())
    }
}


val timberLogger = object : Logger {
    override fun debug(msg: String) {
        Timber.d(msg)
    }

    override fun err(msg: String) {
        Timber.e(msg)
    }

    override fun log(msg: String) {
        Timber.i(msg)
    }

}