package pl.pawwar.quizapp.utils.GenericAdapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

open class GenericTypedAdapter<V : Any>(private vararg val binders: GenericViewBinder<*>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val classList = binders.map { it.dataClass.java }

    private val dataMutable = mutableListOf<V>()
    var data: List<V>
        get() = dataMutable
        set(value) {
            dataMutable.clear()
            dataMutable.addAll(value)
            notifyDataSetChanged()
        }

    fun reset() {
        dataMutable.clear()
        notifyDataSetChanged()
    }

    fun addData(newData: List<V>, hasMore: Boolean = false) {
        val offset = dataMutable.size
        dataMutable.addAll(newData)
        notifyItemRangeInserted(offset, newData.size) // Or invalidate
    }

    fun setDataWithAnimations(newData: List<V>, detectMoves: Boolean = false, diffUtilCallback: DiffUtil.Callback = object : DiffUtil.Callback() {
        override fun areItemsTheSame(p0: Int, p1: Int): Boolean = dataMutable[p0] === newData[p1]
        override fun getOldListSize(): Int = dataMutable.size
        override fun getNewListSize(): Int = newData.size
        override fun areContentsTheSame(p0: Int, p1: Int): Boolean = dataMutable[p0] == newData[p1]
    }) {
        val result = DiffUtil.calculateDiff(diffUtilCallback, detectMoves)
        dataMutable.clear()
        dataMutable.addAll(newData)
        result.dispatchUpdatesTo(this)
    }

    override fun getItemCount() = dataMutable.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) = binders[getItemViewType(position)].bindGenericPayloads(holder, dataMutable[position], payloads)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = binders[getItemViewType(position)].bindGeneric(holder, dataMutable[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = binders[viewType].createViewHolder(parent)

    override fun getItemViewType(position: Int) = classList.lastIndexOf(dataMutable[position].javaClass).also {
        if (it == -1) throw IllegalStateException("Binder for data type not registered: ${dataMutable[position].javaClass}")
    }

    @Suppress("UNCHECKED_CAST") // this is required - bridge between declarative generics and runtime reflection
    private fun <T : Any> GenericViewBinder<*>.bindGeneric(holder: RecyclerView.ViewHolder, data: T) = (this as GenericViewBinder<T>).bindViewHolder(holder, data)

    @Suppress("UNCHECKED_CAST") // this is required - bridge between declarative generics and runtime reflection
    private fun <T : Any> GenericViewBinder<*>.bindGenericPayloads(holder: RecyclerView.ViewHolder, data: T, payloads: MutableList<Any>?) = (this as GenericViewBinder<T>).bindViewHolderPayloads(holder, data, payloads)
}