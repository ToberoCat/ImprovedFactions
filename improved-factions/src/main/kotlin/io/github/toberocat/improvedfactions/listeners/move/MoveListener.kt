package io.github.toberocat.improvedfactions.listeners.move

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.getFactionClaim
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.utils.toAudience
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class MoveListener(plugin: ImprovedFactionsPlugin) : Listener {
    private val territoryListener = TerritoryTitle(plugin.improvedFactionsConfig)
    private val raidableBossBar = RaidableBossBar()
    @EventHandler
    fun playerMove(event: PlayerMoveEvent) {
        val to = event.to?.chunk
        val from = event.from.chunk
        if (to == event.from.chunk) return

        loggedTransaction {
            val toClaim = to?.getFactionClaim()
            val fromClaim = from.getFactionClaim()
            val audience = event.player.toAudience()

            val toFaction = toClaim?.faction()
            val isRaidable = toClaim?.isRaidable() == true

            fromClaim?.siegeManager?.leaveClaimCombat(event.player)
            if (isRaidable)
                toClaim?.siegeManager?.enterClaimCombat(event.player)

            raidableBossBar.claimChanged(isRaidable, event.player, audience)
            territoryListener.claimChanged(toClaim, fromClaim, toFaction, isRaidable, event.player)
        }
    }



}