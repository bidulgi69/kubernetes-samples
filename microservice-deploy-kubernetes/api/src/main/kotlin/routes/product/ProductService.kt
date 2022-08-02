package routes.product

import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ProductService {

    @PostMapping(
        value = ["/product"],
        consumes = ["application/json"],
        produces = ["application/json"]
    )
    fun create(@RequestBody product: Product): Mono<Product>

    @GetMapping(
        value = ["/product/{productId}"],
        produces = ["application/json"]
    )
    fun getByProductId(@PathVariable(name = "productId") productId: Long): Mono<Product>

    @DeleteMapping(
        value = ["/product/{productId}"],
        produces = ["application/json"]
    )
    fun deleteByProductId(@PathVariable(name = "productId") productId: Long): Mono<Long>

    @GetMapping(
        value = ["/product/{offset}/{size}"],
        produces = ["application/x-ndjson"]
    )
    fun list(
        @PathVariable(name = "offset") offset: Long,
        @PathVariable(name = "size") size: Long
    ): Flux<Product>
}