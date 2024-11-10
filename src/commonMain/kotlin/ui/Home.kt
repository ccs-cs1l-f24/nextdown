package ui

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import model.Calendar
import model.Event

@Composable
fun HomePage() {
    Surface(modifier = Modifier.fillMaxSize()) {
        val appOpened = remember { Clock.System.now() }
        var currentTimeMs by rememberSaveable { mutableLongStateOf(Clock.System.now().toEpochMilliseconds()) }
        LaunchedEffect(true) {
            currentTimeMs = Clock.System.now().toEpochMilliseconds()
            while (true) {
                delay(1000 - (currentTimeMs % 1000))
                currentTimeMs = Clock.System.now().toEpochMilliseconds()
            }
        }
        fun ldt(instant: Instant) = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        var calendar by remember {
            mutableStateOf(
                Calendar(
                    listOf(
                        Event(
                            uid = "test2",
                            dtStart = ldt(appOpened + 3.seconds),
                            dtEnd = ldt(appOpened + 3.hours),
                            summary = "Test event 2",
                            description = "This is a test event! How exciting.",
                        ),
                        Event(
                            uid = "test1",
                            dtStart = ldt(appOpened - 2.days),
                            dtEnd = ldt(appOpened - 1.days),
                            summary = "Test event 1",
                            description = "This is a test event! How exciting.",
                        ),
                        Event(
                            uid = "test3",
                            dtStart = ldt(appOpened + 1.days),
                            dtEnd = ldt(appOpened + 2.days),
                            summary = "Test event 3",
                            description = "This is a test event! How exciting.",
                        ),
                        Event(
                            uid = "test4",
                            dtStart = ldt(appOpened + 2.days),
                            dtEnd = ldt(appOpened + 3.days),
                            summary = "Test event 4",
                            description = "This is a test event! How exciting.",
                        ),
                    )
                )
            )
        }
        val events by derivedStateOf {
            calendar.events
                .sortedBy { it.dtStart }
                .partition {
                    it.dtStart.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds() < currentTimeMs
                }
                .let { (before, after) -> before to after.drop(1) to after.firstOrNull() }
        }
        var scroll by remember { mutableFloatStateOf(0f) }
        var range by remember { mutableStateOf(0..0) }
        val nextEvent =
            @Composable {
                Card(modifier = Modifier.padding(8.dp)) { Text("base element", modifier = Modifier.padding(24.dp)) }
            }
        val eventsBefore =
            @Composable {
                repeat(5) {
                    OutlinedCard(modifier = Modifier.padding(8.dp)) {
                        Text("before: element $it", modifier = Modifier.padding(24.dp))
                    }
                }
            }
        val eventsAfter =
            @Composable {
                repeat(10) {
                    OutlinedCard(modifier = Modifier.padding(8.dp)) {
                        Text("after: element $it", modifier = Modifier.padding(24.dp))
                    }
                }
            }
        SubcomposeLayout(
            modifier =
                Modifier.fillMaxSize()
                    .scrollable(
                        rememberScrollableState { delta ->
                            val old = scroll
                            scroll = (scroll + delta).coerceIn(range.first.toFloat()..range.last.toFloat())
                            scroll - old
                        },
                        orientation = Orientation.Vertical,
                    )
        ) { constraints ->
            var slotId = 0
            val nextEventHeight = subcompose(++slotId) { nextEvent() }.first().measure(Constraints()).height
            val nextEventFinalHeight =
                (scroll / constraints.maxHeight *
                        (0.5f * constraints.maxHeight - nextEventHeight) *
                        (if (scroll <= 0) 1 else -1) + 0.5f * constraints.maxHeight)
                    .roundToInt()
                    .coerceAtLeast(0)
            val nextEventPlaceable =
                subcompose(++slotId) { nextEvent() }
                    .first()
                    .measure(
                        Constraints(
                            minWidth = constraints.maxWidth,
                            maxWidth = constraints.maxWidth,
                            minHeight = nextEventFinalHeight,
                            maxHeight = nextEventFinalHeight,
                        )
                    )
            val eventsBeforePlaceables =
                subcompose(++slotId) { eventsBefore() }
                    .map { it.measure(Constraints(minWidth = constraints.maxWidth, maxWidth = constraints.maxWidth)) }
            val eventsAfterPlaceables =
                subcompose(++slotId) { eventsAfter() }
                    .map { it.measure(Constraints(minWidth = constraints.maxWidth, maxWidth = constraints.maxWidth)) }
            range = -eventsAfterPlaceables.sumOf { it.height }..eventsBeforePlaceables.sumOf { it.height }
            layout(width = constraints.maxWidth, height = constraints.maxHeight) {
                nextEventPlaceable.place(
                    0,
                    ((if (scroll <= 0)
                            scroll / constraints.maxHeight * (0.25f * constraints.maxHeight + nextEventHeight)
                        else scroll * 0.75f) + 0.25f * constraints.maxHeight)
                        .roundToInt(),
                )
                var y = scroll.roundToInt()
                eventsBeforePlaceables.asReversed().forEach {
                    y -= it.height
                    it.place(0, y)
                }
                y = constraints.maxHeight + scroll.roundToInt()
                eventsAfterPlaceables.forEach {
                    it.place(0, y)
                    y += it.height
                }
            }
        }
    }
}
