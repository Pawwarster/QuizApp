package pl.pawwar.quizapp.ui.main.fragment.binder

import android.support.v7.util.DiffUtil

class QuizDiffCallback(private val oldList: List<QuizScreenItem>, private val newList: List<QuizScreenItem>) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        if (oldItem.javaClass == newItem.javaClass && oldItem == newItem) return true
        return false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        if (oldItem != newItem) return Any()
        return null
    }

}