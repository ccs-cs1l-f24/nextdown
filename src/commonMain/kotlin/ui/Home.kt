package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Before:")
                events.first.first.forEach { Text(it.uid) }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Next:")
                events.second?.let { Text(it.uid) }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("After:")
                events.first.second.forEach { Text(it.uid) }
            }
        }
    }
}
