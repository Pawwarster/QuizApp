package pl.pawwar.quizapp.model

import pl.pawwar.quizapp.api.ApiResponse
import pl.pawwar.quizapp.api.Error

data class QuizQuestions (
        override val status: Int = 200,
        override val error: Error,
        val celebrity: Celebrity,
        val rates: List<Rate>,
        val questions: List<Question>,
        val createdAt: String,
        val sponsored: Boolean,
        val title: String,
        val type: String,
        val content: String,
        val buttonStart: String,
        val shareTitle: String,
        val flagResults: List<FlagResult>,
        val categories: List<QuestionCategory>,
        val id: Long,
        val scripts: String,
        val mainPhoto: MainPhotoData,
        val category: QuizCategory,
        val isBattle: Boolean,
        val created: Long,
        val canonical: String,
        val latestResults: List<LatestResult>,
        val avgResult: Double,
        val resultCount: Int,
        val cityAvg: Double?,
        val cityTime: Int?,
        val cityCount: Int?,
        val userBattleDone: Boolean,
        val sponsoredResults: SponsoredResults
) : ApiResponse

data class Celebrity (
     val result: String,
     val imageAuthor: String,
     val imageHeight: String,
     val imageUrl: String,
     val show: Int,
     val name: String,
     val imageTitle: String,
     val imageWidth: String,
     val content: String,
     val imageSource: String
)

data class QuestionCategory (
     val uid: Long,
     val secondaryCid: String,
     val name: String,
     val type: String
)

data class Rate (
     val from: Int,
     val to: Int,
     val content: String
)

data class Question (
     val image: QuestionImage,
     val answers: List<Answer>,
     val text: String,
     val answer: String,
     val type: String,
     val order: Int
)

data class QuestionImage (
     val author: String,
     val width: String,
     val mediaId: String,
     val source: String,
     val url: String,
     val height: String
)

data class Answer (
     val image: QuestionImage,
     val order: Int,
     val text: String,
     val isCorrect: Int = 0
)

data class FlagResult (
     val image: QuestionImage,
     val flag: String,
     val title: String,
     val content: String
)

data class LatestResult (
     val city: Long,
     val end_date: String,
     val result: Double,
     val resolveTime: Int
)

data class SponsoredResults (
     val imageAuthor: String,
     val imageHeight: String,
     val imageUrl: String,
     val imageWidth: String,
     val textColor: String,
     val content: String,
     val mainColor: String,
     val imageSource: String
)