package pl.pawwar.quizapp.ui.main.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.fragment_quiz_list.*
import org.koin.android.architecture.ext.viewModel
import pl.pawwar.quizapp.R
import pl.pawwar.quizapp.ui.main.fragment.binder.QuizDiffCallback
import pl.pawwar.quizapp.ui.main.fragment.binder.QuizScreenItem
import pl.pawwar.quizapp.ui.main.fragment.binder.QuizScreenItemBinder
import pl.pawwar.quizapp.ui.main.viewModel.QuizListViewModel
import pl.pawwar.quizapp.utils.GenericAdapter.GenericTypedAdapter
import pl.pawwar.quizapp.utils.copy
import pl.pawwar.quizapp.utils.ext.observeNonNull
import pl.pawwar.quizapp.utils.ext.toast
import pl.pawwar.quizapp.utils.ext.unknownError
import pl.pawwar.quizapp.utils.isNetworkAvailable

class QuizListFragment: MainFragmentAbstract() {
    companion object {
        const val TAG = "QuizListFragment"
    }

    private val viewModel: QuizListViewModel by viewModel()

    override val viewLayoutId = R.layout.fragment_quiz_list

    private val adapter = GenericTypedAdapter<QuizScreenItem>(
            QuizScreenItemBinder(::onClicked)
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        bindViewModel()
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadItems()
    }

    private fun bindViewModel() {
        viewModel.isLoading.observeNonNull(viewLifecycleOwner) {
            loadingLayout.setLoading(it)
        }
        viewModel.error.observeNonNull(viewLifecycleOwner) {
            toast(it.message ?: unknownError())
        }
        viewModel.screenItems
                .bindToViewLifecycle()
                .subscribe {
                    adapter.setDataWithAnimations(it, detectMoves = true, diffUtilCallback = QuizDiffCallback(adapter.data.copy(), it))
                }
    }

    private fun onClicked(quiz: QuizScreenItem) {
        if (!isNetworkAvailable() && !viewModel.checkIfQuestionsAvailable(quiz.quiz.id)) {
            toast(R.string.offline_quiz_monit)
            return
        }
        viewModel.setDefultPhoto(quiz.quiz.mainPhoto.url)
        navigation?.showQuizQuestionsFragment(quiz.quiz.id)
    }
}