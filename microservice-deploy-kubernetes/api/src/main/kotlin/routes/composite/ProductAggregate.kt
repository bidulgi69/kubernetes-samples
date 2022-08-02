package routes.composite

import routes.product.Product
import routes.review.Review

data class ProductAggregate(
    val product: Product,
    val reviews: List<Review>,
)