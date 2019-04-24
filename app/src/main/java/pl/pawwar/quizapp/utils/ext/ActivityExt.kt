package pl.pawwar.quizapp.utils.ext

import android.app.Activity
import android.content.Intent
import android.support.annotation.StringRes
import android.widget.Toast

fun <T> Activity.startActivity(cls: Class<T>) {
    startActivity(Intent(this, cls))
}

fun Activity.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}


fun Activity.toast(@StringRes messageId: Int) {
    Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show()
}
