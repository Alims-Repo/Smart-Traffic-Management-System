import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(compose.materialIconsExtended)
            implementation("dev.chrisbanes.haze:haze:1.6.4")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)

            implementation("com.google.code.gson:gson:2.10.1")
            implementation("io.ktor:ktor-client-logging:2.3.7")

            implementation("io.ktor:ktor-client-core:2.3.5")
            implementation("io.ktor:ktor-client-cio:2.3.5")
            implementation("io.ktor:ktor-client-websockets:2.3.5")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.5")

            implementation("io.ktor:ktor-client-content-negotiation:2.3.5")

            // Coroutines
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.7.3")

            // JSON
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

            // JavaFX for video playback (included in most JDKs)
            implementation("org.openjfx:javafx-controls:19")
            implementation("org.openjfx:javafx-media:19")
            implementation("org.openjfx:javafx-web:19")
            implementation("org.openjfx:javafx-swing:19")

            // Logging
            implementation("ch.qos.logback:logback-classic:1.4.11")
        }
    }
}


compose.desktop {
    application {
        mainClass = "com.gub.app.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.gub"
            packageVersion = "1.0.0"
        }
    }
}
