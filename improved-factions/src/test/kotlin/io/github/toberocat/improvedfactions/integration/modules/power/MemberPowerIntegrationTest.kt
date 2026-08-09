package io.github.toberocat.improvedfactions.integration.modules.power

import io.github.toberocat.improvedfactions.FactionsIntegrationTest
import io.github.toberocat.improvedfactions.database.storage.GameStateCommands
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/** The configured member constant is the durable source of faction start and membership power. */
class MemberPowerIntegrationTest : FactionsIntegrationTest() {
    @Test
    fun `member power constant controls faction start max and join leave adjustments`() = scenario {
        val originalConstant = PowerRaidsModule.config.baseMemberConstant
        PowerRaidsModule.config.baseMemberConstant = 10.0
        try {
            val owner = player("power-owner")
            val member = player("power-member")

            command("/f create PowerTeam")
                .asPlayer(owner)
                .run()
                .expectHandled()
                .awaitStorage()

            val faction = checkNotNull(StorageManager.cache.factions().singleOrNull { it.name == "PowerTeam" })
            assertEquals(10, faction.maxPower)
            assertEquals(10, faction.accumulatedPower)

            GameStateCommands.setJoinType(faction.id, io.github.toberocat.improvedfactions.factions.FactionJoinType.OPEN).await()
            awaitStorage()
            command("/f join PowerTeam")
                .asPlayer(member)
                .run()
                .expectHandled()
                .awaitStorage()

            assertEquals(20, checkNotNull(StorageManager.cache.faction(faction.id)).maxPower)

            GameStateCommands.setPower(faction.id, accumulated = 20).await()
            awaitStorage()

            command("/f leave confirm")
                .asPlayer(member)
                .run()
                .expectHandled()
                .awaitStorage()

            val afterLeave = checkNotNull(StorageManager.cache.faction(faction.id))
            assertEquals(10, afterLeave.maxPower)
            assertEquals(10, afterLeave.accumulatedPower, "Lowering member-derived max power must clamp stored power")
        } finally {
            PowerRaidsModule.config.baseMemberConstant = originalConstant
        }
    }
}
