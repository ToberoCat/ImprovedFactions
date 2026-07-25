import java.nio.file.Files
import java.util.*
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.devtools.ksp)
    id("maven-publish")
    id("com.gradleup.shadow") version "9.6.1"
    id("com.github.ben-manes.versions") version "0.52.0"
    id("org.jetbrains.dokka") version "2.1.0"
}

val versionPropsFile = file("version.properties")
val versionProps = Properties()

if (versionPropsFile.exists()) {
    versionProps.load(versionPropsFile.inputStream())
}

val buildIncrement = versionProps["buildIncrement"]?.toString()?.toInt() ?: 1
val versionName = versionProps["versionName"]!!.toString()


group = "io.github.toberocat.improved-factions"
version = versionName

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://maven.paulem.net/releases/")
    maven("https://repo.mikeprimm.com/")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(project(":shared"))

    // Paper API
    compileOnly(libs.paper.api)

    // Exposed ORM
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.kotlin.datetime)

    // Jackson
    implementation(libs.jackson.core)
    implementation(libs.jackson.databind)
    implementation(libs.jackson.module.kotlin)

    // Other dependencies
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.datetime.jvm)
    implementation(libs.toberocore)
    implementation(libs.sqlite.jdbc)
    implementation(libs.h2)
    implementation(libs.spigot.update.checker)
    compileOnly(libs.guiengine)
    implementation(libs.adventure.text.minimessage)
    implementation(libs.adventure.text.serializer.legacy)
    implementation(libs.bstats.bukkit)

    // Provided dependencies
    compileOnly(libs.placeholderapi)
    compileOnly(libs.dynmap.api)

    // Test dependencies
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.junit.jupiter.params)
    testImplementation(libs.mockbukkit)
    testImplementation(libs.snakeyaml)
    testImplementation(libs.gson)
    testImplementation(libs.logback.classic)

    // KSP
    ksp(project(":code-generation"))
}

tasks.named<Copy>("processResources") {
    filesMatching("plugin.yml") {
        expand("version" to project.version)
    }
}

tasks.matching { it.name.startsWith("ksp") }.configureEach {
    dependsOn(generateBuildConfig)
}

dokka {
    dokkaPublications.html {
        outputDirectory.set(file("../improved-factions-docs/static/api"))
    }
    dokkaSourceSets.configureEach {
        skipEmptyPackages.set(true)
        reportUndocumented.set(true)
    }
}

tasks.shadowJar {
    archiveFileName.set("${project.name}-${project.version}.jar")
    relocate("com.fasterxml.jackson", "io.github.toberocat.relocated.jackson")
    relocate("net.kyori", "io.github.toberocat.relocated.kyori")
    relocate("dev.s7a", "io.github.toberocat.relocated.base64itemstack")
    relocate("org.bstats", "io.github.toberocat.relocated.bstats")
    relocate("com.jeff_media.updatechecker", "io.github.toberocat.relocated.updatechecker")
    
    exclude("META-INF/LICENSE*")
    exclude("META-INF/NOTICE*")

    mergeServiceFiles()
}

tasks {
    jar {
        enabled = false
    }

    build {
        dependsOn(shadowJar)
    }

    compileKotlin {
        dependsOn(generateBuildConfig)
    }

    processResources {
        filesMatching("**/*.yml") {
            expand("buildIncrement" to buildIncrement)
        }
    }

    test {
        useJUnitPlatform()
    }
}

kotlin {
    jvmToolchain(25)
    compilerOptions.jvmTarget.set(JvmTarget.JVM_21)

    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
        kotlin.srcDir("build/generated/source/buildConfig/kotlin")
    }
    sourceSets.test {
        kotlin.srcDir("build/generated/ksp/test/kotlin")
    }
}

ksp {
    arg("languageFolder", "$projectDir/src/main/resources/languages")
    arg("plugin.yml", "$projectDir/src/main/resources/plugin.yml")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}

val generateBuildConfig by tasks.registering {
    val outputDir = layout.buildDirectory.dir("generated/source/buildConfig/kotlin")
    val outputFile = outputDir.map { it.file("BuildConfig.kt") }

    outputs.file(outputFile)

    doLast {
        val dir = outputDir.get().asFile
        try {
            if (!dir.exists()) {
                dir.mkdirs()
                if (!dir.exists()) {
                    Files.createDirectories(dir.toPath())
                }
            }

            logger.info("BuildConfig directory created at: ${dir.absolutePath}, exists: ${dir.exists()}")

            val file = outputFile.get().asFile
            file.writeText(
                """
                object BuildConfig {
                    const val VERSION_NAME = "${project.version}"
                    const val BUILD_INCREMENT = $buildIncrement
                    const val VERSION = "${project.version}.$buildIncrement"
                }
                """.trimIndent()
            )
            logger.info("BuildConfig.kt generated successfully at: ${file.absolutePath}")
        } catch (e: Exception) {
            logger.error("Failed to generate BuildConfig.kt: ${e.message}")
            throw e
        }
    }
}
