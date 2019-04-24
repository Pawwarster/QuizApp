package pl.pawwar.quizapp.ui.main.fragment

import android.os.Bundle
import android.view.View
import com.jskierbi.bundle_helper.lazyArg
import kotlinx.android.synthetic.main.fragment_final_score.*
import pl.pawwar.quizapp.R

class FinalScoreFragment: MainFragmentAbstract() {
    private val id by lazyArg<Long>(ARG_ID)
    private val score by lazyArg<Int>(ARG_SCORE)
    private val content by lazyArg<String?>(ARG_CONTENT)

    companion object {
        const val TAG = "FinalScoreFragment"
        const val ARG_ID = "ARG_ID"
        const val ARG_SCORE = "ARG_SCORE"
        const val ARG_CONTENT = "ARG_CONTENT"
    }

    override val viewLayoutId = R.layout.fragment_final_score

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView()
    }

    private fun bindView() {
        contentText.text = this.content
        scoreText.text = "$score %"

        again.setOnClickListener {
            navigation?.popBackStack(QuizQuestionsFragment.TAG)
            navigation?.showQuizQuestionsFragment(id)
            navigation?.popBackStack(TAG)
        }

        back.setOnClickListener {
            navigation?.popBackStack(QuizQuestionsFragment.TAG)
            navigation?.goBack()
        }
    }


}