import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import model.DTStartValue
import theme.Inter
import theme.JetBrainsMono
import theme.getTypography

fun main() {
    application {
        Window(onCloseRequest = ::exitApplication, title = "NextDown") {
            MaterialTheme(
                colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme(),
                typography = getTypography(Inter),
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        var currentTimeMs by rememberSaveable {
                            mutableLongStateOf(Clock.System.now().toEpochMilliseconds())
                        }
                        LaunchedEffect(true) {
                            currentTimeMs = Clock.System.now().toEpochMilliseconds()
                            while (true) {
                                delay(1000 - (currentTimeMs % 1000))
                                currentTimeMs = Clock.System.now().toEpochMilliseconds()
                            }
                        }
                        val event = testCalendar.events.first()
                        Text(
                            ((event.dtStart as DTStartValue.DateTime)
                                    .value
                                    .value
                                    .toInstant(TimeZone.currentSystemDefault()) -
                                    Instant.fromEpochMilliseconds(currentTimeMs))
                                .toComponents { days, hours, minutes, seconds, _ ->
                                    buildString {
                                        append(days.toString().padStart(2, '0'))
                                        append(':')
                                        append(hours.toString().padStart(2, '0'))
                                        append(':')
                                        append(minutes.toString().padStart(2, '0'))
                                        append(':')
                                        append(seconds.toString().padStart(2, '0'))
                                    }
                                },
                            fontSize = 40.sp,
                            fontFamily = JetBrainsMono,
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                }
            }
        }
    }
}
