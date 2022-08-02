package kr.dove.product

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import routes.event.Event
import routes.event.EventType
import routes.product.Product
import routes.product.ProductService
import java.util.function.Consumer

@Configuration
class MessageProcessorConfiguration(
    private val productService: ProductService,
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Bean
    fun messageProcessor(): Consumer<Event<Long, Product>> {
        return Consumer<Event<Long, Product>> { evt ->
            logger.info("Process message created at {}...", evt.published)
            when (evt.type) {
                EventType.CREATE -> {
                    productService.create(evt.value)
                        .subscribe { logger.info("Receive create message {}", evt.key) }
                }
                EventType.DELETE -> {
                    productService.deleteByProductId(evt.key)
                        .subscribe { logger.info("Receive delete message {}", evt.key) }
                }
                else -> {
                    logger.warn("Incorrect event type {}!", evt.type.name)
                }
            }
        }
    }
}