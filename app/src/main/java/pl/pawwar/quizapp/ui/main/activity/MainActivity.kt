package pl.pawwar.quizapp.ui.main.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.jskierbi.bundle_helper.withArgs
import pl.pawwar.quizapp.R
import pl.pawwar.quizapp.ui.main.fragment.FinalScoreFragment
import pl.pawwar.quizapp.ui.main.fragment.MainFragmentAbstract
import pl.pawwar.quizapp.ui.main.fragment.QuizListFragment
import pl.pawwar.quizapp.ui.main.fragment.QuizQuestionsFragment
import java.lang.ref.WeakReference

class MainActivity: AppCompatActivity(), MainNavigationInterface {

    // Used to handle double back button press to exit app.
    private var mLastBackPressed: Long = 0
    private val DOUBLE_CLICK_DELAY_TO_EXIT: Long = 2000
    // Used to cancel toast when user exits from application.
    private var exitToastRef: WeakReference<Toast>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            // Start home page on very initial launch.
            showQuizListFragment()
        }

    }

    override fun goBack() {
        onBackPressed()
    }

    override fun onBackPressed() {
        // Firstly handle back press in each fragments.
        supportFragmentManager.fragments.reversed().forEach {
            if (it is MainFragmentAbstract) {
                val consumed = it.onBackPressed()
                if (consumed) {
                    return
                }
            }
        }
        // Application should have always added one Fragment to the backstack. It's by navigation design. Double tap to exit
        if (supportFragmentManager.backStackEntryCount == 1) {
            // Double click to exit.
            if (System.currentTimeMillis() > mLastBackPressed + DOUBLE_CLICK_DELAY_TO_EXIT) {
                mLastBackPressed = System.currentTimeMillis()
                val exitToast = Toast.makeText(baseContext, R.string.press_again_to_exit, Toast.LENGTH_SHORT)
                exitToastRef = WeakReference(exitToast)
                exitToast.show()
                return
            }

            // Cancel toast if exists.
            val exitToast = if (exitToastRef != null) exitToastRef!!.get() else null
            if (exitToast != null) exitToast!!.cancel()

            finish()
            return
        }
        super.onBackPressed()
    }

    override fun popBackStack(tag: String?, inclusive: Boolean) {
        supportFragmentManager.popBackStack(tag, if (inclusive) FragmentManager.POP_BACK_STACK_INCLUSIVE else 0)
    }

    override fun showFinalScore(id: Long, score: Int, content: String?) {
        if (supportFragmentManager.findFragmentByTag(FinalScoreFragment.TAG) != null) {
            supportFragmentManager.popBackStackImmediate(FinalScoreFragment.TAG, 0)
        } else {
            showPage(
                    FinalScoreFragment()
                            .withArgs(FinalScoreFragment.ARG_ID to id,
                                    FinalScoreFragment.ARG_SCORE to score,
                                    FinalScoreFragment.ARG_CONTENT to content),
                    FinalScoreFragment.TAG)
        }
    }

    override fun showQuizQuestionsFragment(id: Long) {
        if (supportFragmentManager.findFragmentByTag(QuizQuestionsFragment.TAG) != null) {
            supportFragmentManager.popBackStackImmediate(QuizQuestionsFragment.TAG, 0)
        } else {
            showPage(
                    QuizQuestionsFragment()
                            .withArgs(QuizQuestionsFragment.ARG_ID to id),
                    QuizQuestionsFragment.TAG)
        }
    }

    override fun showQuizListFragment() {
        if (supportFragmentManager.findFragmentByTag(QuizListFragment.TAG) != null) {
            supportFragmentManager.popBackStackImmediate(QuizListFragment.TAG, 0)
        } else {
            showPage(QuizListFragment(), QuizListFragment.TAG)
        }
    }

    private fun showPage(fragment: Fragment, tag: String, addToBackstack: Boolean = true, replaceParentFragment: Boolean = true, tagToCheck: Boolean = false) {
        if (tagToCheck) {
            if (supportFragmentManager.findFragmentByTag(tag) != null) {
                supportFragmentManager.popBackStackImmediate(tag, 0)
                return
            }
        }
        supportFragmentManager?.beginTransaction()
                ?.apply {
                    if (replaceParentFragment) {
                        replace(R.id.fragmentContainer, fragment, tag)
                    } else {
                        add(R.id.fragmentContainer, fragment, tag)
                    }
                }
                ?.setCustomAnimations(R.anim.slide_from_left, 0, 0, R.anim.slide_to_left)
                ?.apply {
                    if (addToBackstack) addToBackStack(tag)
                }
                ?.commit()
    }
}