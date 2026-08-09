package io.github.toberocat.improvedfactions.unit.database

import io.github.toberocat.improvedfactions.database.DatabaseSettings
import io.github.toberocat.improvedfactions.database.DatabaseType
import io.github.toberocat.improvedfactions.database.createDataSource
import org.junit.jupiter.api.Test
import java.nio.file.Files
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@io.github.toberocat.improvedfactions.testing.UnitTest
class DatabaseDataSourceFactoryTest {
    @Test
    fun `sqlite datasource has exactly one connection`() {
        val databaseFile = Files.createTempFile("improved-factions-pool-", ".sqlite")
        val settings = DatabaseSettings(
            DatabaseType.SQLITE,
            "jdbc:sqlite:$databaseFile",
            null,
            null,
            mysqlMaximumPoolSize = 8
        )

        createDataSource(settings).use { dataSource ->
            assertEquals(1, dataSource.maximumPoolSize)
        }
    }

    @Test
    fun `mariadb datasource pool is bounded by configuration`() {
        val settings = DatabaseSettings(
            DatabaseType.MYSQL,
            "jdbc:mariadb://127.0.0.1:3307/improvedfactions",
            "improvedfactions",
            "improvedfactions",
            mysqlMaximumPoolSize = 3
        )

        createDataSource(settings).use { dataSource ->
            assertEquals(3, dataSource.maximumPoolSize)
        }
    }

    @Test
    fun `datasource closes explicitly with the storage lifecycle`() {
        val databaseFile = Files.createTempFile("improved-factions-lifecycle-", ".sqlite")
        val dataSource = createDataSource(
            DatabaseSettings(DatabaseType.SQLITE, "jdbc:sqlite:$databaseFile", null, null, 1)
        )

        dataSource.close()

        assertTrue(dataSource.isClosed)
    }
}
