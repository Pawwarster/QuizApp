package pl.pawwar.quizapp.inject

import android.preference.PreferenceManager
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext
import pl.pawwar.quizapp.db.DBOpenHelper
import pl.pawwar.quizapp.repository.QuizRepository
import pl.pawwar.quizapp.ui.main.viewModel.QuizListViewModel
import pl.pawwar.quizapp.ui.main.viewModel.QuizQuestionsViewModel

val mainModule = applicationContext {

    bean { /** SharedPreferences */ PreferenceManager.getDefaultSharedPreferences(get()) }
    bean { QuizRepository(get(), get(), get()) }
    bean { DBOpenHelper(get(), null) }

    viewModel { QuizListViewModel(get()) }
    viewModel { QuizQuestionsViewModel(get()) }
}