package model

import kotlinx.serialization.Serializable

@Serializable
data class Calendar(val events: List<Event>)
