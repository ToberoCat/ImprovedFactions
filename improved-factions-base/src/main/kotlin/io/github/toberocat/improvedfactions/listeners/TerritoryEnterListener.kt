package io.github.toberocat.improvedfactions.listeners

import io.github.toberocat.improvedfactions.claims.FactionClaim
import io.github.toberocat.improvedfactions.claims.clustering.ClaimClusterDetector
import io.github.toberocat.improvedfactions.claims.clustering.Position
import io.github.toberocat.improvedfactions.claims.getFactionClaim
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.translation.TranslatedBossBars
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

class TerritoryEnterListener(private val claimCluster: ClaimClusterDetector) : Listener {
    @EventHandler
    fun playerMove(event: PlayerMoveEvent) {
        val to = event.to?.chunk
        val from = event.from.chunk
        if (to == event.from.chunk) return
        transaction {
            val toClaim = to?.getFactionClaim()
            val fromClaim = from.getFactionClaim()
            val audience = event.player.toAudience()

            val toFaction = toClaim?.faction()
            val isRaidable = isRaidable(toClaim, toFaction)

            showRaidableBossbar(isRaidable, event.player, audience)
            if (hideTerritoryAnnouncement(toClaim, fromClaim)) return@transaction

            val toZone = toClaim?.zone()
            val key = when {
                toFaction != null -> "base.claim-faction-territory"
                else -> toZone?.noFactionTitle ?: "base.zone.wilderness"
            }
            audience.sendTitlePart(
                TitlePart.TITLE, event.player.getLocalized(
                    key, mapOf(
                        "faction" to (toFaction?.name ?: "Wilderness")
                    )
                )
            )
            if (!isRaidable) {
                audience.sendTitlePart(TitlePart.SUBTITLE, Component.empty())
                return@transaction
            }
            audience.sendTitlePart(
                TitlePart.SUBTITLE, event.player.getLocalized(
                    "base.claim-faction-territory.subtitles.unprotected"
                )
            )
        }
    }

    private fun showRaidableBossbar(isRaidable: Boolean, player: Player, audience: Audience) {
        val raidableBossbar = player.getBossBar(TranslatedBossBarsType.RAIDABLE, ::createRaidableBar)
        when {
            isRaidable -> audience.showBossBar(raidableBossbar)
            else -> audience.hideBossBar(raidableBossbar)
        }
    }

    private fun isRaidable(toClaim: FactionClaim?, toFaction: Faction?): Boolean {
        if (toClaim == null)
            return false

        val cluster = claimCluster.getCluster(
            Position(
                toClaim.chunkX,
                toClaim.chunkZ,
                toFaction?.id?.value ?: noFactionId
            )
        )
        return cluster != null && cluster.isUnprotected(toClaim.chunkX, toClaim.chunkZ)
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

    private fun createRaidableBar(name: Component) =
        BossBar.bossBar(name, 1f, BossBar.Color.RED, BossBar.Overlay.PROGRESS)
}