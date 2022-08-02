package kr.dove.review

import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import routes.review.Review
import routes.review.ReviewService

@RestController
class ReviewServiceImpl(
    private val reactiveMongoTemplate: ReactiveMongoTemplate
) : ReviewService {

    override fun create(review: Review): Mono<Review> {
        return review.productId ?. let { productId ->
            reactiveMongoTemplate
                .save(ReviewEntity(
                    productId,
                    review.reviewId,
                    review.author,
                    review.subject,
                    review.content
                ))
                .flatMap {
                    Mono.just(
                        it.mapToApi()
                    )
                }
        } ?: run {
            Mono.error(
                IllegalArgumentException("Product id val is not present.")
            )
        }
    }

    override fun getByProductId(productId: Long): Flux<Review> {
        return reactiveMongoTemplate
            .find(
                Query.query(
                    Criteria.where("productId").`is`(productId)
                ),
                ReviewEntity::class.java
            )
            .flatMap {
                Mono.just(
                    it.mapToApi()
                )
            }
    }

    override fun deleteByProductId(productId: Long): Flux<Long> {
        return reactiveMongoTemplate
            .findAllAndRemove(
                Query.query(
                    Criteria.where("productId").`is`(productId)
                ),
                ReviewEntity::class.java
            )
            .flatMap {
                Mono.just(
                    it.reviewId
                )
            }
    }
}