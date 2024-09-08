package io.github.toberocat.improvedfactions.unit.claims

import io.github.toberocat.improvedfactions.exceptions.CantClaimThisChunkException
import io.github.toberocat.improvedfactions.unit.ImprovedFactionsTest
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class FactionClaimTest : ImprovedFactionsTest() {
    @Test
    fun `test can claim in allowed world`() {
        val faction = testFaction()
        val world = testWorld()
        val chunk = world.getChunkAt(0, 0)

        transaction { assertDoesNotThrow { faction.claim(chunk) } }
    }

    @Test
    fun `test can't claim in blocked world`() {
        val faction = testFaction()
        val world = testWorld()
        val chunk = world.getChunkAt(0, 0)

        plugin.improvedFactionsConfig.allowedWorlds = setOf("other-world")
        transaction { assertThrows<CantClaimThisChunkException> { faction.claim(chunk) } }
    }
}