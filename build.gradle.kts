import com.diffplug.gradle.spotless.BaseKotlinExtension
import com.diffplug.gradle.spotless.SpotlessExtension
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

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
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "nextdown-wasm"
        browser {
            commonWebpackConfig {
                outputFileName = "nextdown-wasm.js"
                devServer =
                    (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                        static = (static ?: mutableListOf()).apply { add(project.rootDir.path) }
                    }
            }
        }
        binaries.executable()
    }
    js {
        moduleName = "nextdown-js"
        browser { commonWebpackConfig { outputFileName = "nextdown-js.js" } }
        binaries.executable()
    }
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
        val webMain by creating { dependsOn(commonMain) }
        val wasmJsMain by getting { dependsOn(webMain) }
        val jsMain by getting { dependsOn(webMain) }
    }
}

tasks.register<Copy>("webDev") {
    val wasmTask = tasks.getByPath(":wasmJsBrowserDevelopmentExecutableDistribution")
    val jsTask = tasks.getByPath(":jsBrowserDevelopmentExecutableDistribution")
    dependsOn(wasmTask)
    dependsOn(jsTask)
    from(wasmTask.outputs, jsTask.outputs)
    into(project.layout.buildDirectory.dir("dist/web/dev"))
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.register<Copy>("webProd") {
    val wasmTask = tasks.getByPath(":wasmJsBrowserDistribution")
    val jsTask = tasks.getByPath(":jsBrowserDistribution")
    dependsOn(wasmTask)
    dependsOn(jsTask)
    from(wasmTask.outputs, jsTask.outputs)
    into(project.layout.buildDirectory.dir("dist/web/prod"))
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
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
