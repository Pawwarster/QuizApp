package pl.pawwar.quizapp.utils.view

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import io.reactivex.Observable

abstract class ViewLifecycleFragment : Fragment() {

    /**
     * Backing filed for viewLifecycleOwner.
     */
    val viewLifecycleOwner: ViewLifecycleOwner = ViewLifecycleOwner()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }


    override fun onStart() {
        super.onStart()
        viewLifecycleOwner.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }


    override fun onResume() {
        super.onResume()
        viewLifecycleOwner.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }


    override fun onPause() {
        viewLifecycleOwner.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        super.onPause()
    }


    override fun onStop() {
        viewLifecycleOwner.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        super.onStop()
    }


    override fun onDestroyView() {
        viewLifecycleOwner.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        super.onDestroyView()
    }

    fun <T> Observable<T>.bindToViewLifecycle() = bindToLifecycle(viewLifecycleOwner)


    class ViewLifecycleOwner : LifecycleOwner {
        private val lifecycleRegistry = LifecycleRegistry(this)

        override fun getLifecycle(): LifecycleRegistry {
            return lifecycleRegistry
        }
    }
}