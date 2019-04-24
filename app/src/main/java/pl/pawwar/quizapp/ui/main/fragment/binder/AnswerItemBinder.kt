package pl.pawwar.quizapp.ui.main.fragment.binder

import kotlinx.android.synthetic.main.item_answer.view.*
import pl.pawwar.quizapp.R
import pl.pawwar.quizapp.utils.GenericAdapter.GenericViewBinder
import pl.pawwar.quizapp.utils.isNullOrEmpty

class AnswerItemBinder(
        private val onClicked: (AnswerItem) -> Unit
): GenericViewBinder<AnswerItem>(
        layout = R.layout.item_answer,
        dataClass = AnswerItem::class,
        bindPayloads = { data, payloads ->
            if (payloads.isNullOrEmpty()) {
                itemView.answer.text = data.answer.text

                itemView.answer.setOnClickListener{
                    onClicked(data)
                }
            } else {
                // Item state has changed - partial binding.
            }
            itemView.animate()
                    .alpha(1f)
                    .setDuration(200)
                    .start()
        }
)