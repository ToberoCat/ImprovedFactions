package io.github.toberocat.improvedfactions.integration.database

import io.github.toberocat.improvedfactions.database.DatabaseMigrator
import java.nio.file.Files
import java.sql.DriverManager
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DatabaseMigrationTest {
    @Test
    fun `fresh sqlite database receives the complete schema`() {
        val databaseFile = Files.createTempFile("improved-factions-migration-", ".sqlite").toFile()
        databaseFile.delete()
        val url = "jdbc:sqlite:${databaseFile.absolutePath}"

        DatabaseMigrator.migrate(url, "classpath:db/migration/sqlite")

        DriverManager.getConnection(url).use { connection ->
            val tables = connection.createStatement().use { statement ->
                statement.executeQuery(
                    "SELECT name FROM sqlite_master WHERE type = 'table' AND name NOT LIKE 'sqlite_%'"
                ).use { result ->
                    buildSet {
                        while (result.next()) add(result.getString("name"))
                    }
                }
            }

            assertTrue("flyway_schema_history" in tables)
            assertEquals(
                setOf(
                    "clusters",
                    "factions",
                    "faction_users",
                    "faction_ranks",
                    "faction_permissions",
                    "faction_bans",
                    "player_usage_limits",
                    "faction_clusters",
                    "zone_clusters",
                    "faction_claims",
                    "faction_invites",
                    "known_offline_players",
                    "faction_homes",
                    "faction_relations",
                    "faction_ally_invites"
                ),
                tables - "flyway_schema_history"
            )
        }
    }

    @Test
    fun `sqlite migration is idempotent`() {
        val databaseFile = Files.createTempFile("improved-factions-migration-", ".sqlite").toFile()
        databaseFile.delete()
        val url = "jdbc:sqlite:${databaseFile.absolutePath}"

        DatabaseMigrator.migrate(url, "classpath:db/migration/sqlite")
        DatabaseMigrator.migrate(url, "classpath:db/migration/sqlite")

        DriverManager.getConnection(url).use { connection ->
            connection.createStatement().use { statement ->
                statement.executeQuery("SELECT COUNT(*) FROM flyway_schema_history").use { result ->
                    assertTrue(result.next())
                    assertEquals(1, result.getInt(1))
                }
            }
        }
    }
}
