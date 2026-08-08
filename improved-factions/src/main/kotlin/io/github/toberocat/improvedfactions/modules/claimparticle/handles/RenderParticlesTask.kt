package io.github.toberocat.improvedfactions.modules.claimparticle.handles

import io.github.toberocat.improvedfactions.claims.clustering.position.WorldPosition
import io.github.toberocat.improvedfactions.database.storage.ClaimKey
import io.github.toberocat.improvedfactions.database.storage.ClaimSnapshot
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.factions.FactionHandler
import io.github.toberocat.improvedfactions.modules.claimparticle.config.ClaimParticleModuleConfig
import io.github.toberocat.improvedfactions.user.noFactionId
import io.github.toberocat.improvedfactions.zone.ZoneHandler
import io.github.toberocat.toberocore.util.MathUtils
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class RenderParticlesTask(private val config: ClaimParticleModuleConfig) : BukkitRunnable() {
    override fun run() {
        LineHandler.clearCache()
        val cache = StorageManager.cache
        if (!cache.isReady()) return
        Bukkit.getOnlinePlayers().forEach { player ->
            val center = player.location.chunk
            cache.claimsNear(player.world.name, center.x, center.z, config.chunkRenderDistance)
                .filter { it.factionId != noFactionId || it.zoneType != ZoneHandler.FACTION_ZONE_TYPE }
                .forEach { player.renderClaimBoundary(it) }
        }
    }

    private fun Player.renderClaimBoundary(claim: ClaimSnapshot) {
        val color = when {
            claim.factionId != noFactionId -> FactionHandler.generateColor(claim.factionId)
            else -> ZoneHandler.getZone(claim.zoneType)?.mapColor ?: 0xffffff
        }
        val baseColor = Color.fromRGB(color)
        boundarySegments(claim).forEach { (start, end) ->
            LineHandler.getLocations(start, end)
                .forEach { location -> renderParticlesToPlayer(this, baseColor, location) }
        }
    }

    private fun boundarySegments(claim: ClaimSnapshot): List<Pair<WorldPosition, WorldPosition>> {
        val key = claim.key
        val minX = key.chunkX * 16
        val minZ = key.chunkZ * 16
        val maxX = minX + 16
        val maxZ = minZ + 16
        val northWest = WorldPosition(key.world, minX, minZ)
        val northEast = WorldPosition(key.world, maxX, minZ)
        val southEast = WorldPosition(key.world, maxX, maxZ)
        val southWest = WorldPosition(key.world, minX, maxZ)
        val candidates = listOf(
            ClaimKey(key.world, key.chunkX, key.chunkZ - 1) to (northWest to northEast),
            ClaimKey(key.world, key.chunkX + 1, key.chunkZ) to (northEast to southEast),
            ClaimKey(key.world, key.chunkX, key.chunkZ + 1) to (southEast to southWest),
            ClaimKey(key.world, key.chunkX - 1, key.chunkZ) to (southWest to northWest)
        )
        return candidates.filter { (neighbourKey, _) ->
            val neighbour = StorageManager.cache.claim(neighbourKey)
            neighbour?.factionId != claim.factionId || neighbour.zoneType != claim.zoneType
        }.map { it.second }
    }

    private fun renderParticlesToPlayer(player: Player, baseColor: Color, location: Location) {
        if (player.world != location.world) {
            return
        }

        val distance = player.location.distanceSquared(location).toFloat()
        if (distance >= config.blockRenderDistance) {
            return
        }

        val dust = Particle.DustOptions(
            baseColor, MathUtils.clamp(
                distance - config.particleSizeBias,
                config.minParticleSize,
                config.maxParticleSize
            )
        )

        player.spawnParticle(
            Particle.DUST,
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
