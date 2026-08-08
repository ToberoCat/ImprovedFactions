package io.github.toberocat.improvedfactions.unit.database

import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.test.assertFalse

class HotPathDatabaseBoundaryTest {
    @Test
    fun `server-thread hot paths neither transact nor wait for futures`() {
        val sourceRoot = Path.of("src/main/kotlin/io/github/toberocat/improvedfactions")
        val hotPaths = listOf(
            "listeners/move/MoveListener.kt",
            "listeners/PlayerJoinListener.kt",
            "listeners/claim/ProtectionListener.kt",
            "listeners/claim/GeneralPvPListener.kt",
            "listeners/claim/InFactionPvPListener.kt",
            "listeners/claim/ClaimTntListener.kt",
            "listeners/claim/ClaimFullTntListener.kt",
            "modules/claimparticle/handles/RenderParticlesTask.kt",
            "integrations/papi/PlaceholderIntegration.kt"
        )
        val forbidden = listOf(
            "loggedTransaction", "transaction {", ".get(", ".join(", "FactionClaim.find", "SizedIterable"
        )

        hotPaths.forEach { relativePath ->
            val path = sourceRoot.resolve(relativePath)
            val source = Files.readString(path)
            forbidden.forEach { token ->
                assertFalse(source.contains(token), "$relativePath must not contain blocking token '$token'")
            }
        }
    }
}
