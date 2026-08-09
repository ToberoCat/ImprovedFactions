package io.github.toberocat.improvedfactions.unit.modules.power

import io.github.toberocat.improvedfactions.ImprovedFactionsTest
import io.github.toberocat.improvedfactions.database.storage.GameStateCommands
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.modules.power.config.PowerManagementConfig
import org.bukkit.configuration.file.YamlConfiguration
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class PowerModuleTest : ImprovedFactionsTest() {
    @Test
    fun `overclaim toggle uses the documented config key`() {
        val config = PowerManagementConfig(allowOverclaim = true)

        config.reload(YamlConfiguration().apply {
            set("factions.power-management.allow-overclaiming", false)
        })

        assertFalse(config.allowOverclaim)
    }

    @Test
    fun `test setting max power`() {
        val faction = testFaction()
        assertEquals(50, faction.maxPower)
        assertEquals(50, faction.accumulatedPower)

        GameStateCommands.setPower(faction.id, maximum = 70)
        awaitStorage()
        assertEquals(70, StorageManager.cache.faction(faction.id)?.maxPower)
        assertEquals(50, StorageManager.cache.faction(faction.id)?.accumulatedPower)

        GameStateCommands.setPower(faction.id, maximum = 10)
        awaitStorage()
        assertEquals(10, StorageManager.cache.faction(faction.id)?.maxPower)
        assertEquals(10, StorageManager.cache.faction(faction.id)?.accumulatedPower)
    }

    @Test
    fun `test setting accumulated power`() {
        val faction = testFaction()
        assertEquals(50, faction.maxPower)
        assertEquals(50, faction.accumulatedPower)

        GameStateCommands.setPower(faction.id, accumulated = 70)
        awaitStorage()
        assertEquals(50, StorageManager.cache.faction(faction.id)?.maxPower)
        assertEquals(50, StorageManager.cache.faction(faction.id)?.accumulatedPower)

        GameStateCommands.setPower(faction.id, accumulated = 10)
        awaitStorage()
        assertEquals(50, StorageManager.cache.faction(faction.id)?.maxPower)
        assertEquals(10, StorageManager.cache.faction(faction.id)?.accumulatedPower)
    }
}
