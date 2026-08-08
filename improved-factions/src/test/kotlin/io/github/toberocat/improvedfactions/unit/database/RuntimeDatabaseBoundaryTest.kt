package io.github.toberocat.improvedfactions.unit.database

import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.extension
import kotlin.test.assertEquals

class RuntimeDatabaseBoundaryTest {
    @Test
    fun `runtime gameplay packages contain no synchronous exposed queries`() {
        val root = Path.of("src/main/kotlin/io/github/toberocat/improvedfactions")
        val runtimePackages = listOf("commands", "listeners", "modules", "integrations", "charts", "api")
        val forbidden = listOf(
            "loggedTransaction",
            "transaction {",
            "SizedIterable",
            "Faction.findById(",
            "FactionUser.find",
            "FactionClaim.find",
            "FactionRank.find",
            "FactionInvite.find",
            "Faction.all()"
        )
        val violations = buildList {
            runtimePackages.forEach { packageName ->
                Files.walk(root.resolve(packageName)).use { paths ->
                    paths.filter { Files.isRegularFile(it) && it.extension == "kt" }.forEach { path ->
                        val source = Files.readString(path)
                        forbidden.filter(source::contains).forEach { token ->
                            add("${root.relativize(path)} -> $token")
                        }
                    }
                }
            }
        }

        assertEquals(emptyList(), violations)
    }

    @Test
    fun `database worker commands accept no bukkit or exposed types`() {
        val source = Files.readString(
            Path.of("src/main/kotlin/io/github/toberocat/improvedfactions/database/storage/GameStateCommands.kt")
        )

        assertEquals(false, source.contains("org.bukkit"))
        assertEquals(false, source.contains("org.jetbrains.exposed"))
        assertEquals(false, source.contains("FactionClaim"))
        assertEquals(false, source.contains("FactionUser"))
    }

    @Test
    fun `legacy exposed entities are internal and events expose snapshots only`() {
        val entityFiles = listOf(
            "factions/Faction.kt",
            "user/FactionUser.kt",
            "claims/FactionClaim.kt",
            "ranks/FactionRank.kt",
            "invites/FactionInvite.kt",
            "permissions/FactionPermission.kt",
            "modules/home/data/FactionHome.kt"
        )
        entityFiles.forEach { relative ->
            val source = Files.readString(Path.of("src/main/kotlin/io/github/toberocat/improvedfactions/$relative"))
            assertEquals(true, source.contains("internal class"), relative)
        }

        Files.walk(Path.of("src/main/kotlin/io/github/toberocat/improvedfactions/api/events")).use { paths ->
            paths.filter { Files.isRegularFile(it) && it.extension == "kt" }.forEach { eventFile ->
                val source = Files.readString(eventFile)
                assertEquals(false, source.contains("improvedfactions.factions.Faction"), eventFile.toString())
                assertEquals(false, source.contains("improvedfactions.user.FactionUser"), eventFile.toString())
            }
        }
    }
}
