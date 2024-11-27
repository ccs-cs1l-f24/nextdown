import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.io.File
import java.io.IOException
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.AppState
import theme.FontFamilies
import theme.Inter
import theme.JetBrainsMono
import theme.LocalFontFamilies
import theme.defaultDarkTheme
import theme.defaultLightTheme
import theme.getTypography
import ui.HomePage

val stateFile =
    if (System.getProperty("os.name").contains("win", ignoreCase = true)) {
        File(System.getenv("APPDATA"), "NextDown/state.json")
    } else if (System.getenv("XDG_DATA_HOME") != null) {
        File(System.getenv("XDG_DATA_HOME"), "nextdown/state.json")
    } else {
        File(System.getProperty("user.home"), ".local/share/nextdown/state.json")
    }

@OptIn(DelicateCoroutinesApi::class)
fun main() {
    val appState =
        MutableStateFlow(
            try {
                stateFile.readText().let { Json.decodeFromString(it) }
            } catch (e: Throwable) {
                when (e) {
                    is IllegalArgumentException,
                    is IOException,
                    is RuntimeException -> AppState()
                    else -> throw e
                }
            }
        )
    GlobalScope.launch(Dispatchers.IO) {
        appState.collectLatest { state ->
            delay(100)
            try {
                stateFile.parentFile.mkdirs()
                stateFile.writeText(Json.encodeToString(state))
            } catch (e: Throwable) {
                when (e) {
                    is IllegalArgumentException,
                    is IOException,
                    is RuntimeException -> {}
                    else -> throw e
                }
            }
        }
    }
    application {
        Window(onCloseRequest = ::exitApplication, title = "NextDown") {
            CompositionLocalProvider(LocalFontFamilies provides FontFamilies(regular = Inter, mono = JetBrainsMono)) {
                MaterialTheme(
                    colorScheme = if (isSystemInDarkTheme()) defaultDarkTheme else defaultLightTheme,
                    typography = getTypography(LocalFontFamilies.current.regular),
                ) {
                    HomePage(appState = appState.collectAsState().value, setAppState = { appState.value = it })
                }
            }
        }
    }
}
