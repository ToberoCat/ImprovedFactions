package io.github.toberocat.improvedfactions.modules.tutorial.scene

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player

class SampleScene(player: Player, plugin: ImprovedFactionsPlugin) : Scene(player, plugin) {
    override suspend fun start() {
        area.createArea(
            area.point(0, 0, 0),
            area.point(16, 0, 16),
            Material.GRASS_BLOCK
        )
        movePlayer(8, 8, 0)
        val villager = createEntity(EntityType.VILLAGER, area.point(0, 1, 8), "Villager") ?: return
        lookAtEntity(villager)
        villager.moveTo(area.point(16, 1, 8), 1.0)
        stopLookAt()
    }
}