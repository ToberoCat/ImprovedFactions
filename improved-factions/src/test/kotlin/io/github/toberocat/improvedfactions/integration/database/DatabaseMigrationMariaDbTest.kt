package io.github.toberocat.improvedfactions.integration.database

import io.github.toberocat.improvedfactions.database.DatabaseMigrator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import java.sql.DriverManager
import kotlin.test.assertEquals

@EnabledIfEnvironmentVariable(named = "MARIADB_TEST_URL", matches = ".+")
class DatabaseMigrationMariaDbTest {
    @Test
    fun `mariadb migration creates the schema and is idempotent`() {
        val url = requireNotNull(System.getenv("MARIADB_TEST_URL"))
        val user = System.getenv("MARIADB_TEST_USER") ?: "root"
        val password = System.getenv("MARIADB_TEST_PASSWORD") ?: ""

        DatabaseMigrator.migrate(url, "classpath:db/migration/mysql", user, password)
        DatabaseMigrator.migrate(url, "classpath:db/migration/mysql", user, password)

        DriverManager.getConnection(url, user, password).use { connection ->
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
