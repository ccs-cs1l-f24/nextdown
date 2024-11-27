package model

import kotlinx.serialization.Serializable

@Serializable
data class AppState(val calendar: Calendar = Calendar(events = emptyList()))
