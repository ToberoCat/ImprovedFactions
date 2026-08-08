package io.github.toberocat.improvedfactions.listeners.move

import io.github.toberocat.improvedfactions.claims.overclaim.ClaimSiegeManager
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.database.storage.claimKey
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.utils.toAudience
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class MoveListener : Listener {
    private val territoryListener = TerritoryTitle(BaseModule.config)
    private val raidableBossBar = RaidableBossBar()

    @EventHandler
    fun playerMove(event: PlayerMoveEvent) {
        val to = event.to?.chunk
        val from = event.from.chunk
        if (to == event.from.chunk) return

        val cache = StorageManager.cache
        if (!cache.isReady()) return
        val toClaim = to?.let { cache.claim(it.claimKey()) }
        val fromClaim = cache.claim(from.claimKey())
        val audience = event.player.toAudience()

        val toFaction = toClaim?.let { cache.faction(it.factionId) }
        val isRaidable = toClaim?.isRaidable == true

        fromClaim?.let(ClaimSiegeManager::getManager)?.leaveClaimCombat(event.player)
        if (isRaidable) toClaim?.let(ClaimSiegeManager::getManager)?.enterClaimCombat(event.player)

        raidableBossBar.claimChanged(isRaidable, event.player, audience)
        territoryListener.claimChanged(toClaim, fromClaim, toFaction, isRaidable, event.player)
    }



}
