import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.AppState
import org.jetbrains.skiko.wasm.onWasmReady
import org.w3c.dom.StorageEvent
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener

@OptIn(ExperimentalComposeUiApi::class, DelicateCoroutinesApi::class)
fun main() {
    val appState =
        MutableStateFlow(
            localStorage.getItem(stateStorageKey)?.let { data ->
                try {
                    Json.decodeFromString(data)
                } catch (e: IllegalArgumentException) {
                    AppState()
                }
            } ?: AppState()
        )
    window.addEventListener(
        "storage",
        object : EventListener {
            override fun handleEvent(event: Event) {
                (event as? StorageEvent)?.let {
                    if (it.key == stateStorageKey)
                        appState.value =
                            localStorage.getItem(stateStorageKey)?.let { data ->
                                try {
                                    Json.decodeFromString(data)
                                } catch (e: IllegalArgumentException) {
                                    AppState()
                                }
                            } ?: AppState()
                }
            }
        },
    )
    GlobalScope.launch {
        appState.collectLatest { state -> localStorage.setItem(stateStorageKey, Json.encodeToString(state)) }
    }
    onWasmReady {
        CanvasBasedWindow(canvasElementId = "app") {
            App(appState = appState.collectAsState().value, setAppState = { appState.value = it })
        }
    }
}
