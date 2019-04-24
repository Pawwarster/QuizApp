package pl.pawwar.quizapp.ui.main.fragment.binder

import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_quiz.view.*
import pl.pawwar.quizapp.R
import pl.pawwar.quizapp.utils.GenericAdapter.GenericViewBinder
import pl.pawwar.quizapp.utils.isNullOrEmpty

class QuizScreenItemBinder(
        private val onClicked: (QuizScreenItem) -> Unit
): GenericViewBinder<QuizScreenItem>(
        layout = R.layout.list_item_quiz,
        dataClass = QuizScreenItem::class,
        bindPayloads = { data, payloads ->
            if (payloads.isNullOrEmpty()) {
                itemView.title.text = data.quiz?.title
                itemView.comment.text = data.quiz?.content
                Picasso.get()
                        .load(data.quiz?.mainPhoto?.url)
                        .fit()
                        .error(R.drawable.ico_remove)
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .into(itemView.photo)

                itemView.setOnClickListener{
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