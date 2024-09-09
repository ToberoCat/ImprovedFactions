package io.github.toberocat.improvedfactions.modules.relations.database

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.factions.Factions
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.bukkit.Bukkit
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object FactionAllyInvites : IntIdTable("faction_ally_invites") {
    var inviteExpiresInMinutes = 5

    val sourceFaction = reference("source_faction_id", Factions)
    val targetFaction = reference("target_faction_id", Factions)

    val expirationDate = datetime("expiration_date")

    fun scheduleInviteExpirations() = FactionAllyInvite.all().forEach {
        val ticksTillExpired = (it.expirationDate.toInstant(TimeZone.UTC) - Clock.System.now()).inWholeSeconds * 20
        if (ticksTillExpired <= 0) {
            it.delete()
            return@forEach
        }

        Bukkit.getScheduler().runTaskLater(
            ImprovedFactionsPlugin.instance, Runnable { loggedTransaction { it.delete() } }, ticksTillExpired
        )
    }
}