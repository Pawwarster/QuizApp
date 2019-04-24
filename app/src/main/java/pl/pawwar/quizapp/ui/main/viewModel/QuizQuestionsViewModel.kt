package pl.pawwar.quizapp.ui.main.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.Flowable
import io.reactivex.disposables.Disposables
import io.reactivex.subjects.BehaviorSubject
import pl.pawwar.quizapp.model.Answer
import pl.pawwar.quizapp.model.Question
import pl.pawwar.quizapp.model.Rate
import pl.pawwar.quizapp.repository.QuizRepository
import pl.pawwar.quizapp.ui.main.fragment.binder.AnswerItem
import pl.pawwar.quizapp.ui.main.fragment.binder.QuizQuestionItem
import pl.pawwar.quizapp.utils.BaseEvent
import pl.pawwar.quizapp.utils.SingleLiveEvent
import pl.pawwar.quizapp.utils.ext.ioMain
import pl.pawwar.quizapp.utils.ext.next
import pl.pawwar.quizapp.utils.lce.LceException

class QuizQuestionsViewModel(
        private val quizRepository: QuizRepository
): ViewModel() {
    private var disposable = Disposables.disposed()

    private var currentScore = 0
    private var currentOrder = 0
    var currentRates: List<Rate>? = null

    val isLoading = MutableLiveData<Boolean>()
    val error = SingleLiveEvent<LceException>()
    val action = SingleLiveEvent<BaseEvent>()

    val screenItems = BehaviorSubject.create<List<QuizQuestionItem>>()

    fun getNextEvents(): Flowable<QuizRepository.NextQuestion>? = quizRepository.getNext()

    fun loadItems(quizId: Long) {
        disposable = quizRepository.getQuestions(quizId)
                .ioMain()
                .subscribe {
                    isLoading.value = it.isLoading
                    when {
                        it.isSuccess -> {
                            currentScore = 0
                            currentOrder = 0
                            currentRates = it.data?.rates
                            screenItems.next = generateScreenItems(it.data?.questions!!)
                            action.value = QuestionsDownloadedEvent(it.data.title)
                        }
                        it.isError -> error.value = it.requireError()
                    }
                }
    }

    private fun generateScreenItems(newSubs: List<Question>): List<QuizQuestionItem> {
        return newSubs.map {
            QuizQuestionItem(it)
        }
    }

    fun generateAnswerItems(newSubs: List<Answer>?): List<AnswerItem> {
        if (newSubs != null && newSubs.isNotEmpty()) {
            return newSubs.map {
                AnswerItem(it)
            }
        }
        return emptyList()
    }

    fun updateScore(answer: Answer) {
        if (answer.isCorrect == 1) ++currentScore
    }

    fun newNext() {
        quizRepository.newNextEvent(QuizRepository.NextQuestion(++currentOrder))
    }

    fun getDefaultImage(): String {
        return quizRepository.defualtQuizPhoto
    }

    fun getFinalScore(): Int {
        return ((currentScore*100).toDouble()/screenItems.value.size).toInt()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

    fun getQuestion(order: Int): QuizQuestionItem? {
        return screenItems.value?.first { it.question.order == order }
    }
}

class QuestionsDownloadedEvent(title: String): BaseEvent() {
    val title = title
}