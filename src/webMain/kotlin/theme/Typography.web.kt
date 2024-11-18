package theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import resources.Res

@OptIn(DelicateCoroutinesApi::class)
actual val Inter: FontFamily
    @Composable
    get() {
        var fontFamily: FontFamily? by remember { mutableStateOf(null) }
        var fontFamilyLoaded by remember { mutableStateOf(false) }
        LaunchedEffect(true) {
            if (!fontFamilyLoaded) {
                GlobalScope.launch { fontFamily = getInterWeb() }
                fontFamilyLoaded = true
            }
        }
        return fontFamily ?: FontFamily.SansSerif
    }

@OptIn(ExperimentalResourceApi::class)
private suspend fun getInterWeb(): FontFamily {
    suspend operator fun String.invoke() = Res.readBytes("font/$this")
    val regular = "inter_regular.ttf"()
    val bold = "inter_bold.ttf"()
    val semibold = "inter_semibold.ttf"()
    val medium = "inter_medium.ttf"()
    return FontFamily(
        Font("inter_regular", regular, weight = FontWeight.Normal),
        Font("inter_bold", bold, weight = FontWeight.Bold),
        Font("inter_semibold", semibold, weight = FontWeight.SemiBold),
        Font("inter_medium", medium, weight = FontWeight.Medium),
        Font("inter_thin", regular, weight = FontWeight.Thin),
        Font("inter_extralight", regular, weight = FontWeight.ExtraLight),
        Font("inter_light", regular, weight = FontWeight.Light),
        Font("inter_extrabold", bold, weight = FontWeight.ExtraBold),
        Font("inter_black", bold, weight = FontWeight.Black),
    )
}

@OptIn(DelicateCoroutinesApi::class)
actual val JetBrainsMono: FontFamily
    @Composable
    get() {
        var fontFamily: FontFamily? by remember { mutableStateOf(null) }
        var fontFamilyLoaded by remember { mutableStateOf(false) }
        LaunchedEffect(true) {
            if (!fontFamilyLoaded) {
                GlobalScope.launch { fontFamily = getJetBrainsMonoWeb() }
                fontFamilyLoaded = true
            }
        }
        return fontFamily ?: FontFamily.Monospace
    }

@OptIn(ExperimentalResourceApi::class)
private suspend fun getJetBrainsMonoWeb(): FontFamily {
    suspend operator fun String.invoke() = Res.readBytes("font/$this")
    val regular = "jbmono_regular.ttf"()
    val bold = "jbmono_bold.ttf"()
    val semibold = "jbmono_semibold.ttf"()
    val medium = "jbmono_medium.ttf"()
    return FontFamily(
        Font("jbmono_regular", regular, weight = FontWeight.Normal),
        Font("jbmono_bold", bold, weight = FontWeight.Bold),
        Font("jbmono_semibold", semibold, weight = FontWeight.SemiBold),
        Font("jbmono_medium", medium, weight = FontWeight.Medium),
        Font("jbmono_thin", regular, weight = FontWeight.Thin),
        Font("jbmono_extralight", regular, weight = FontWeight.ExtraLight),
        Font("jbmono_light", regular, weight = FontWeight.Light),
        Font("jbmono_extrabold", bold, weight = FontWeight.ExtraBold),
        Font("jbmono_black", bold, weight = FontWeight.Black),
    )
}
