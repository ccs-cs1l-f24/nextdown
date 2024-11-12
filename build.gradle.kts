import com.diffplug.gradle.spotless.BaseKotlinExtension
import com.diffplug.gradle.spotless.SpotlessExtension
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeJB)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.ksp)
    alias(libs.plugins.serialization)
    alias(libs.plugins.spotless)
}

kotlin {
    jvm("desktop")
    @Suppress("unused")
    sourceSets {
        val commonMain by getting {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
            dependencies {
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.components.resources)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.serialization)
                implementation(libs.arrow.optics)
            }
        }
        val desktopMain by getting { dependencies { implementation(compose.desktop.currentOs) } }
    }
}

dependencies { kspCommonMainMetadata(libs.arrow.optics.ksp) }

compose.resources {
    packageOfResClass = "resources"
    publicResClass = true
}

compose.desktop {
    application {
        mainClass = "MainKt"
        buildTypes.release.proguard {
            obfuscate.set(true)
            optimize.set(true)
            joinOutputJars.set(true)
        }
        nativeDistributions {
            packageName = "NextDown"
            packageVersion = "0.0.1"
            licenseFile.set(rootProject.file("LICENSE"))
            targetFormats(TargetFormat.Exe, TargetFormat.Deb)
        }
    }
}

configure<SpotlessExtension> {
    fun BaseKotlinExtension.ktfmtConfigured() =
        ktfmt().kotlinlangStyle().configure {
            it.setManageTrailingCommas(true)
            it.setMaxWidth(120)
        }
    kotlin {
        target(
            fileTree(".") {
                include("**/*.kt")
                exclude("**/build/**")
            }
        )
        ktfmtConfigured()
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        ktfmtConfigured()
    }
}
