package pl.pawwar.quizapp.ui.main.activity

interface MainNavigationInterface {

    fun goBack()
    fun popBackStack(tag: String? = null, inclusive: Boolean = false)

    fun showQuizListFragment()
    fun showQuizQuestionsFragment(id: Long)
    fun showFinalScore(id: Long, score: Int, content: String?)
}