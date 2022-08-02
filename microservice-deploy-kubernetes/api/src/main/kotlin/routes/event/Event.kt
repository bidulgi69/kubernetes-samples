package routes.event

import java.time.LocalDateTime

data class Event<K, T>(
    val type: EventType,
    val key: K,
    val value: T,
    val published: LocalDateTime = LocalDateTime.now(),
)

enum class EventType {
    CREATE,
    DELETE
}