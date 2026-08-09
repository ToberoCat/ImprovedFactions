package io.github.toberocat.improvedfactions.integration.database

import io.github.toberocat.improvedfactions.database.DatabaseMigrator
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.mariadb.MariaDBContainer
import java.sql.DriverManager
import kotlin.test.assertEquals

@io.github.toberocat.improvedfactions.testing.DatabaseTest
@Testcontainers(disabledWithoutDocker = true)
class DatabaseMigrationMariaDbTest {
    companion object {
        @Container
        @JvmField
        val mariaDb = MariaDBContainer("mariadb:10.3.39")
            .withDatabaseName("improved_factions")
            .withUsername("improved_factions")
            .withPassword("test-password")
    }

    @Test
    fun `mariadb 10_3 migration creates the schema and is idempotent`() {
        DatabaseMigrator.migrate(mariaDb.jdbcUrl, "classpath:db/migration/mysql", mariaDb.username, mariaDb.password)
        DatabaseMigrator.migrate(mariaDb.jdbcUrl, "classpath:db/migration/mysql", mariaDb.username, mariaDb.password)

        DriverManager.getConnection(mariaDb.jdbcUrl, mariaDb.username, mariaDb.password).use { connection ->
            connection.createStatement().use { statement ->
                statement.executeQuery(
                    "SELECT COUNT(*) FROM information_schema.tables " +
                        "WHERE table_schema = DATABASE() AND table_name <> 'flyway_schema_history'"
                ).use { result ->
                    check(result.next())
                    assertEquals(15, result.getInt(1))
                }

                statement.executeQuery("SELECT COUNT(*) FROM flyway_schema_history WHERE success = 1").use { result ->
                    check(result.next())
                    assertEquals(2, result.getInt(1))
                }
            }
        }
    }
}
