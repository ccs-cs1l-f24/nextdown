import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import theme.FontFamilies
import theme.Inter
import theme.JetBrainsMono
import theme.LocalFontFamilies
import theme.defaultDarkTheme
import theme.defaultLightTheme
import theme.getTypography
import ui.HomePage

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow(canvasElementId = "app") {
        CompositionLocalProvider(LocalFontFamilies provides FontFamilies(regular = Inter, mono = JetBrainsMono)) {
            MaterialTheme(
                colorScheme = if (isSystemInDarkTheme()) defaultDarkTheme else defaultLightTheme,
                typography = getTypography(LocalFontFamilies.current.regular),
            ) {
                HomePage()
            }
        }
    }
}
