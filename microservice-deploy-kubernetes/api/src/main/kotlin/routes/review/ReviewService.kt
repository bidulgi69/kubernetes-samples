package routes.review

import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ReviewService {

    @PostMapping(
        value = ["/review"],
        consumes = ["application/json"],
        produces = ["application/json"]
    )
    fun create(@RequestBody review: Review): Mono<Review>

    @GetMapping(
        value = ["/review/{productId}"],
        produces = ["application/x-ndjson"]
    )
    fun getByProductId(@PathVariable(name = "productId") productId: Long): Flux<Review>

    @DeleteMapping(
        value = ["/review/{productId}"],
        produces = ["application/x-ndjson"]
    )
    fun deleteByProductId(@PathVariable(name = "productId") productId: Long): Flux<Long>
}