import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import model.Calendar
import model.Event
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

val testCalendar =
    Clock.System.now().let { now ->
        fun ldt(instant: Instant) = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        Calendar(
            listOf(
                Event(
                    uid = "test1",
                    dtStart = ldt(now + 2.hours),
                    dtEnd = ldt(now + 3.hours),
                    summary = "Test event 1",
                    description = "This is a test event! How exciting.",
                ),
                Event(
                    uid = "test2",
                    dtStart = ldt(now - 2.days),
                    dtEnd = ldt(now - 1.days),
                    summary = "Test event 2",
                    description = "This is a test event! How exciting.",
                ),
                Event(
                    uid = "test3",
                    dtStart = ldt(now + 1.days),
                    dtEnd = ldt(now + 2.days),
                    summary = "Test event 3",
                    description = "This is a test event! How exciting.",
                ),
                Event(
                    uid = "test4",
                    dtStart = ldt(now + 2.days),
                    dtEnd = ldt(now + 3.days),
                    summary = "Test event 4",
                    description = "This is a test event! How exciting.",
                ),
            )
        )
    }
