package theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

data class FontFamilies(val regular: FontFamily, val mono: FontFamily)

val LocalFontFamilies = compositionLocalOf<FontFamilies> { error("font families not provided") }

expect val Inter: FontFamily

expect val JetBrainsMono: FontFamily

@Composable
fun getTypography(fontFamily: FontFamily) =
    with(Typography()) {
        Typography(
            displayLarge = displayLarge.copy(fontFamily = fontFamily, fontWeight = FontWeight.Bold),
            displayMedium = displayMedium.copy(fontFamily = fontFamily, fontWeight = FontWeight.Bold),
            displaySmall = displaySmall.copy(fontFamily = fontFamily, fontWeight = FontWeight.Bold),
            headlineLarge = headlineLarge.copy(fontFamily = fontFamily, fontWeight = FontWeight.SemiBold),
            headlineMedium = headlineMedium.copy(fontFamily = fontFamily, fontWeight = FontWeight.SemiBold),
            headlineSmall = headlineSmall.copy(fontFamily = fontFamily, fontWeight = FontWeight.SemiBold),
            titleLarge = titleLarge.copy(fontFamily = fontFamily, fontWeight = FontWeight.Medium),
            titleMedium = titleMedium.copy(fontFamily = fontFamily, fontWeight = FontWeight.Medium),
            titleSmall = titleSmall.copy(fontFamily = fontFamily, fontWeight = FontWeight.Medium),
            bodyLarge = bodyLarge.copy(fontFamily = fontFamily, fontWeight = FontWeight.Normal),
            bodyMedium = bodyMedium.copy(fontFamily = fontFamily, fontWeight = FontWeight.Normal),
            bodySmall = bodySmall.copy(fontFamily = fontFamily, fontWeight = FontWeight.Normal),
            labelLarge = labelLarge.copy(fontFamily = fontFamily, fontWeight = FontWeight.Medium),
            labelMedium = labelMedium.copy(fontFamily = fontFamily, fontWeight = FontWeight.Medium),
            labelSmall = labelSmall.copy(fontFamily = fontFamily, fontWeight = FontWeight.Medium),
        )
    }
