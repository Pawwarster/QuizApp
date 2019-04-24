package pl.pawwar.quizapp.utils.view

import android.content.Context
import android.text.method.Touch.onTouchEvent
import android.view.MotionEvent
import android.content.res.TypedArray
import android.util.AttributeSet
import pl.pawwar.quizapp.R
import android.text.method.Touch.onTouchEvent
import android.support.v4.view.ViewPager


class CustomViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    private var enable: Boolean = false

    init {
        this.enable = true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (this.enable) {
            super.onTouchEvent(event)
        } else false

    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (this.enable) {
            super.onInterceptTouchEvent(event)
        } else false

    }

    fun setPagingEnabled(enabled: Boolean) {
        this.enable = enabled
    }
}