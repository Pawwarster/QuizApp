package pl.pawwar.quizapp.inject

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext
import pl.pawwar.quizapp.api.ApiService
import pl.pawwar.quizapp.BuildConfig
import pl.pawwar.quizapp.R
import pl.pawwar.quizapp.utils.ext.getString
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit

val networkModule: Module = applicationContext {

    bean { httpClientBuilder(get()) }
    bean { httpClient(get()) }
    bean { gson() }
    bean { retrofit(get(), get()) }
    bean { /** ApiService by */ createWebService<ApiService>(get()) }
}

const val CACHE_SIZE = 20 * 1024 * 1024L // 20 MB

private fun httpClientBuilder(context: Context): OkHttpClient.Builder {
    val builder = OkHttpClient.Builder()
    if (BuildConfig.DEBUG) {
        val logging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            if (!it.contains(Regex("\\p{C}")))
                Timber.d(it)
            else
                Timber.v(it)
        })
        logging.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(logging)
        builder.addInterceptor(ChuckInterceptor(context))
    }
    builder.connectTimeout(20, TimeUnit.SECONDS)
    builder.readTimeout(20, TimeUnit.SECONDS)
    builder.cache(Cache(File(context.cacheDir, "http-cache"), CACHE_SIZE))
    return builder
}


private fun httpClient(builder: OkHttpClient.Builder): OkHttpClient {
    builder.addInterceptor {
        fun proceed(request: Request): Response {
            return it.proceed(request
                    .newBuilder()
                    .addHeader("Content-type", "application/json; charset=utf-8")
                    .build())
        }

        val response = proceed(it.request())
        response
    }
    return builder.build()
}


private fun gson(): Gson {
    val gsonBuilder = GsonBuilder()
    gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
    return gsonBuilder.create()
}


private fun retrofit(client: OkHttpClient, gson: Gson) =
        Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(when {
                    BuildConfig.DEBUG -> getString(R.string.api_url_debug)
                    else -> getString(R.string.api_url_release)
                })
                .build()


inline fun <reified T> createWebService(retrofit: Retrofit): T {
    return retrofit.create(T::class.java)
}