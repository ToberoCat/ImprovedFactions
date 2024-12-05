package io.github.toberocat.improvedfactions.listeners.claim

import io.github.toberocat.improvedfactions.annotations.localization.Localization
import io.github.toberocat.improvedfactions.claims.clustering.cluster.FactionCluster
import io.github.toberocat.improvedfactions.claims.clustering.position.ChunkPosition
import io.github.toberocat.improvedfactions.claims.getFactionClaim
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.managers.ByPassManager
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.user.noFactionId
import org.bukkit.Chunk
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Listener

abstract class ProtectionListener(
    protected val zoneType: String,
    private val sendMessage: Boolean = true
) : Listener {
    private val claimClusters = BaseModule.claimChunkClusters
    abstract fun namespace(): String

    protected fun protectChunk(event: Cancellable, entity: Entity?, player: Player) =
        protectChunk(event, entity?.location?.chunk, player)


    protected fun protectChunk(event: Cancellable, block: Block?, player: Player) =
        protectChunk(event, block?.chunk, player)

    @Localization("base.claim.protected")
    @Localization("base.zone.protected")
    private fun protectChunk(event: Cancellable, chunk: Chunk?, player: Player) {
        if (ByPassManager.isBypassing(player.uniqueId)) return

        loggedTransaction {
            val claim = chunk?.getFactionClaim()
            val claimZone = claim?.zone()
            if (claim?.zoneType != zoneType || claimZone?.protectAlways == false && claim.factionId == noFactionId)
                return@loggedTransaction

            val claimedFaction = claim.factionId
            val playerFaction = player.factionUser().factionId
            if (claimedFaction == playerFaction && playerFaction != noFactionId)
                return@loggedTransaction

            val isRaidable = ChunkPosition(chunk.x, chunk.z, claim.world).getFactionClaim()
                ?.let { claimClusters.getCluster(it) }
                ?.let { it.findAdditionalType() as? FactionCluster }?.isUnprotected(chunk.x, chunk.z, chunk.world.name)
                ?: false
            if (isRaidable)
                return@loggedTransaction

            event.isCancelled = true
            if (!sendMessage) {
                return@loggedTransaction
            }

            when (claimZone?.protectAlways) {
                true -> player.sendLocalized("base.zone.protected")
                else -> player.sendLocalized("base.claim.protected")
            }
        }
    }

    fun protectChunk(event: Cancellable, chunk: Chunk?) = loggedTransaction {
        if (!shouldProtect(chunk))
            return@loggedTransaction

        event.isCancelled = true
    }

    fun shouldProtect(chunk: Chunk?): Boolean {
        val claim = chunk?.getFactionClaim()
        val claimZone = claim?.zone()
        if (claim?.zoneType != zoneType || claimZone?.protectAlways == false && claim.factionId == noFactionId)
            return false

        val isRaidable = ChunkPosition(chunk.x, chunk.z, claim.world).getFactionClaim()
            ?.let { claimClusters.getCluster(it) }
            ?.let { it.findAdditionalType() as? FactionCluster }?.isUnprotected(chunk.x, chunk.z, chunk.world.name)
            ?: false
        return !isRaidable
    }
}