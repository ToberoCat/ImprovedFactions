package io.github.toberocat.improvedfactions.zone

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.FactionClaim
import io.github.toberocat.improvedfactions.claims.getFactionClaim
import io.github.toberocat.improvedfactions.user.noFactionId
import org.bukkit.Chunk

data class Zone(val type: String,
                val noFactionTitle: String,
                val announceTitle: Boolean,
                val protectAlways: Boolean,
                val mapColor: Int,
                val allowClaiming: Boolean) {
    fun claim(chunk: Chunk): FactionClaim {
        val claim = chunk.getFactionClaim() ?: FactionClaim.new {
            chunkX = chunk.x
            chunkZ = chunk.z
            this.world = chunk.world.name
            this.factionId = noFactionId
        }

        claim.zoneType = type
        ImprovedFactionsPlugin.instance.claimChunkClusters.insertZonePosition(claim.toPosition(), type)
        return claim
    }
}