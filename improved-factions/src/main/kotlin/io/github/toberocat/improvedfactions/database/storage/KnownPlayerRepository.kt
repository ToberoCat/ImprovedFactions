package io.github.toberocat.improvedfactions.database.storage

import io.github.toberocat.improvedfactions.database.DatabaseType
import java.nio.ByteBuffer
import java.util.UUID
import javax.sql.DataSource

class KnownPlayerRepository(
    private val dataSource: DataSource,
    private val databaseType: DatabaseType
) {
    fun upsert(uniqueId: UUID, name: String) {
        dataSource.connection.use { connection ->
            connection.autoCommit = false
            try {
                val sql = when (databaseType) {
                    DatabaseType.SQLITE ->
                        "INSERT INTO known_offline_players (id, name) VALUES (?, ?) " +
                            "ON CONFLICT(id) DO UPDATE SET name = excluded.name"
                    DatabaseType.MYSQL ->
                        "INSERT INTO known_offline_players (id, name) VALUES (?, ?) " +
                            "ON DUPLICATE KEY UPDATE name = VALUES(name)"
                }
                connection.prepareStatement(sql).use { statement ->
                    statement.setBytes(1, uniqueId.bytes())
                    statement.setString(2, name)
                    statement.executeUpdate()
                }
                connection.commit()
            } catch (failure: Throwable) {
                runCatching { connection.rollback() }
                throw failure
            }
        }
    }

    private fun UUID.bytes(): ByteArray = ByteBuffer.allocate(16)
        .putLong(mostSignificantBits)
        .putLong(leastSignificantBits)
        .array()
}
