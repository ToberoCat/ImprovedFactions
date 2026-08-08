package io.github.toberocat.improvedfactions.unit.modules.power

import io.github.toberocat.improvedfactions.ImprovedFactionsTest
import io.github.toberocat.improvedfactions.database.storage.GameStateCommands
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import org.junit.jupiter.api.Test

class PowerModuleTest : ImprovedFactionsTest() {
    @Test
    fun `test setting max power`() {
        val faction = testFaction()
        assert(faction.maxPower == 50)
        assert(faction.accumulatedPower == 50)

        GameStateCommands.setPower(faction.id, maximum = 70)
        awaitStorage()
        assert(StorageManager.cache.faction(faction.id)?.maxPower == 70)
        assert(StorageManager.cache.faction(faction.id)?.accumulatedPower == 50)

        GameStateCommands.setPower(faction.id, maximum = 10)
        awaitStorage()
        assert(StorageManager.cache.faction(faction.id)?.maxPower == 10)
        assert(StorageManager.cache.faction(faction.id)?.accumulatedPower == 10)
    }

    @Test
    fun `test setting accumulated power`() {
        val faction = testFaction()
        assert(faction.maxPower == 50)
        assert(faction.accumulatedPower == 50)

        GameStateCommands.setPower(faction.id, accumulated = 70)
        awaitStorage()
        assert(StorageManager.cache.faction(faction.id)?.maxPower == 50)
        assert(StorageManager.cache.faction(faction.id)?.accumulatedPower == 50)

        GameStateCommands.setPower(faction.id, accumulated = 10)
        awaitStorage()
        assert(StorageManager.cache.faction(faction.id)?.maxPower == 50)
        assert(StorageManager.cache.faction(faction.id)?.accumulatedPower == 10)
    }
}
