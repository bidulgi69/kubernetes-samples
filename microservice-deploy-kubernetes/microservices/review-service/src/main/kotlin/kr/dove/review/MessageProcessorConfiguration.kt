package kr.dove.review

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import routes.event.Event
import routes.event.EventType
import routes.review.Review
import routes.review.ReviewService
import java.util.function.Consumer

@Configuration
class MessageProcessorConfiguration(
    private val reviewService: ReviewService
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Bean
    fun messageProcessor(): Consumer<Event<Long, Review>> {
        return Consumer<Event<Long, Review>> { evt ->
            logger.info("Process message created at {}...", evt.published)
            when (evt.type) {
                EventType.CREATE -> {
                    reviewService.create(evt.value.apply {
                        this.productId = evt.key
                    })
                        .subscribe { logger.info("Receive create message {}", evt.key) }
                }
                EventType.DELETE -> {
                    reviewService.deleteByProductId(evt.key)
                        .subscribe { logger.info("Receive delete message {}", evt.key) }
                }
                else -> {
                    logger.warn("Incorrect event type {}!", evt.type.name)
                }
            }
        }
    }
}