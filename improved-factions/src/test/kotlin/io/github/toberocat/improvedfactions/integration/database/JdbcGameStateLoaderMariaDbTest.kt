package io.github.toberocat.improvedfactions.integration.database

import io.github.toberocat.improvedfactions.database.DatabaseMigrator
import io.github.toberocat.improvedfactions.database.DatabaseSettings
import io.github.toberocat.improvedfactions.database.DatabaseType
import io.github.toberocat.improvedfactions.database.createDataSource
import io.github.toberocat.improvedfactions.database.storage.JdbcGameStateLoader
import io.github.toberocat.improvedfactions.database.storage.RaidSnapshotConfig
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import kotlin.test.assertNotNull

@EnabledIfEnvironmentVariable(named = "MARIADB_TEST_URL", matches = ".+")
@io.github.toberocat.improvedfactions.testing.DatabaseTest
class JdbcGameStateLoaderMariaDbTest {
    @Test
    fun `mariadb pool can load a migrated snapshot`() {
        val url = requireNotNull(System.getenv("MARIADB_TEST_URL"))
        val user = System.getenv("MARIADB_TEST_USER") ?: "root"
        val password = System.getenv("MARIADB_TEST_PASSWORD") ?: ""
        DatabaseMigrator.migrate(url, "classpath:db/migration/mysql", user, password)

        val settings = DatabaseSettings(DatabaseType.MYSQL, url, user, password, 3)
        createDataSource(settings).use { dataSource ->
            val snapshot = JdbcGameStateLoader(
                dataSource,
                RaidSnapshotConfig(allowOverclaim = true, claimPowerKeep = 1.0, guestRankName = "Guest")
            ).load()
            assertNotNull(snapshot.claims)
        }
    }
}
