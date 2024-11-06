package model

import kotlinx.datetime.LocalDateTime

data class Event(
    val uid: String,
    val dtStart: LocalDateTime,
    val dtEnd: LocalDateTime,
    val summary: String?,
    val description: String?,
)
