package routes.composite

import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

interface ProductCompositeService {

    @PostMapping(
        value = ["/product-composite"],
        consumes = ["application/json"],
        produces = ["application/json"]
    )
    fun create(@RequestBody aggregate: ProductAggregate): Mono<Void>

    @DeleteMapping(
        value = ["/product-composite/{productId}"],
        produces = ["application/json"]
    )
    fun deleteByProductId(@PathVariable(name = "productId") productId: Long): Mono<Void>

    @GetMapping(
        value = ["/product-composite/{productId}"],
        produces = ["application/json"]
    )
    fun getByProductId(@PathVariable(name = "productId") productId: Long): Mono<ProductAggregate>
}