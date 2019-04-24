package pl.pawwar.quizapp.ui.main.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.squareup.picasso.Picasso
import org.koin.android.architecture.ext.sharedViewModel
import pl.pawwar.quizapp.ui.main.viewModel.QuizQuestionsViewModel
import pl.pawwar.quizapp.utils.ext.observeNonNull
import pl.pawwar.quizapp.utils.ext.toast
import pl.pawwar.quizapp.utils.ext.unknownError
import kotlinx.android.synthetic.main.item_quiz_question.*
import pl.pawwar.quizapp.R
import pl.pawwar.quizapp.ui.main.fragment.binder.AnswerDiffCallback
import pl.pawwar.quizapp.ui.main.fragment.binder.AnswerItem
import pl.pawwar.quizapp.ui.main.fragment.binder.AnswerItemBinder
import pl.pawwar.quizapp.utils.GenericAdapter.GenericTypedAdapter
import pl.pawwar.quizapp.utils.copy

class QuestionFragment: MainFragmentAbstract() {
    private var order:Int = 0

    private val viewModel by sharedViewModel<QuizQuestionsViewModel>()

    override val viewLayoutId = R.layout.item_quiz_question

    private val adapter = GenericTypedAdapter<AnswerItem>(
            AnswerItemBinder(::onClicked)
    )

    companion object {
        const val ARG_ORDER = "ARG_ORDER"
        const val TAG = "QuestionFragment"

        fun newInstance(order: Int): Fragment {
            val fragment = QuestionFragment()
            val bundle = Bundle()
            bundle.putInt("ARG_ORDER", order)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        if (args != null && args.containsKey(ARG_ORDER)) {
            this.order = args.getInt(ARG_ORDER)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        bindViewModel()
    }

    private fun bindViewModel() {
        viewModel.error.observeNonNull(viewLifecycleOwner) {
            toast(it.message ?: unknownError())
        }
        val quizQuestion = viewModel.getQuestion(order)
        if (!quizQuestion?.question?.image?.url.isNullOrEmpty()) {
            Picasso.get().load(quizQuestion?.question?.image?.url).error(R.drawable.ico_remove).fit().into(photo)
        } else {
            Picasso.get().load(viewModel.getDefaultImage()).error(R.drawable.ico_remove).fit().into(photo)
        }
        question.text = quizQuestion?.question?.text

        val newAnswers = viewModel.generateAnswerItems(quizQuestion?.question?.answers)

        adapter.setDataWithAnimations(newAnswers, detectMoves = true, diffUtilCallback = AnswerDiffCallback(adapter.data.copy(), newAnswers))

    }

    private fun onClicked(answer: AnswerItem) {
        viewModel.updateScore(answer.answer)
        viewModel.newNext()
    }
}