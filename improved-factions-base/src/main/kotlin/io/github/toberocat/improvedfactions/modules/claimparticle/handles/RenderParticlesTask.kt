package io.github.toberocat.improvedfactions.modules.claimparticle.handles

import io.github.toberocat.improvedfactions.claims.clustering.Cluster
import io.github.toberocat.improvedfactions.claims.clustering.ClusterExtension.getCurrentClusters
import io.github.toberocat.improvedfactions.claims.clustering.WorldPosition
import io.github.toberocat.improvedfactions.modules.claimparticle.config.ClaimParticleModuleConfig
import io.github.toberocat.toberocore.util.MathUtils
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.scheduler.BukkitRunnable

class RenderParticlesTask(private val config: ClaimParticleModuleConfig) : BukkitRunnable() {
    override fun run() {
        Bukkit.getOnlinePlayers().forEach { player ->
            player.getCurrentClusters(config.chunkRenderDistance).forEach { renderClusterParticles(it) }
        }
    }

    private fun renderClusterParticles(cluster: Cluster) {
        cluster.getOuterNodes().forEach { renderLoop(cluster, it) }
    }

    private fun renderLoop(cluster: Cluster, loop: List<WorldPosition>) {
        val baseColor = Color.fromRGB(cluster.getColor())
        for (i in 1 until loop.size) {
            val pos1 = loop[i - 1]
            val pos2 = loop[i]
            val locations = LineHandler.getLocations(pos1, pos2)
            locations.forEach { location -> renderParticlesToPlayer(baseColor, location) }
        }
    }

    private fun renderParticlesToPlayer(baseColor: Color, location: Location) {
        val f = Math.random()
        val color = when {
            f < 0.2 -> Color.WHITE
            f < 0.4 -> Color.BLACK
            else -> baseColor
        }

        // ToDo: This is a very expensive operation, consider not iterating twice over the player
        // TODo: Cache the locations instead of computing every render interval
        Bukkit.getOnlinePlayers()
            .forEach { player ->
                val distance = player.location.distanceSquared(location).toFloat()
                if (distance >= config.blockRenderDistance) {
                    return@forEach
                }

                val dust = Particle.DustOptions(
                    color, MathUtils.clamp(
                        distance - config.particleSizeBias,
                        config.minParticleSize,
                        config.maxParticleSize
                    )
                )

                player.spawnParticle(
                    Particle.REDSTONE,
                    location,
                    config.particleCount,
                    config.particleSpread,
                    config.particleSpread,
                    config.particleSpread,
                    config.particleSpeed,
                    dust
                )
            }
    }
}