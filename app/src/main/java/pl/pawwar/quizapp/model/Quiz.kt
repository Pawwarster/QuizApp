package pl.pawwar.quizapp.model

data class Quiz(
    val buttonStart: String,
    val shareTitle: String,
    val questions: Int,
    val sponsored: Boolean,
    val categories: List<Category>,
    val id: Long,
    val title: String,
    val type: String,
    val content: String,
    val mainPhoto: MainPhotoData,
    val tags: List<QuizTag>,
    val category: QuizCategory,

    //added parameters
    var finalScore: Int = 0,
    var isInProgress: Boolean = false
)

data class Category(
    val uid: Long,
    val secondaryCid: String,
    val name: String,
    val type: String
)

data class MainPhotoData(
    val author: String,
    val width: Int,
    val source: String,
    val title: String,
    val url: String,
    val height: Int
)

data class QuizTag(
    val uid: Long,
    val name: String,
    val type: String
)

data class QuizCategory(
    val id: Long,
    val name: String
)