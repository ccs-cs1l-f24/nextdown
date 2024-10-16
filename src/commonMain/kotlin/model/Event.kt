package model

import types.VTCalAddress
import types.VTDate
import types.VTDateTime
import types.VTText

data class Event(
    val dtStamp: VTDateTime,
    val uid: String,
    val dtStart: DTStartValue,
    val dtEnd: DTEndValue?,
    val organizer: VTCalAddress?,
    val status: VTText?,
    val summary: VTText?,
    val description: VTText?,
    val comment: VTText?,
)

sealed interface DTStartValue {
    data class DateTime(val value: VTDateTime) : DTStartValue

    data class Date(val value: VTDate) : DTStartValue
}

sealed interface DTEndValue {
    data class DateTime(val value: VTDateTime) : DTEndValue

    data class Date(val value: VTDate) : DTEndValue
}
