package pl.pawwar.quizapp.ui.main.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.Disposables
import io.reactivex.subjects.BehaviorSubject
import pl.pawwar.quizapp.model.Quiz
import pl.pawwar.quizapp.ui.main.fragment.binder.QuizScreenItem
import pl.pawwar.quizapp.repository.QuizRepository
import pl.pawwar.quizapp.utils.BaseEvent
import pl.pawwar.quizapp.utils.SingleLiveEvent
import pl.pawwar.quizapp.utils.ext.ioMain
import pl.pawwar.quizapp.utils.ext.next
import pl.pawwar.quizapp.utils.lce.LceException

class QuizListViewModel(
        private val quizRepository: QuizRepository
): ViewModel() {
    private var disposable = Disposables.disposed()

    val isLoading = MutableLiveData<Boolean>()
    val error = SingleLiveEvent<LceException>()
    val action = SingleLiveEvent<BaseEvent>()

    val noQuizVisible = BehaviorSubject.createDefault(false)
    val screenItems = BehaviorSubject.create<List<QuizScreenItem>>()

    fun loadItems() {
        disposable = quizRepository.getQuizzes()
                .ioMain()
                .subscribe() {
                    val quizzes = it?.data?: emptyList()
                    noQuizVisible.next = quizzes.isEmpty() && !it.isLoading && !it.isError
                    isLoading.value = it.isLoading
                    when {
                        it.isSuccess -> {
                            screenItems.next = generateScreenItems(quizzes)
                            action.value = QuizzesDownloadedEvent()
                        }
                        it.isError -> error.value = it.requireError()
                    }
                }
    }

    private fun generateScreenItems(newSubs: List<Quiz>): List<QuizScreenItem> {
        return newSubs.map {
            QuizScreenItem(it)
        }
    }

    fun checkIfQuestionsAvailable(quizId: Long): Boolean {
        return quizRepository.checkQuestionsAvailable(quizId)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

    fun setDefultPhoto(url: String) {
        quizRepository.defualtQuizPhoto = url
    }
}

class QuizzesDownloadedEvent: BaseEvent()