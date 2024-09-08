package io.github.toberocat.improvedfactions.listeners.move

import io.github.toberocat.improvedfactions.claims.FactionClaim
import io.github.toberocat.improvedfactions.config.ImprovedFactionsConfig
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.user.noFactionId
import io.github.toberocat.improvedfactions.zone.ZoneHandler
import org.bukkit.entity.Player
import org.bukkit.event.Listener

class TerritoryTitle(private val pluginConfig: ImprovedFactionsConfig) : Listener {


    fun claimChanged(
        toClaim: FactionClaim?,
        fromClaim: FactionClaim?,
        toFaction: Faction?,
        isRaidable: Boolean,
        player: Player
    ) {
        if (hideTerritoryAnnouncement(toClaim, fromClaim)) return

        val toZone = toClaim?.zone()
        val key = when {
            toFaction != null -> "base.claim-faction-territory"
            else -> toZone?.noFactionTitle ?: "base.zone.wilderness"
        }

        pluginConfig.territoryDisplayLocation.display(
            player,
            key,
            when {
                isRaidable -> "base.claim-faction-territory.subtitles.unprotected"
                else -> null
            },
            mapOf("faction" to (toFaction?.name ?: "Wilderness"))
        )
    }

    private fun hideTerritoryAnnouncement(toClaim: FactionClaim?, fromClaim: FactionClaim?): Boolean {
        val toZone = toClaim?.zone()
        if (toZone == null && pluginConfig.hideWildernessTitle) return false
        return when (toZone?.announceTitle) {
            false -> true
            else -> {
                val toFaction = toClaim?.faction()
                val fromFaction = fromClaim?.factionId ?: noFactionId

                val fromZoneType = fromClaim?.zoneType ?: ZoneHandler.FACTION_ZONE_TYPE
                val toZoneType = toZone?.type ?: ZoneHandler.FACTION_ZONE_TYPE
                val toFactionId = toFaction?.id?.value ?: noFactionId
                return toFactionId == fromFaction && toZoneType == fromZoneType
            }
        }
    }
}