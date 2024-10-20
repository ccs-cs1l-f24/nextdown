package theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import resources.*

val Inter
    @Composable
    get() =
        FontFamily(
            Font(Res.font.inter_regular, weight = FontWeight.Normal),
            Font(Res.font.inter_bold, weight = FontWeight.Bold),
            Font(Res.font.inter_semibold, weight = FontWeight.SemiBold),
            Font(Res.font.inter_medium, weight = FontWeight.Medium),
            Font(Res.font.inter_thin, weight = FontWeight.Thin),
            Font(Res.font.inter_extralight, weight = FontWeight.ExtraLight),
            Font(Res.font.inter_light, weight = FontWeight.Light),
            Font(Res.font.inter_extrabold, weight = FontWeight.ExtraBold),
            Font(Res.font.inter_black, weight = FontWeight.Black),
        )

val JetBrainsMono
    @Composable
    get() =
        FontFamily(
            Font(Res.font.jbmono_regular, weight = FontWeight.Normal),
            Font(Res.font.jbmono_bold, weight = FontWeight.Bold),
            Font(Res.font.jbmono_semibold, weight = FontWeight.SemiBold),
            Font(Res.font.jbmono_medium, weight = FontWeight.Medium),
            Font(Res.font.jbmono_thin, weight = FontWeight.Thin),
            Font(Res.font.jbmono_extralight, weight = FontWeight.ExtraLight),
            Font(Res.font.jbmono_light, weight = FontWeight.Light),
            Font(Res.font.jbmono_extrabold, weight = FontWeight.ExtraBold),
        )

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
