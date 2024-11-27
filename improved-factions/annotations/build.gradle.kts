plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "io.github.toberocat.improved-factions"
version = "2.3.0"

repositories {
    mavenCentral()
}

dependencies {
}

kotlin {
    jvmToolchain(17)
}