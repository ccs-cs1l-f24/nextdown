package dev.edwinchang.nextdown

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.AppState
import java.io.File

class MainActivityViewModel : ViewModel() {
    var state = MutableStateFlow(AppState())

    private val Context.stateFile
        get() = File(filesDir, "state.json")

    fun load(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            state.value =
                if (context.stateFile.exists()) {
                    try {
                        Json.decodeFromString(context.stateFile.readText())
                    } catch (e: IllegalArgumentException) {
                        AppState()
                    }
                } else AppState()
            state.collectLatest { state ->
                delay(100)
                context.stateFile.writeText(Json.encodeToString(state))
            }
        }
    }
}
