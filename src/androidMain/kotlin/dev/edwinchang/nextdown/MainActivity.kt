package dev.edwinchang.nextdown

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import theme.FontFamilies
import theme.Inter
import theme.JetBrainsMono
import theme.LocalFontFamilies
import theme.defaultDarkTheme
import theme.defaultLightTheme
import theme.getTypography
import ui.HomePage

class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewModel.load(this)
        setContent {
            val state by viewModel.state.collectAsStateWithLifecycle()
            CompositionLocalProvider(LocalFontFamilies provides FontFamilies(regular = Inter, mono = JetBrainsMono)) {
                val useDarkTheme = isSystemInDarkTheme()
                val useDynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                val colorScheme =
                    if (useDynamicColor) {
                        if (useDarkTheme) dynamicDarkColorScheme(LocalContext.current)
                        else dynamicLightColorScheme(LocalContext.current)
                    } else {
                        if (useDarkTheme) defaultDarkTheme else defaultLightTheme
                    }
                val view = LocalView.current
                if (!view.isInEditMode) {
                    SideEffect {
                        WindowCompat.getInsetsController((view.context as Activity).window, view)
                            .isAppearanceLightStatusBars = !useDarkTheme
                    }
                }
                MaterialTheme(
                    colorScheme = colorScheme,
                    typography = getTypography(LocalFontFamilies.current.regular),
                ) {
                    HomePage(state, setAppState = { viewModel.state.value = it })
                }
            }
        }
    }
}
