package routes.product

data class Product(
    val name: String,
    val productId: Long,
    val cost: Int,
    val version: Int? = null,
)
