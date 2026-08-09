package io.github.toberocat.improvedfactions.integration.modules.power

import io.github.toberocat.improvedfactions.ImprovedFactionsTest
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import net.kyori.adventure.text.Component
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.event.entity.PlayerDeathEvent
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PlayerDeathListenerTest : ImprovedFactionsTest() {
    @Test
    fun `death outside an allowed world does not reduce faction power`() {
        val player = createTestPlayer()
        val faction = testFaction(player.uniqueId)
        BaseModule.config.allowedWorlds = setOf("allowed-world-only")

        server.pluginManager.callEvent(
            PlayerDeathEvent(
                player,
                DamageSource.builder(DamageType.GENERIC).build(),
                emptyList(),
                0,
                Component.empty(),
                true
            )
        )
        awaitStorage()

        assertEquals(faction.accumulatedPower, StorageManager.cache.faction(faction.id)?.accumulatedPower)
    }
}
