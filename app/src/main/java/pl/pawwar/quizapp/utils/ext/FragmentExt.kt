package pl.pawwar.quizapp.utils.ext

import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.widget.Toast

fun Fragment.toast(@StringRes msg: Int) {
    context.let {
        Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
    }
}


fun Fragment.toast(msg: String) {
    context.let {
        Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
    }
}