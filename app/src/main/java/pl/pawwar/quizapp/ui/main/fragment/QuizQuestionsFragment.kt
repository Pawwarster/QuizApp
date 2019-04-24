package pl.pawwar.quizapp.ui.main.fragment

import android.os.Bundle
import android.view.View
import com.jskierbi.bundle_helper.lazyArg
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import kotlinx.android.synthetic.main.fragment_quiz_questions.*
import org.koin.android.architecture.ext.sharedViewModel
import pl.pawwar.quizapp.ui.main.viewModel.QuizQuestionsViewModel
import pl.pawwar.quizapp.R
import pl.pawwar.quizapp.ui.main.fragment.binder.QuestionsAdapter
import pl.pawwar.quizapp.ui.main.viewModel.QuestionsDownloadedEvent
import pl.pawwar.quizapp.utils.ext.observeNonNull
import pl.pawwar.quizapp.utils.ext.toast
import pl.pawwar.quizapp.utils.ext.unknownError
import timber.log.Timber

class QuizQuestionsFragment: MainFragmentAbstract() {
    private val id by lazyArg<Long>(ARG_ID)

    companion object {
        const val ARG_ID = "ARG_ID"
        const val TAG = "QuizQuestionsFragment"
    }

    override val viewLayoutId = R.layout.fragment_quiz_questions

    private val viewModel by sharedViewModel<QuizQuestionsViewModel>()

    private lateinit var pagerAdapter: QuestionsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
    }

    private fun bindViewModel() {
        viewModel.getNextEvents()?.bindToLifecycle(viewLifecycleOwner)?.subscribe({
            if (it.order == (viewModel.screenItems.value.size - 1)) {
                val finalScore = viewModel.getFinalScore()
                val rate = viewModel.currentRates?.first { finalScore >= it.from && finalScore <= it.to }
                navigation?.showFinalScore(id, viewModel.getFinalScore(), rate?.content)
            } else {
                val progress = (it.order * 100).toDouble() / viewModel.screenItems.value.size
                quizProgress.progress = progress.toInt()
                viewPager.currentItem = it.order
            }
        }, {
            Timber.e(it,"unhandled exception")
        })
        viewModel.isLoading.observeNonNull(viewLifecycleOwner) {
            loadingLayout.setLoading(it)
        }
        viewModel.error.observeNonNull(viewLifecycleOwner) {
            toast(it.message ?: unknownError())
        }
        viewModel.action.observeNonNull(viewLifecycleOwner) {
            if (it is QuestionsDownloadedEvent) {
                quizTitle.text = it.title
            }
        }
        viewModel.screenItems
                .bindToViewLifecycle()
                .subscribe {
                    pagerAdapter = QuestionsAdapter(activity?.supportFragmentManager!!)

                    it.map {
                        pagerAdapter.addFragment(QuestionFragment.newInstance(it.question.order))
                    }

                    viewPager.adapter = pagerAdapter
                    viewPager.setPagingEnabled(false)

                    quizProgress.progress = 0
                }
        viewModel.loadItems(id)
    }
}