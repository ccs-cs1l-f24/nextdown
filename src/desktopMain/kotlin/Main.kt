import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
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
                Surface(modifier = Modifier.fillMaxSize()) { HomePage() }
            }
        }
    }
}
