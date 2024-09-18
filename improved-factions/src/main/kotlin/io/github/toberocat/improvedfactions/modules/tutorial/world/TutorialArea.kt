package io.github.toberocat.improvedfactions.modules.tutorial.world

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import kotlinx.coroutines.withContext
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player

data class TutorialArea(val origin: Location) {
    fun point(relativeX: Int, relativeY: Int, relativeZ: Int) =
        TutorialAreaPoint(origin, relativeX.toDouble(), relativeY.toDouble(), relativeZ.toDouble())

    suspend fun createArea(
        point1: TutorialAreaPoint,
        point2: TutorialAreaPoint,
        blockType: Material,
    ) {
        withContext(ImprovedFactionsPlugin.instance.bukkitDispatcher) {
            val minX = point1.getLocation().blockX.coerceAtMost(point2.getLocation().blockX)
            val minY = point1.getLocation().blockY.coerceAtMost(point2.getLocation().blockY)
            val minZ = point1.getLocation().blockZ.coerceAtMost(point2.getLocation().blockZ)

            val maxX = point1.getLocation().blockX.coerceAtLeast(point2.getLocation().blockX)
            val maxY = point1.getLocation().blockY.coerceAtLeast(point2.getLocation().blockY)
            val maxZ = point1.getLocation().blockZ.coerceAtLeast(point2.getLocation().blockZ)

            for (x in minX..maxX)
                for (y in minY..maxY)
                    for (z in minZ..maxZ)
                        origin.world?.getBlockAt(x, y, z)?.type = blockType
        }
    }

    fun teleportPlayer(player: Player, relativeX: Int, relativeY: Int, relativeZ: Int) {
        player.teleport(point(relativeX, relativeY, relativeZ).getLocation())
    }
}