package io.github.toberocat.improvedfactions.listeners.move

import io.github.toberocat.improvedfactions.config.ImprovedFactionsConfig
import io.github.toberocat.improvedfactions.database.storage.ClaimSnapshot
import io.github.toberocat.improvedfactions.database.storage.FactionSnapshot
import io.github.toberocat.improvedfactions.user.noFactionId
import io.github.toberocat.improvedfactions.zone.ZoneHandler
import org.bukkit.entity.Player
import org.bukkit.event.Listener

class TerritoryTitle(private val pluginConfig: ImprovedFactionsConfig) : Listener {


    fun claimChanged(
        toClaim: ClaimSnapshot?,
        fromClaim: ClaimSnapshot?,
        toFaction: FactionSnapshot?,
        isRaidable: Boolean,
        player: Player
    ) {
        if (hideTerritoryAnnouncement(toClaim, fromClaim)) return

        val toZone = toClaim?.zoneType?.let(ZoneHandler::getZone)
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

    private fun hideTerritoryAnnouncement(toClaim: ClaimSnapshot?, fromClaim: ClaimSnapshot?): Boolean {
        val toZone = toClaim?.zoneType?.let(ZoneHandler::getZone)
        if (toZone == null && pluginConfig.hideWildernessTitle) return false
        return when (toZone?.announceTitle) {
            false -> true
            else -> {
                val fromFaction = fromClaim?.factionId ?: noFactionId

                val fromZoneType = fromClaim?.zoneType ?: ZoneHandler.FACTION_ZONE_TYPE
                val toZoneType = toZone?.type ?: ZoneHandler.FACTION_ZONE_TYPE
                val toFactionId = toClaim?.factionId ?: noFactionId
                return toFactionId == fromFaction && toZoneType == fromZoneType
            }
        }
    }
}
