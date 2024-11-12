package ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import model.Event
import org.jetbrains.compose.resources.vectorResource
import resources.Res
import resources.ic_close
import resources.ic_delete
import resources.ic_done

sealed interface Dialog {
    data object CreateEvent : Dialog

    data class EditEvent(val uid: String) : Dialog

    data class ConfirmDeleteEvent(val uid: String) : Dialog
}

@OptIn(ExperimentalUuidApi::class)
@Composable
fun CreateEventDialog(onCreateEvent: (Event) -> Unit, onClose: () -> Unit, modifier: Modifier = Modifier) {
    var event by remember {
        val now = Clock.System.now().toEpochMilliseconds()
        val oneHr = 1.hours.inWholeMilliseconds
        mutableStateOf(
            Event(
                uid = Uuid.random().toString(),
                dtStart =
                    Instant.fromEpochMilliseconds(now - now % oneHr + oneHr)
                        .toLocalDateTime(TimeZone.currentSystemDefault()),
                dtEnd =
                    Instant.fromEpochMilliseconds(now - now % oneHr + oneHr * 2)
                        .toLocalDateTime(TimeZone.currentSystemDefault()),
                summary = null,
                description = null,
            )
        )
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier.verticalScroll(rememberScrollState()).widthIn(0.dp, 480.dp).padding(vertical = 24.dp),
    ) {
        Text(
            "Create event",
            style = MaterialTheme.typography.headlineMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 24.dp),
        )
        EventEditing(event, { event = it }, modifier = Modifier.padding(horizontal = 24.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
            modifier =
                Modifier.horizontalScroll(rememberScrollState(), reverseScrolling = true)
                    .align(Alignment.End)
                    .padding(horizontal = 24.dp),
        ) {
            OutlinedButton(onClick = onClose) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(vectorResource(Res.drawable.ic_close), null)
                    Text("Cancel")
                }
            }
            Button(
                onClick = {
                    onCreateEvent(event)
                    onClose()
                }
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(vectorResource(Res.drawable.ic_done), null)
                    Text("Create")
                }
            }
        }
    }
}

@Composable
fun EditEventDialog(
    event: Event,
    onUpdateEvent: (Event) -> Unit,
    onDelete: (Event) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var editedEvent by remember { mutableStateOf(event) }
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier.verticalScroll(rememberScrollState()).widthIn(0.dp, 480.dp).padding(vertical = 24.dp),
    ) {
        Text(
            "Edit event",
            style = MaterialTheme.typography.headlineMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 24.dp),
        )
        OutlinedButton(onClick = { onDelete(event) }, modifier = Modifier.padding(horizontal = 24.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(vectorResource(Res.drawable.ic_delete), null)
                Text("Delete event")
            }
        }
        EventEditing(editedEvent, { editedEvent = it }, modifier = Modifier.padding(horizontal = 24.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
            modifier =
                Modifier.horizontalScroll(rememberScrollState(), reverseScrolling = true)
                    .align(Alignment.End)
                    .padding(horizontal = 24.dp),
        ) {
            OutlinedButton(onClick = onClose) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(vectorResource(Res.drawable.ic_close), null)
                    Text("Cancel")
                }
            }
            Button(
                onClick = {
                    onUpdateEvent(editedEvent)
                    onClose()
                }
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(vectorResource(Res.drawable.ic_done), null)
                    Text("Done")
                }
            }
        }
    }
}

@Composable
fun ConfirmDeleteEventDialog(
    event: Event,
    onDelete: (Event) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier.verticalScroll(rememberScrollState()).widthIn(0.dp, 480.dp).padding(vertical = 24.dp),
    ) {
        Text(
            "Confirm",
            style = MaterialTheme.typography.headlineMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 24.dp),
        )
        Text(
            "Are you sure you want to delete the event \"${event.summary ?: "[unnamed]"}\"? This can't be undone.",
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 24.dp),
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
            modifier =
                Modifier.horizontalScroll(rememberScrollState(), reverseScrolling = true)
                    .align(Alignment.End)
                    .padding(horizontal = 24.dp),
        ) {
            OutlinedButton(onClick = onClose) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(vectorResource(Res.drawable.ic_close), null)
                    Text("Cancel")
                }
            }
            Button(
                onClick = {
                    onDelete(event)
                    onClose()
                }
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(vectorResource(Res.drawable.ic_done), null)
                    Text("Yes, delete event")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EventEditing(state: Event, setState: (Event) -> Unit, modifier: Modifier = Modifier) {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp), modifier = modifier.animateContentSize()) {
        OutlinedTextField(
            value = state.summary ?: "",
            onValueChange = { setState(state.copy(summary = it.takeIf { it.isNotBlank() })) },
            label = { Text("Summary") },
        )
        OutlinedTextField(
            value = state.description ?: "",
            onValueChange = { setState(state.copy(description = it.takeIf { it.isNotBlank() })) },
            label = { Text("Description") },
        )
        Text("Start", style = MaterialTheme.typography.titleSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
        var showStartDatePicker by rememberSaveable { mutableStateOf(false) }
        Text(
            state.dtStart.toJavaLocalDateTime().format(DateTimeFormatter.ofPattern("M/d/yy")),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (showStartDatePicker) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier =
                Modifier.clip(MaterialTheme.shapes.small)
                    .clickable { showStartDatePicker = !showStartDatePicker }
                    .padding(8.dp),
        )
        val startDatePickerState =
            rememberDatePickerState(
                initialSelectedDateMillis = state.dtStart.toInstant(TimeZone.UTC).toEpochMilliseconds()
            )
        if (showStartDatePicker) DatePicker(startDatePickerState)
        var showStartTimePicker by rememberSaveable { mutableStateOf(false) }
        Text(
            state.dtStart.toJavaLocalDateTime().format(DateTimeFormatter.ofPattern("h:mm a")),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (showStartTimePicker) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier =
                Modifier.clip(MaterialTheme.shapes.small)
                    .clickable { showStartTimePicker = !showStartTimePicker }
                    .padding(8.dp),
        )
        val startTimePickerState = rememberTimePickerState(state.dtStart.hour, state.dtStart.minute)
        if (showStartTimePicker) TimePicker(startTimePickerState, layoutType = TimePickerLayoutType.Vertical)
        LaunchedEffect(
            startDatePickerState.selectedDateMillis,
            startTimePickerState.hour,
            startTimePickerState.minute,
        ) {
            startDatePickerState.selectedDateMillis?.let {
                setState(
                    state.copy(
                        dtStart =
                            (Instant.fromEpochMilliseconds(it) +
                                    startTimePickerState.hour.hours +
                                    startTimePickerState.minute.minutes)
                                .toLocalDateTime(TimeZone.UTC)
                    )
                )
            }
        }
        Text("End", style = MaterialTheme.typography.titleSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
        var showEndDatePicker by rememberSaveable { mutableStateOf(false) }
        Text(
            state.dtEnd.toJavaLocalDateTime().format(DateTimeFormatter.ofPattern("M/d/yy")),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (showEndDatePicker) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier =
                Modifier.clip(MaterialTheme.shapes.small)
                    .clickable { showEndDatePicker = !showEndDatePicker }
                    .padding(8.dp),
        )
        val endDatePickerState =
            rememberDatePickerState(
                initialSelectedDateMillis = state.dtEnd.toInstant(TimeZone.UTC).toEpochMilliseconds()
            )
        if (showEndDatePicker) DatePicker(endDatePickerState)
        var showEndTimePicker by rememberSaveable { mutableStateOf(false) }
        Text(
            state.dtEnd.toJavaLocalDateTime().format(DateTimeFormatter.ofPattern("h:mm a")),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (showEndTimePicker) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier =
                Modifier.clip(MaterialTheme.shapes.small)
                    .clickable { showEndTimePicker = !showEndTimePicker }
                    .padding(8.dp),
        )
        val endTimePickerState = rememberTimePickerState(state.dtEnd.hour, state.dtEnd.minute)
        if (showEndTimePicker) TimePicker(endTimePickerState, layoutType = TimePickerLayoutType.Vertical)
        LaunchedEffect(endDatePickerState.selectedDateMillis, endTimePickerState.hour, endTimePickerState.minute) {
            endDatePickerState.selectedDateMillis?.let {
                setState(
                    state.copy(
                        dtEnd =
                            (Instant.fromEpochMilliseconds(it) +
                                    endTimePickerState.hour.hours +
                                    endTimePickerState.minute.minutes)
                                .toLocalDateTime(TimeZone.UTC)
                    )
                )
            }
        }
    }
}
