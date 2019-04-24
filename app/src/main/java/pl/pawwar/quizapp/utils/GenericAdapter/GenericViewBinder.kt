package pl.pawwar.quizapp.utils.GenericAdapter

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlin.reflect.KClass

open class GenericViewBinder<T : Any>(@LayoutRes private val layout: Int,
                                      val dataClass: KClass<T>,
                                      private val bind: RecyclerView.ViewHolder.(T) -> Unit = {},
                                      private val bindPayloads: RecyclerView.ViewHolder.(T, MutableList<Any>?) -> Unit = { data, _ -> bind(data) },
                                      private val onViewCreated: (View) -> Unit = {}
) {
    open fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        this.onViewCreated(view)
        return object : RecyclerView.ViewHolder(view) {}
    }
    open fun bindViewHolder(holder: RecyclerView.ViewHolder, data: T) = holder.bind(data)
    open fun bindViewHolderPayloads(holder: RecyclerView.ViewHolder, data: T, payloads: MutableList<Any>?) = holder.bindPayloads(data, payloads)
}