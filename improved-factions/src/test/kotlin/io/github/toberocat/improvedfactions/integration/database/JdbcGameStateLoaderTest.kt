package io.github.toberocat.improvedfactions.integration.database

import io.github.toberocat.improvedfactions.database.DatabaseMigrator
import io.github.toberocat.improvedfactions.database.DatabaseSettings
import io.github.toberocat.improvedfactions.database.DatabaseType
import io.github.toberocat.improvedfactions.database.createDataSource
import io.github.toberocat.improvedfactions.database.storage.ClaimKey
import io.github.toberocat.improvedfactions.database.storage.JdbcGameStateLoader
import io.github.toberocat.improvedfactions.database.storage.KnownPlayerRepository
import io.github.toberocat.improvedfactions.database.storage.RaidSnapshotConfig
import io.github.toberocat.improvedfactions.database.storage.StorageDispatcher
import org.junit.jupiter.api.Test
import java.nio.ByteBuffer
import java.nio.file.Files
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import java.util.concurrent.TimeUnit

class JdbcGameStateLoaderTest {
    @Test
    fun `sqlite loader materializes immutable values inside the database boundary`() {
        val databaseFile = Files.createTempFile("improved-factions-snapshot-", ".sqlite").toFile()
        databaseFile.delete()
        val url = "jdbc:sqlite:${databaseFile.absolutePath}"
        DatabaseMigrator.migrate(url, "classpath:db/migration/sqlite")
        val settings = DatabaseSettings(DatabaseType.SQLITE, url, null, null, 1)
        val owner = UUID.randomUUID()
        val member = UUID.randomUUID()

        createDataSource(settings).use { dataSource ->
            dataSource.connection.use { connection ->
                connection.prepareStatement(
                    "INSERT INTO factions (id, name, owner, accumulated_power, max_power, default_rank, join_type) " +
                        "VALUES (1, 'Builders', ?, 35, 50, 7, 1)"
                ).use { it.setBytes(1, owner.bytes()); it.executeUpdate() }
                connection.prepareStatement(
                    "INSERT INTO faction_ranks (id, faction_id, rank_name, priority) VALUES (7, 1, 'Member', 1)"
                ).use { it.executeUpdate() }
                connection.prepareStatement(
                    "INSERT INTO faction_users (uniqueId, faction_id, rank_id) VALUES (?, 1, 7)"
                ).use { it.setBytes(1, member.bytes()); it.executeUpdate() }
                connection.createStatement().use {
                    it.executeUpdate(
                        "INSERT INTO faction_invites (inviter_id, invited_id, faction_id, rank_id, expiration_date) " +
                            "VALUES (1, 1, 1, 7, '2026-08-08T12:34:56')"
                    )
                    it.executeUpdate(
                        "INSERT INTO faction_ally_invites (source_faction_id, target_faction_id, expiration_date) " +
                            "VALUES (1, 1, '2026-08-08 12:34:56')"
                    )
                }
                connection.prepareStatement(
                    "INSERT INTO known_offline_players (id, name) VALUES (?, 'Owner')"
                ).use { it.setBytes(1, owner.bytes()); it.executeUpdate() }
                connection.prepareStatement(
                    "INSERT INTO faction_claims (chunk_x, chunk_z, faction_id, world, zone_type) " +
                        "VALUES (3, -2, 1, 'world', 'default')"
                ).use { it.executeUpdate() }
                connection.commit()
            }

            KnownPlayerRepository(dataSource, DatabaseType.SQLITE).also { repository ->
                repository.upsert(owner, "RenamedOwner")
            }

            val snapshot = JdbcGameStateLoader(
                dataSource,
                RaidSnapshotConfig(allowOverclaim = true, claimPowerKeep = 1.0, guestRankName = "Guest")
            ).load()

            assertEquals("Builders", snapshot.factions[1]?.name)
            assertEquals(1, snapshot.factions[1]?.claimCount)
            assertEquals("Member", snapshot.users[member]?.rankName)
            assertEquals("RenamedOwner", snapshot.playerNames[owner])
            assertFalse(checkNotNull(snapshot.claims[ClaimKey("world", 3, -2)]).isRaidable)
            assertTrue(snapshot.invites.values.single().expiresAtEpochMillis > 0)
            assertTrue(snapshot.allyInvites.values.single().expiresAtEpochMillis > 0)
        }
    }

    @Test
    fun `sqlite worker serializes queued upserts without lock failures`() {
        val databaseFile = Files.createTempFile("improved-factions-locks-", ".sqlite").toFile()
        databaseFile.delete()
        val url = "jdbc:sqlite:${databaseFile.absolutePath}"
        DatabaseMigrator.migrate(url, "classpath:db/migration/sqlite")
        val settings = DatabaseSettings(DatabaseType.SQLITE, url, null, null, 1)

        createDataSource(settings).use { dataSource ->
            val dispatcher = StorageDispatcher.create(DatabaseType.SQLITE, mysqlParallelism = 4)
            val repository = KnownPlayerRepository(dataSource, DatabaseType.SQLITE)
            val writes = (1..32).map { index ->
                dispatcher.submit { repository.upsert(UUID.randomUUID(), "Player$index") }
            }

            writes.forEach { it.toCompletableFuture().get(3, TimeUnit.SECONDS) }
            dataSource.connection.use { connection ->
                connection.createStatement().use { statement ->
                    statement.executeQuery("SELECT COUNT(*) FROM known_offline_players").use { result ->
                        result.next()
                        assertEquals(32, result.getInt(1))
                    }
                }
            }
            dispatcher.close()
        }
    }

    private fun UUID.bytes(): ByteArray = ByteBuffer.allocate(16)
        .putLong(mostSignificantBits)
        .putLong(leastSignificantBits)
        .array()
}
