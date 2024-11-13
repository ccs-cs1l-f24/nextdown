package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import model.Event
import theme.LocalFontFamilies

@Composable
fun EventCard(
    event: Event,
    currentTimeMs: Long,
    emphasisState: Float = 0f,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(onClick = onClick, modifier = modifier.padding(16.dp)) {
        if (emphasisState < 0.6) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier =
                    Modifier.fillMaxWidth()
                        .then(if (emphasisState == 0f) Modifier else Modifier.fillMaxHeight())
                        .padding(24.dp),
            ) {
                Column {
                    Text(event.summary ?: "[unnamed]", style = MaterialTheme.typography.titleMedium)
                    event.description?.let { Text(it, style = MaterialTheme.typography.bodyMedium) }
                }
                Countdown(event.dtStart.toInstant(TimeZone.currentSystemDefault()), currentTimeMs)
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().padding(24.dp),
            ) {
                Text(event.summary ?: "[unnamed]", style = MaterialTheme.typography.titleLarge)
                Countdown(event.dtStart.toInstant(TimeZone.currentSystemDefault()), currentTimeMs, fontSize = 48.sp)
                event.description?.let { Text(it, style = MaterialTheme.typography.bodyLarge) }
            }
        }
    }
}

@Composable
fun Countdown(time: Instant, currentTimeMs: Long, fontSize: TextUnit = 36.sp, modifier: Modifier = Modifier) {
    val currentTime = Instant.fromEpochMilliseconds(currentTimeMs)
    Text(
        (if (time > currentTime) time - currentTime else currentTime - time).toComponents {
            days,
            hours,
            minutes,
            seconds,
            _ ->
            buildString {
                if (time < currentTime) append("-")
                append(days.toString().padStart(2, '0'))
                append(':')
                append(hours.toString().padStart(2, '0'))
                append(':')
                append(minutes.toString().padStart(2, '0'))
                append(':')
                append(seconds.toString().padStart(2, '0'))
            }
        },
        fontSize = fontSize,
        fontFamily = LocalFontFamilies.current.mono,
        modifier = modifier,
    )
}
