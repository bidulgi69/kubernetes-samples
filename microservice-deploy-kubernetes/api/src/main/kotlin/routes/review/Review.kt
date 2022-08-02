package routes.review

data class Review(
    var productId: Long? = null,
    val reviewId: Long,
    val author: String,
    val subject: String,
    val content: String,
    val version: Int? = null,
)
