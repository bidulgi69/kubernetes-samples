package kr.dove.review

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document
import routes.review.Review
import java.time.LocalDateTime
import java.util.UUID

@Document(collection = "reviews")
@CompoundIndex(
    name = "prod-rev-id",
    unique = true,
    def = "{'productId': 1, 'reviewId': 1}"
)
data class ReviewEntity(
    val productId: Long,
    val reviewId: Long,
    var author: String,
    var subject: String,
    var content: String = "",
    @Version val version: Int = 0,
) {
    @Id lateinit var id: String
        private set
    @CreatedDate lateinit var created: LocalDateTime
        private set
    @LastModifiedDate lateinit var modified: LocalDateTime
        private set

    fun mapToApi(): Review = Review(
        productId,
        reviewId,
        author,
        subject,
        content
    )
}