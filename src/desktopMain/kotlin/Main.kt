import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() {
    application {
        Window(onCloseRequest = ::exitApplication, title = "NextDown") {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Text(greeting, modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}
