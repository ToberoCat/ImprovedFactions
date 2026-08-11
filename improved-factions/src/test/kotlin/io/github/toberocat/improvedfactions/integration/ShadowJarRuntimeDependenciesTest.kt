package io.github.toberocat.improvedfactions.integration

import java.io.File
import java.util.jar.JarFile
import kotlin.test.Test
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
        }
    }
}
