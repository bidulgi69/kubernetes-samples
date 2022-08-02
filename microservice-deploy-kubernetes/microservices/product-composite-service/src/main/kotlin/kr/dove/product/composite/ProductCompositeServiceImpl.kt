package kr.dove.product.composite

import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Scheduler
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2
import routes.composite.ProductAggregate
import routes.composite.ProductCompositeService
import routes.event.Event
import routes.event.EventType
import routes.product.Product
import routes.review.Review

@RestController
class ProductCompositeServiceImpl(
    private val integration: ProductCompositeIntegration,
    private val publishEventScheduler: Scheduler,
) : ProductCompositeService {

    override fun create(aggregate: ProductAggregate): Mono<Void> {
        return Mono.fromRunnable<Void?> {
            integration.sendMessage("products-out-0", Event(
                EventType.CREATE,
                aggregate.product.productId,
                aggregate.product
            ))

            val productId = aggregate.product.productId
            aggregate.reviews
                .forEach { rev ->
                    integration.sendMessage("reviews-out-0", Event(
                        EventType.CREATE,
                        productId,
                        rev
                    ))
                }
        }.subscribeOn(publishEventScheduler)
    }

    override fun deleteByProductId(productId: Long): Mono<Void> {
        return Mono.fromRunnable<Void?> {
            integration.sendMessage("products-out-0", Event(
                EventType.DELETE,
                productId,
                productId
            ))

            integration.sendMessage("reviews-out-0", Event(
                EventType.DELETE,
                productId,
                productId
            ))
        }.subscribeOn(publishEventScheduler)
    }

    override fun getByProductId(productId: Long): Mono<ProductAggregate> {
        val product: Mono<Product> = integration.getProduct(productId)
        val reviews: Flux<Review> = integration.getReviews(productId)

        return Mono.zip(
            product,
            reviews.collectList()
        ).flatMap {
            val (p, r) = it
            Mono.just(
                ProductAggregate(
                    p,
                    r
                )
            )
        }
    }
}