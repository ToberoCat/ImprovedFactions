package io.github.toberocat.improvedfactions.listeners.move

import io.github.toberocat.improvedfactions.claims.FactionClaim
import io.github.toberocat.improvedfactions.claims.clustering.ClaimClusterDetector
import io.github.toberocat.improvedfactions.claims.clustering.Position
import io.github.toberocat.improvedfactions.claims.getFactionClaim
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.translation.TranslatedBossBars.getBossBar
import io.github.toberocat.improvedfactions.translation.TranslatedBossBarsType
import io.github.toberocat.improvedfactions.translation.getLocalized
import io.github.toberocat.improvedfactions.user.noFactionId
import io.github.toberocat.improvedfactions.utils.toAudience
import io.github.toberocat.improvedfactions.zone.ZoneHandler
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.TitlePart
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.jetbrains.exposed.sql.transactions.transaction

class TerritoryTitle : Listener {
    fun claimChanged(
        toClaim: FactionClaim?,
        fromClaim: FactionClaim?,
        toFaction: Faction?,
        audience: Audience,
        isRaidable: Boolean,
        player: Player
    ) {
        if (hideTerritoryAnnouncement(toClaim, fromClaim)) return

        val toZone = toClaim?.zone()
        val key = when {
            toFaction != null -> "base.claim-faction-territory"
            else -> toZone?.noFactionTitle ?: "base.zone.wilderness"
        }
        audience.sendTitlePart(
            TitlePart.TITLE, player.getLocalized(
                key, mapOf(
                    "faction" to (toFaction?.name ?: "Wilderness")
                )
            )
        )
        if (!isRaidable) {
            audience.sendTitlePart(TitlePart.SUBTITLE, Component.empty())
            return
        }
        audience.sendTitlePart(
            TitlePart.SUBTITLE, player.getLocalized(
                "base.claim-faction-territory.subtitles.unprotected"
            )
        )
    }

    private fun hideTerritoryAnnouncement(toClaim: FactionClaim?, fromClaim: FactionClaim?): Boolean {
        val toZone = toClaim?.zone()
        return when (toZone?.announceTitle) {
            false -> true
            else -> {
                val toFaction = toClaim?.faction()
                val fromFaction = fromClaim?.factionId ?: noFactionId

                val fromZoneType = fromClaim?.zoneType
                (toFaction?.id?.value ?: noFactionId) == fromFaction && (toZone?.type
                    ?: ZoneHandler.FACTION_ZONE_TYPE) == (fromZoneType ?: ZoneHandler.FACTION_ZONE_TYPE)
            }
        }
    }
}