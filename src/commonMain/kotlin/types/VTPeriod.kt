package types

sealed interface VTPeriod : ValueType {
    data class Explicit(val start: VTDateTime, val end: VTDateTime) : VTPeriod

    data class Start(val start: VTDateTime, val duration: VTDuration) : VTPeriod
}
