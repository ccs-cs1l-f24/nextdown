import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import theme.FontFamilies
import theme.Inter
import theme.JetBrainsMono
import theme.LocalFontFamilies
import theme.defaultDarkTheme
import theme.defaultLightTheme
import theme.getTypography
import ui.HomePage

fun main() {
    application {
        Window(onCloseRequest = ::exitApplication, title = "NextDown") {
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
}
