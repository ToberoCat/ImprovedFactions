package io.github.toberocat.improvedfactions.listeners.claim

import io.github.toberocat.improvedfactions.annotations.localization.Localization
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.database.storage.claimKey
import io.github.toberocat.improvedfactions.managers.ByPassManager
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.noFactionId
import io.github.toberocat.improvedfactions.zone.ZoneHandler
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
    private val cache = StorageManager.cache
    abstract fun namespace(): String

    protected fun protectChunk(event: Cancellable, entity: Entity?, player: Player) =
        protectChunk(event, entity?.location?.chunk, player)


    protected fun protectChunk(event: Cancellable, block: Block?, player: Player) =
        protectChunk(event, block?.chunk, player)

    @Localization("base.claim.protected")
    @Localization("base.zone.protected")
    private fun protectChunk(event: Cancellable, chunk: Chunk?, player: Player) {
        if (ByPassManager.isBypassing(player.uniqueId)) return

        if (chunk == null) return
        if (!cache.isReady()) {
            event.isCancelled = true
            return
        }
        val claim = cache.claim(chunk.claimKey()) ?: return
        val claimZone = ZoneHandler.getZone(claim.zoneType)
        if (claim.zoneType != zoneType || claimZone?.protectAlways == false && claim.factionId == noFactionId) return

        val playerFaction = cache.user(player.uniqueId)?.factionId ?: noFactionId
        if (claim.factionId == playerFaction && playerFaction != noFactionId) return
        if (claim.isRaidable) return

        event.isCancelled = true
        if (sendMessage) {
            when (claimZone?.protectAlways) {
                true -> player.sendLocalized("base.zone.protected")
                else -> player.sendLocalized("base.claim.protected")
            }
        }
    }

    fun protectChunk(event: Cancellable, chunk: Chunk?) {
        if (shouldProtect(chunk)) event.isCancelled = true
    }

    fun shouldProtect(chunk: Chunk?): Boolean {
        if (chunk == null) return false
        if (!cache.isReady()) return true
        val claim = cache.claim(chunk.claimKey()) ?: return false
        val claimZone = ZoneHandler.getZone(claim.zoneType)
        if (claim.zoneType != zoneType || claimZone?.protectAlways == false && claim.factionId == noFactionId)
            return false
        return !claim.isRaidable
    }
}
