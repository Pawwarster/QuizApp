package pl.pawwar.quizapp.utils.ext

import android.content.Context
import android.os.Build
import android.support.annotation.*
import android.view.View
import pl.pawwar.quizapp.Application
import pl.pawwar.quizapp.R

fun getResourceAsString(context: Context, @RawRes id: Int) =
        context.resources
                .openRawResource(id)
                .bufferedReader()
                .use {
                    it.readText()
                }


fun getInteger(@IntegerRes id: Int) =
        Application.instance.resources.getInteger(id)


fun getLong(@IntegerRes id: Int) =
        Application.instance.resources.getInteger(id).toLong()


fun getDimen(@DimenRes dimenId: Int) =
        Application.instance.resources.getDimension(dimenId)


fun getDimenInt(@DimenRes dimenId: Int) =
        getDimen(dimenId).toInt()


fun View.getDrawable(@DrawableRes drawableId: Int) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context.getDrawable(drawableId)
        } else {
            @Suppress("DEPRECATION")
            context.resources.getDrawable(drawableId)
        }


fun getColor(@ColorRes colorId: Int) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Application.instance.resources.getColor(colorId, Application.instance.theme)
        } else {
            @Suppress("DEPRECATION")
            Application.instance.resources.getColor(colorId)
        }


fun Context.getColorCompat(@ColorRes colorId: Int) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            resources.getColor(colorId, theme)
        } else {
            @Suppress("DEPRECATION")
            resources.getColor(colorId)
        }


fun View.getColorCompat(@ColorRes colorId: Int) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            resources.getColor(colorId, context.theme)
        } else {
            @Suppress("DEPRECATION")
            resources.getColor(colorId)
        }


fun getString(@StringRes key: Int): String =
        Application.instance.getString(key)


fun getString(@StringRes key: Int, vararg args: Any): String =
        Application.instance.getString(key, *args)

fun getQuantityString(@PluralsRes key: Int, count: Int, vararg args: Any): String =
        Application.instance.resources.getQuantityString(key, count, *args)

fun unknownError() =
        Application.instance.getString(R.string.error_unknown)

fun networkError() =
        Application.instance.getString(R.string.error_network)