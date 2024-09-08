package io.github.toberocat.improvedfactions.unit.modules.power

import io.github.toberocat.improvedfactions.factions.PowerAccumulationChangeReason
import io.github.toberocat.improvedfactions.unit.ImprovedFactionsTest
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test

class PowerModuleTest : ImprovedFactionsTest() {
    @Test
    fun `test setting max power`() {
        val faction = testFaction()
        assert(faction.maxPower == 50)
        assert(faction.accumulatedPower == 50)

        transaction { faction.setMaxPower(70) }
        assert(faction.maxPower == 70)
        assert(faction.accumulatedPower == 50)

        transaction { faction.setMaxPower(10) }
        assert(faction.maxPower == 10)
        assert(faction.accumulatedPower == 10)
    }

    @Test
    fun `test setting accumulated power`() {
        val faction = testFaction()
        assert(faction.maxPower == 50)
        assert(faction.accumulatedPower == 50)

        transaction { faction.setAccumulatedPower(70, PowerAccumulationChangeReason.OTHER) }
        assert(faction.maxPower == 50)
        assert(faction.accumulatedPower == 50)

        transaction { faction.setAccumulatedPower(10, PowerAccumulationChangeReason.OTHER) }
        assert(faction.maxPower == 50)
        assert(faction.accumulatedPower == 10)
    }
}