package io.github.toberocat.improvedfactions.modules.pve.impl

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.clustering.WorldPosition
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.modules.pve.config.PveModuleConfig
import io.github.toberocat.improvedfactions.modules.pve.handle.PveModuleHandle
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.EntityType
import kotlin.math.round

class PveModuleHandleImpl(
    private val plugin: ImprovedFactionsPlugin,
    private val config: PveModuleConfig
) : PveModuleHandle {

    override fun spawnRaid(faction: Faction) {
        val cluster = plugin.claimChunkClusters.getFactionClustersById(faction.id.value).randomOrNull() ?: return

        val outerLoop = cluster.getOuterNodes().maxByOrNull { it.size } ?: return
        val position = outerLoop.randomOrNull() ?: return
        val centerX = round(cluster.centerX * 16).toInt()
        val centerY = round(cluster.centerY * 16).toInt()

        val direction = position.getDirection(WorldPosition(cluster.world, centerX, centerY))
        val world = Bukkit.getWorld(cluster.world) ?: return
        val bukkitPosition = Location(world, position.x.toDouble(), 0.0, position.y.toDouble())
        val spawnLocation = bukkitPosition.add(direction.normalize().multiply(-config.raidDistance().random()))

        spawnAt(world, spawnLocation, 10)

        faction.broadcast(
            "pve.raid.spawned",
            mapOf(
                "x" to centerX.toString(),
                "z" to centerY.toString(),
                "world" to cluster.world
            )
        )
    }

    private fun spawnAt(world: World, location: Location, raidSize: Int) {
        val loc = world.getHighestBlockAt(location.blockX, location.blockZ).location
        loc.y++

        println(loc)
        for (i in 0 until raidSize) {
            location.world?.spawnEntity(loc, listOf(
                EntityType.PILLAGER,
                EntityType.EVOKER,
                EntityType.VINDICATOR,
                EntityType.RAVAGER
            ).random())
        }
    }
}