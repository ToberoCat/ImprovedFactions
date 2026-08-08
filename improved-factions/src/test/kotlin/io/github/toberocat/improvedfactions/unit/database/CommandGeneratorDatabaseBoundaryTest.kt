package io.github.toberocat.improvedfactions.unit.database

import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertFalse

class CommandGeneratorDatabaseBoundaryTest {
    @Test
    fun `generated command and tab completion never wrap gameplay in a transaction`() {
        val source = Files.readString(
            Path.of("code-generation/src/main/kotlin/io/github/toberocat/improvedfactions/command/generator/CommandCodeGenerator.kt")
        )

        assertFalse(source.contains("DatabaseManager.loggedTransaction"))
        assertFalse(source.contains("return loggedTransaction"))
    }
}
