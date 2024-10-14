package types

import kotlinx.datetime.DayOfWeek

data class VTRecurrenceRule(
    val freq: String,
    val until: UntilValue,
    val count: Int,
    val interval: Int,
    val bySecond: List<Int>,
    val byMinute: List<Int>,
    val byHour: List<Int>,
    val byDay: List<DayOfWeek>,
    val byMonthDay: List<Int>,
    val byYearDay: List<Int>,
    val byWeekNo: List<Int>,
    val byMonth: List<Int>,
    val bySetPos: List<Int>,
    val workStart: DayOfWeek,
)

sealed class UntilValue {
    data class Date(val value: VTDate) : UntilValue()

    data class DateTime(val value: VTDateTime) : UntilValue()
}
