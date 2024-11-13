package theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import resources.Res
import resources.inter_black
import resources.inter_bold
import resources.inter_extrabold
import resources.inter_extralight
import resources.inter_light
import resources.inter_medium
import resources.inter_regular
import resources.inter_semibold
import resources.inter_thin
import resources.jbmono_bold
import resources.jbmono_extrabold
import resources.jbmono_extralight
import resources.jbmono_light
import resources.jbmono_medium
import resources.jbmono_regular
import resources.jbmono_semibold
import resources.jbmono_thin

actual val Inter
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

actual val JetBrainsMono
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
