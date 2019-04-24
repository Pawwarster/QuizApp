package pl.pawwar.quizapp.utils

import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.text.Html
import pl.pawwar.quizapp.Application
import pl.pawwar.quizapp.BuildConfig
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.math.BigDecimal
import java.math.RoundingMode

fun fromHtmlCompat(html: String?): CharSequence =
        if (html != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
            } else {
                @Suppress("DEPRECATION")
                Html.fromHtml(html)
            }
        } else {
            ""
        }


fun <E> Collection<E>?.isNullOrEmpty(): Boolean {
    return this == null || this.isEmpty()
}


/**
 * Creates shallow copy of the list.
 */
fun <T> List<T>.copy() = this.toList()


operator fun BigDecimal.times(quantity: Int): BigDecimal =
        this.multiply(quantity.toBigDecimal())


operator fun BigDecimal.div(quantity: Int): BigDecimal =
        this.div(quantity.toBigDecimal())


operator fun BigDecimal.plus(quantity: Int) =
        this.plus(quantity.toBigDecimal())


operator fun BigDecimal.minus(quantity: Int) =
        this.minus(quantity.toBigDecimal())

operator fun BigDecimal.compareTo(i: Int) =
        this.compareTo(i.toBigDecimal())

fun BigDecimal.formatPrice() =
        setScale(2, RoundingMode.HALF_UP).toString()


fun <T> T.deepCopy(): T {
    val bos = ByteArrayOutputStream()
    val oos = ObjectOutputStream(bos)
    oos.writeObject(this)
    oos.flush()
    oos.close()
    bos.close()
    val byteData = bos.toByteArray()
    val bais = ByteArrayInputStream(byteData)
    @Suppress("UNCHECKED_CAST")
    return ObjectInputStream(bais).readObject() as T
}


fun failFastOnDev(e: Throwable? = null) {
    if (BuildConfig.DEBUG) {
        throw RuntimeException("Fail-Fast", e)
    }
}

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

fun getStatusBarHeight(): Int {
    var result = 0
    val resourceId = Application.instance.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = Application.instance.resources.getDimensionPixelSize(resourceId)
    }
    return result
}

fun <T> rearrange(items: List<T>, input: T) {
    val i = items.indexOf(input)
    if (i >= 0) {
        items.toMutableList().apply {add(0, removeAt(i))}
    }
}

fun isNetworkAvailable(): Boolean {
    val cm = Application.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    return activeNetwork?.isConnectedOrConnecting == true
}