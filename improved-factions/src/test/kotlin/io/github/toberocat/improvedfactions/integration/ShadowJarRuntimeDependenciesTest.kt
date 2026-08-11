package io.github.toberocat.improvedfactions.integration

import java.io.File
import java.util.jar.JarFile
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@io.github.toberocat.improvedfactions.testing.IntegrationTest
class ShadowJarRuntimeDependenciesTest {
    @Test
    fun `shadow jar contains the runtime dependencies needed at plugin startup`() {
        val shadowJar = File(requireNotNull(System.getProperty("shadowJar")))

        JarFile(shadowJar).use { jar ->
            listOf(
                "kotlin/jvm/internal/Intrinsics.class",
                "org/sqlite/JDBC.class",
                "org/mariadb/jdbc/Driver.class"
            ).forEach { entry ->
                assertNotNull(jar.getJarEntry(entry), "Missing $entry from ${shadowJar.name}")
            }

            val flywayPlugins = requireNotNull(
                jar.getJarEntry("META-INF/services/org.flywaydb.core.extensibility.Plugin")
            ) { "Missing Flyway plugin descriptor from ${shadowJar.name}" }
            val providerNames = jar.getInputStream(flywayPlugins).bufferedReader().readText()
            assertTrue(providerNames.contains("SQLiteDatabaseType"), "Missing SQLite Flyway provider")
            assertTrue(providerNames.contains("MariaDBDatabaseType"), "Missing MariaDB Flyway provider")

            val pluginDescriptor = requireNotNull(jar.getJarEntry("plugin.yml")) {
                "Missing plugin descriptor from ${shadowJar.name}"
            }
            val apiVersion = jar.getInputStream(pluginDescriptor).bufferedReader().useLines { lines ->
                lines.first { it.startsWith("api-version:") }.substringAfter(':').trim().removeSurrounding("'")
            }
            assertEquals("1.21.11", apiVersion, "Plugin API version must match the supported Paper version")

            val duplicateEntries = jar.entries().asSequence()
                .map { it.name }
                .groupingBy { it }
                .eachCount()
                .filterValues { it > 1 }
                .keys
            assertEquals(emptySet(), duplicateEntries, "Plugin archive contains duplicate entries")
        }
    }
}
