package io.github.toberocat.improvedfactions.unit.database

import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@io.github.toberocat.improvedfactions.testing.UnitTest
class MariaDbMigrationSqlTest {
    @Test
    fun `MariaDB cluster migration does not define a database default for parent cluster`() {
        val migration = assertNotNull(
            javaClass.getResource("/db/migration/mysql/V1__create_schema.sql")
        ).readText()
        val parentClusterDefinition = migration.lineSequence()
            .first { it.trimStart().startsWith("parent_cluster", ignoreCase = true) }

        assertTrue(parentClusterDefinition.contains("BINARY(16)", ignoreCase = true))
        assertFalse(parentClusterDefinition.contains("DEFAULT", ignoreCase = true))
    }

    @Test
    fun `factionless sentinel insert uses MariaDB 10_3 compatible select syntax`() {
        val migration = assertNotNull(
            javaClass.getResource("/db/migration/mysql/V2__add_factionless_sentinel.sql")
        ).readText()

        assertTrue(migration.contains("FROM DUAL", ignoreCase = true))
        assertTrue(migration.contains("WHERE NOT EXISTS", ignoreCase = true))
    }
}
