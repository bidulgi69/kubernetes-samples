package kr.dove.product.composite

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.http.MediaType
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import routes.event.Event
import routes.product.Product
import routes.review.Review

@Service
class ProductCompositeIntegration(
    builder: WebClient.Builder,
    private val streamBridge: StreamBridge,
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val webClient: WebClient = builder.build()
    private val productServiceUri = "http://product"
    private val reviewServiceUri = "http://review"

    fun getProduct(productId: Long): Mono<Product> =
        webClient
            .get()
            .uri("$productServiceUri/product/$productId")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(Product::class.java)

    fun getReviews(productId: Long): Flux<Review> =
        webClient
            .get()
            .uri("$reviewServiceUri/review/$productId")
            .accept(MediaType.APPLICATION_NDJSON)
            .retrieve()
            .bodyToFlux(Review::class.java)


    fun <K, T> sendMessage(bindingName: String, event: Event<K, T>) {
        logger.debug("Sending a {} message...", event.type.name)
        streamBridge.send(
            bindingName,
            MessageBuilder.withPayload(event)
                .build()
        )
    }
}