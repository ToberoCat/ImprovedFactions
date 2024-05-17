package io.github.toberocat.improvedfactions.listeners.claim

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.clustering.Position
import io.github.toberocat.improvedfactions.claims.getFactionClaim
import io.github.toberocat.improvedfactions.managers.ByPassManager
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.user.noFactionId
import org.bukkit.Chunk
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Listener
import org.jetbrains.exposed.sql.transactions.transaction

abstract class ProtectionListener(protected val zoneType: String,
                                  private val sendMessage: Boolean = true) : Listener {
    private val claimClusters = ImprovedFactionsPlugin.instance.claimChunkClusters
    abstract fun namespace(): String

    protected fun protectChunk(event: Cancellable, entity: Entity?, player: Player) =
        protectChunk(event, entity?.location?.chunk, player)


    protected fun protectChunk(event: Cancellable, block: Block?, player: Player) {
        if (ByPassManager.isBypassing(player.uniqueId)) return
        protectChunk(event, block?.chunk, player)
    }

    private fun protectChunk(event: Cancellable, chunk: Chunk?, player: Player) = transaction {
        val claim = chunk?.getFactionClaim()
        val claimZone = claim?.zone()
        if (claim?.zoneType != zoneType || (claimZone?.protectAlways == false && claim.factionId == noFactionId))
            return@transaction

        val claimedFaction = claim.factionId
        val playerFaction = player.factionUser().factionId
        if (claimedFaction == playerFaction)
            return@transaction

        if (claimClusters.getCluster(Position(chunk.x, chunk.z, claim.world, claimedFaction))
                ?.isUnprotected(chunk.x, chunk.z, chunk.world.name) == true
        )
            return@transaction

        event.isCancelled = true
        if (!sendMessage) {
            return@transaction
        }

        when (claimZone?.protectAlways) {
            true -> player.sendLocalized("base.zone.protected")
            else -> player.sendLocalized("base.claim.protected")
        }
    }
}