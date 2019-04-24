package pl.pawwar.quizapp.ui.main.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import pl.pawwar.quizapp.R
import pl.pawwar.quizapp.ui.main.activity.MainNavigationInterface
import pl.pawwar.quizapp.utils.ext.toast
import pl.pawwar.quizapp.utils.view.ViewLifecycleFragment

abstract class MainFragmentAbstract: ViewLifecycleFragment() {
    open val viewLayoutId = View.NO_ID


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(viewLayoutId, container, false)
    }


    /**
     * Override and return true if you want to catch pressing on back key.
     * Returns true if event is consumed and shouldn't be passed further.
     */
    open fun onBackPressed(): Boolean {
        return false
    }


    /**
     * Helper property that simplifies navigation calls.
     */
    val navigation
        get() = activity as MainNavigationInterface?


    fun hideKeyboard() {
        context?.let {
            val inputManager = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            // check if no view has focus
            val v = (context as Activity).currentFocus ?: return
            inputManager.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    fun notImplemented() {
        toast(getString(R.string.not_yet_implemented))
    }
}