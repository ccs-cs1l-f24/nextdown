package model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val uid: String,
    val dtStart: LocalDateTime,
    val dtEnd: LocalDateTime,
    val summary: String?,
    val description: String?,
)
