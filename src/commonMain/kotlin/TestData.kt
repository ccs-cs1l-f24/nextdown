import kotlin.time.Duration.Companion.minutes
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import model.Calendar
import model.DTEndValue
import model.DTStartValue
import model.Event
import types.VTCalAddress
import types.VTDateTime
import types.VTText
import types.VTUri
import kotlin.time.Duration.Companion.hours

val testCalendar =
    Clock.System.now().let { now ->
        fun ldt(instant: Instant) = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        Calendar(
            listOf(
                Event(
                    dtStamp =
                        VTDateTime(
                            Instant.fromEpochMilliseconds(1727766000000)
                                .toLocalDateTime(TimeZone.currentSystemDefault())
                        ),
                    uid = "test1",
                    dtStart = DTStartValue.DateTime(VTDateTime(ldt(now + 2.hours + 30.minutes))),
                    dtEnd = DTEndValue.DateTime(VTDateTime(ldt(now + 3.hours))),
                    organizer = VTCalAddress(VTUri("edwinchang@ucsb.edu")),
                    status = VTText("CONFIRMED"),
                    summary = VTText("Test event 1"),
                    description = VTText("This is a test event! How exciting."),
                    comment = VTText(""),
                )
            )
        )
    }
