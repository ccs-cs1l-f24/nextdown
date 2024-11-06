import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import theme.Inter
import theme.getTypography
import ui.HomePage

fun main() {
    application {
        Window(onCloseRequest = ::exitApplication, title = "NextDown") {
            MaterialTheme(
                colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme(),
                typography = getTypography(Inter),
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    HomePage()

                    //                    var scrollAmount by remember { mutableFloatStateOf(0f) }
                    //                    val scrollState = rememberScrollableState { it.also {
                    // scrollAmount += it } }
                    //                    BoxWithConstraints(
                    //                        modifier =
                    // Modifier.fillMaxSize().scrollable(scrollState, Orientation.Vertical)
                    //                    ) {
                    //                        Card(modifier =
                    // Modifier.align(Alignment.TopCenter).offset(y = scrollAmount.dp)) {
                    //                            Text(testCalendar.events[1].summary ?: "???")
                    //                            Text(testCalendar.events[1].description ?: "???")
                    //                        }
                    //                        Card(modifier =
                    // Modifier.align(Alignment.BottomCenter).offset(y = scrollAmount.dp)) {
                    //                            Text(testCalendar.events[2].summary ?: "???")
                    //                            Text(testCalendar.events[2].description ?: "???")
                    //                        }
                    //                        var currentTimeMs by rememberSaveable {
                    //
                    // mutableLongStateOf(Clock.System.now().toEpochMilliseconds())
                    //                        }
                    //                        LaunchedEffect(true) {
                    //                            currentTimeMs =
                    // Clock.System.now().toEpochMilliseconds()
                    //                            while (true) {
                    //                                delay(1000 - (currentTimeMs % 1000))
                    //                                currentTimeMs =
                    // Clock.System.now().toEpochMilliseconds()
                    //                            }
                    //                        }
                    //                        val event = testCalendar.events.first()
                    //                        Column(
                    //                            horizontalAlignment =
                    // Alignment.CenterHorizontally,
                    //                            verticalArrangement = Arrangement.spacedBy(16.dp),
                    //                            modifier =
                    //                                Modifier.align(Alignment.Center).alpha(1 -
                    // abs(scrollAmount / 200f).coerceIn(0f..1f)),
                    //                        ) {
                    //                            Text(testCalendar.events.first().summary ?: "???")
                    //                            Text(
                    //
                    // (event.dtStart.toInstant(TimeZone.currentSystemDefault()) -
                    //
                    // Instant.fromEpochMilliseconds(currentTimeMs))
                    //                                    .toComponents { days, hours, minutes,
                    // seconds, _ ->
                    //                                        buildString {
                    //                                            append(days.toString().padStart(2,
                    // '0'))
                    //                                            append(':')
                    //
                    // append(hours.toString().padStart(2, '0'))
                    //                                            append(':')
                    //
                    // append(minutes.toString().padStart(2, '0'))
                    //                                            append(':')
                    //
                    // append(seconds.toString().padStart(2, '0'))
                    //                                        }
                    //                                    },
                    //                                fontSize = 64.sp,
                    //                                fontFamily = JetBrainsMono,
                    //                            )
                    //                            Text(testCalendar.events.first().description ?:
                    // "???")
                    //                        }
                    //                        Text(scrollAmount.toString())
                    //                    }
                }
            }
        }
    }
}
