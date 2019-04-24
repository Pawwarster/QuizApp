package pl.pawwar.quizapp.api

import io.reactivex.Flowable
import pl.pawwar.quizapp.model.Quiz
import pl.pawwar.quizapp.model.QuizQuestions
import pl.pawwar.quizapp.utils.lce.LceException
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("quizzes/0/100")
    fun getQuizzes(): Flowable<QuizzesResponse>

    @GET("quiz/{QuizId}/0")
    fun getQestions(@Path("QuizId") quizId: Long): Flowable<QuizQuestions>
}

interface ApiResponse {
    val status: Int
    val error: Error
}

data class QuizzesResponse(
        override val status: Int = 200,
        override val error: Error,
        val count: Int,
        val items: List<Quiz>
): ApiResponse

data class Error (
        val message: String
)

fun <T: ApiResponse> Flowable<T>.handleErrors(): Flowable<T> {
    return this.map {
        when {
            it.error == null -> it
            it.error.message.isNotEmpty() -> throw LceException(it.error.message, errorCode = it.status.toString())
            else -> it
        }
    }
}
