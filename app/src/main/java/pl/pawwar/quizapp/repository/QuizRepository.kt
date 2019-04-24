package pl.pawwar.quizapp.repository

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import pl.pawwar.quizapp.api.ApiService
import pl.pawwar.quizapp.api.handleErrors
import pl.pawwar.quizapp.db.DBOpenHelper
import pl.pawwar.quizapp.model.Question
import pl.pawwar.quizapp.model.Quiz
import pl.pawwar.quizapp.model.QuizQuestions
import pl.pawwar.quizapp.utils.crypto.FileCryptoHelper
import pl.pawwar.quizapp.utils.crypto.encryptedPersistentProperty
import pl.pawwar.quizapp.utils.isNetworkAvailable
import pl.pawwar.quizapp.utils.lce.Lce
import pl.pawwar.quizapp.utils.lce.lce
import timber.log.Timber

class QuizRepository(
        private val apiService: ApiService,
        private val dbOpenHelper: DBOpenHelper,
        private val fileCryptoHelper: FileCryptoHelper
) {

    //z braku czasu to posłuży za bazę danych :), juz mialem gotową implementacje
    var questionsSaved: List<QuizQuestions> by encryptedPersistentProperty(fileCryptoHelper, emptyList())
    var quizesSaved: List<Quiz> by encryptedPersistentProperty(fileCryptoHelper, emptyList())

    private var questions: BehaviorSubject<List<Question>> = BehaviorSubject.createDefault(emptyList())

    var nextBus: PublishSubject<NextQuestion> = PublishSubject.create()
    data class NextQuestion(val order: Int)

    var defualtQuizPhoto = ""

    init {
        questions.subscribe {
            Timber.d("downloading new questions $it")
        }
    }

    private fun updateQuestions(newList: List<Question>) {
        Timber.d("new questions")
        questions.onNext(newList)
    }

    fun getQuizzes(): Flowable<Lce<List<Quiz>>> {
        return if (isNetworkAvailable()) {
            apiService.getQuizzes()
                    .handleErrors()
                    .map {
                        quizesSaved = it.items
                        it.items
                    }
                    .lce()
        } else {
            Flowable.just(quizesSaved)
                    .lce()
        }
    }

    fun getQuestions(quizId: Long): Flowable<Lce<QuizQuestions>> {
        return if (isNetworkAvailable()) {
            apiService.getQestions(quizId)
                    .handleErrors()
                    .map {
                        val tempList = questionsSaved.toMutableList()
                        if (!checkQuestionsAvailable(it.id)) {
                            tempList.add(it)
                        }
                        questionsSaved = tempList.toList()

                        updateQuestions(it.questions)
                        it
                    }
                    .lce()
        } else {
            val questions = questionsSaved.first { it.id == quizId }
            Flowable.just(questions)
                    .lce()

        }
    }

    fun checkQuestionsAvailable(quizId: Long): Boolean {
        return questionsSaved.any { it.id == quizId }
    }


    fun newNextEvent(event: NextQuestion) {
        Timber.d("new next event $event")
        if (event.order > 0) {
            Timber.d("Next Question $event.order")
        }
        nextBus.onNext(event)
    }

    fun getNext() = nextBus.observeOn(AndroidSchedulers.mainThread()).toFlowable(BackpressureStrategy.LATEST)
}