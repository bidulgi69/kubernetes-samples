package kr.dove.product

import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import routes.product.Product
import routes.product.ProductService
import java.util.UUID

@RestController
class ProductServiceImpl(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
) : ProductService {

    override fun create(product: Product): Mono<Product> {
        return reactiveMongoTemplate
            .save(ProductEntity(
                UUID.randomUUID(),
                product.productId,
                product.name,
                product.cost
            ))
            .flatMap {
                Mono.just(
                    it.mapToApi()
                )
            }
    }

    override fun getByProductId(productId: Long): Mono<Product> {
        return reactiveMongoTemplate
            .findOne(
                Query.query(
                    Criteria.where("productId").`is`(productId)
                ),
                ProductEntity::class.java
            )
            .flatMap {
                Mono.just(
                    it.mapToApi()
                )
            }
    }

    override fun deleteByProductId(productId: Long): Mono<Long> {
        return reactiveMongoTemplate
            .findAndRemove(
                Query.query(
                    Criteria.where("productId").`is`(productId)
                ),
                ProductEntity::class.java
            )
            .flatMap {
                Mono.just(
                    it.productId
                )
            }
    }

    override fun list(offset: Long, size: Long): Flux<Product> {
        return reactiveMongoTemplate.findAll(ProductEntity::class.java)
            .skip(offset * size)
            .take(size)
            .flatMap {
                Mono.just(
                    it.mapToApi()
                )
            }
    }
}
