package kr.dove.product.composite

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers

@SpringBootApplication
class ProductCompositeServiceApplication(
	@Value("\${app.threadPoolSize:10}") val threadPoolSize: Int,
	@Value("\${app.taskQueueSize:100}") val taskQueueSize: Int,
) {

	@Bean
	fun publishEventScheduler(): Scheduler =
		Schedulers.newBoundedElastic(threadPoolSize, taskQueueSize, "product-composite-pool")

	@Bean
	fun builder(): WebClient.Builder =
		WebClient.builder()
}

fun main(args: Array<String>) {
	runApplication<ProductCompositeServiceApplication>(*args)
}
