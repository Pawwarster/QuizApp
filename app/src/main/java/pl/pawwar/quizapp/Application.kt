package pl.pawwar.quizapp

import android.support.multidex.MultiDexApplication
import io.reactivex.plugins.RxJavaPlugins
import org.koin.android.ext.android.startKoin
import pl.pawwar.quizapp.inject.cryptoModule
import pl.pawwar.quizapp.inject.mainModule
import pl.pawwar.quizapp.inject.networkModule
import pl.pawwar.quizapp.utils.logs.initTimber
import pl.pawwar.quizapp.utils.logs.timberLogger
import timber.log.Timber
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class Application: MultiDexApplication() {

    companion object {
        lateinit var instance: Application
    }

    override fun onCreate() {
        super.onCreate()
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )
        instance = this
        initTimber()
        startKoin(this, listOf(networkModule, mainModule, cryptoModule), logger = timberLogger)
        RxJavaPlugins.setErrorHandler { Timber.e(it, "Unhandled RX exception") }
    }
}