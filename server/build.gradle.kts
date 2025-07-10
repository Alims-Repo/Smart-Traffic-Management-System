plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    kotlin("plugin.serialization") version "2.1.21"
    application
}

group = "com.gub"
version = "1.0.0"
application {
    mainClass.set("com.gub.application.ApplicationKt")
    
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)

    // 3rd party
    implementation("com.github.oshi:oshi-core:6.4.2")

    implementation("io.ktor:ktor-server-core:2.3.10")
    implementation("io.ktor:ktor-server-netty:2.3.10")

    // Database
    implementation("org.jetbrains.exposed:exposed-core:0.50.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.50.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.50.1")
    implementation("org.postgresql:postgresql:42.7.3")

    implementation("org.jetbrains.exposed:exposed-java-time:0.50.1")

    // Database drivers
    implementation("com.h2database:h2:2.2.220") // For development

    // Connection pool
    implementation("com.zaxxer:HikariCP:5.0.1")

    // Dependency injection
    implementation("io.insert-koin:koin-ktor:3.4.3")

    // WebSockets
    implementation("io.ktor:ktor-server-websockets:2.3.10")
    implementation("io.ktor:ktor-server-cio-jvm:2.3.11")

    // HTTP Client
    implementation("io.ktor:ktor-client-core:2.1.21")
    implementation("io.ktor:ktor-client-cio:2.1.21")
    implementation("io.ktor:ktor-client-content-negotiation:2.1.21")
    implementation("io.ktor:ktor-client-logging:2.1.21")

    // Content Negotiation + JSON
    implementation("io.ktor:ktor-server-content-negotiation:2.3.10")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.10")

    // Kotlinx Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // Testing
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)
}