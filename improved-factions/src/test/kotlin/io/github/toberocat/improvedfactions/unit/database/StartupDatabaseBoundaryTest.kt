package io.github.toberocat.improvedfactions.unit.database

import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@io.github.toberocat.improvedfactions.testing.UnitTest
class StartupDatabaseBoundaryTest {
    @Test
    fun `startup snapshot is asynchronous and never awaited by Paper lifecycle code`() {
        val storage = source("database/storage/StorageManager.kt")
        val modules = source("modules/ModuleManager.kt")
        val base = source("modules/base/BaseModule.kt")

        assertTrue(storage.contains("fun initializeSnapshotRepository"))
        assertTrue(storage.contains("CompletionStage<GameStateSnapshot>"))
        assertFalse(modules.contains("initializeSnapshotRepository"))
        assertTrue(base.contains("initializeSnapshotRepository"))
        assertTrue(base.contains("continueOnMain"))

        listOf(storage, modules, base).forEach { lifecycleSource ->
            assertFalse(lifecycleSource.contains("toCompletableFuture().get("))
            assertFalse(lifecycleSource.contains(".join()"))
            assertFalse(lifecycleSource.contains("StorageBootstrap(cache"))
        }
    }

    @Test
    fun `legacy Exposed clustering implementation is not reachable from runtime packages`() {
        val root = Path.of("src/main/kotlin/io/github/toberocat/improvedfactions")
        val runtimePackages = listOf("commands", "listeners", "modules", "integrations", "charts", "api")
        runtimePackages.forEach { packageName ->
            Files.walk(root.resolve(packageName)).use { paths ->
                paths.filter { Files.isRegularFile(it) && it.toString().endsWith(".kt") }.forEach { path ->
                    val source = Files.readString(path)
                    assertFalse(source.contains("ClaimClusterDetector"), root.relativize(path).toString())
                    assertFalse(source.contains("claims.clustering.detector"), root.relativize(path).toString())
                }
            }
        }
    }

    private fun source(relative: String): String = Files.readString(
        Path.of("src/main/kotlin/io/github/toberocat/improvedfactions/$relative")
    )
}
