package ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import model.Calendar
import org.jetbrains.compose.resources.vectorResource
import resources.Res
import resources.ic_add
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun HomePage() {
    Surface(modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val density = LocalDensity.current
            var dialog: Dialog? by rememberSaveable { mutableStateOf(null) }
            var currentTimeMs by rememberSaveable { mutableLongStateOf(Clock.System.now().toEpochMilliseconds()) }
            LaunchedEffect(true) {
                currentTimeMs = Clock.System.now().toEpochMilliseconds()
                while (true) {
                    delay(1000 - (currentTimeMs % 1000))
                    currentTimeMs = Clock.System.now().toEpochMilliseconds()
                }
            }
            var calendar by remember { mutableStateOf(Calendar(emptyList())) }
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
            LaunchedEffect(range) { scroll = scroll.coerceIn(range.first.toFloat()..range.last.toFloat()) }
            val nextEvent =
                @Composable {
                    events.second?.let {
                        EventCard(
                            it,
                            currentTimeMs,
                            1 - abs(scroll / constraints.maxHeight),
                            onClick = { dialog = Dialog.EditEvent(it.uid) },
                        )
                    } ?: Box(contentAlignment = Alignment.Center) { Text("No upcoming events. Try creating one!") }
                }
            val eventsBefore =
                @Composable {
                    events.first.first.forEach {
                        EventCard(it, currentTimeMs, 0f, onClick = { dialog = Dialog.EditEvent(it.uid) })
                    }
                }
            val eventsAfter =
                @Composable {
                    events.first.second.forEach {
                        EventCard(it, currentTimeMs, 0f, onClick = { dialog = Dialog.EditEvent(it.uid) })
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
                        .map {
                            it.measure(Constraints(minWidth = constraints.maxWidth, maxWidth = constraints.maxWidth))
                        }
                val eventsAfterPlaceables =
                    subcompose(++slotId) { eventsAfter() }
                        .map {
                            it.measure(Constraints(minWidth = constraints.maxWidth, maxWidth = constraints.maxWidth))
                        }
                range =
                    (-eventsAfterPlaceables.sumOf { it.height } -
                        with(density) {
                            (if (eventsAfterPlaceables.isNotEmpty()) 80 else 0).dp.roundToPx()
                        })..eventsBeforePlaceables.sumOf { it.height }
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
            ExtendedFloatingActionButton(
                text = { Text("New event", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                icon = { Icon(vectorResource(Res.drawable.ic_add), null) },
                onClick = { dialog = Dialog.CreateEvent },
                modifier =
                    Modifier.align(Alignment.BottomEnd).windowInsetsPadding(WindowInsets.systemBars).padding(16.dp),
            )
            AnimatedVisibility(dialog != null, enter = fadeIn(), exit = fadeOut(), modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier =
                        Modifier.fillMaxSize()
                            .alpha(0.75f)
                            .background(MaterialTheme.colorScheme.surfaceDim)
                            .pointerInput(true) {
                                detectTapGestures { dialog = null }
                                detectDragGestures(
                                    onDragEnd = { dialog = null },
                                    onDragCancel = { dialog = null },
                                    onDrag = { _, _ -> },
                                )
                            }
                )
            }
            AnimatedContent(
                targetState = dialog,
                transitionSpec = {
                    fadeIn(tween(200)) + slideInVertically { it / 16 } togetherWith
                        fadeOut(tween(200)) + slideOutVertically { -it / 8 }
                },
                modifier = Modifier.fillMaxSize(),
            ) { targetDialog ->
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    if (targetDialog != null) {
                        Card(
                            shape = MaterialTheme.shapes.large,
                            colors =
                                CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                                ),
                            modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars).padding(8.dp),
                        ) {
                            when (targetDialog) {
                                Dialog.CreateEvent ->
                                    CreateEventDialog(
                                        onClose = { dialog = null },
                                        onCreateEvent = { calendar = calendar.copy(events = calendar.events + it) },
                                    )
                                is Dialog.EditEvent ->
                                    EditEventDialog(
                                        event = calendar.events.first { it.uid == targetDialog.uid },
                                        onUpdateEvent = { editedEvent ->
                                            calendar =
                                                calendar.copy(
                                                    events =
                                                        calendar.events.map {
                                                            if (it.uid == editedEvent.uid) editedEvent else it
                                                        }
                                                )
                                        },
                                        onDelete = { dialog = Dialog.ConfirmDeleteEvent(it.uid) },
                                        onClose = { dialog = null },
                                    )
                                is Dialog.ConfirmDeleteEvent ->
                                    ConfirmDeleteEventDialog(
                                        event = remember { calendar.events.first { it.uid == targetDialog.uid } },
                                        onDelete = { event ->
                                            calendar =
                                                calendar.copy(
                                                    events = calendar.events.filterNot { it.uid == event.uid }
                                                )
                                        },
                                        onClose = { dialog = null },
                                    )
                            }
                        }
                    }
                }
            }
        }
    }
}
