package io.github.toberocat.improvedfactions.invites

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.ranks.FactionRankHandler
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.bukkit.Bukkit
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.transactions.transaction

object FactionInvites : IntIdTable("faction_invites") {
    var inviteExpiresInMinutes = 5

    val inviterId = integer("inviter_id")
    val invitedId = integer("invited_id")
    val factionId = integer("faction_id")
    val rankId = integer("rank_id").default(FactionRankHandler.guestRankId)
    val expirationDate = datetime("expiration_date")

    fun scheduleInviteExpirations() = FactionInvite.all().forEach {
        val ticksTillExpired = (it.expirationDate.toInstant(TimeZone.UTC) - Clock.System.now()).inWholeSeconds * 20
        if (ticksTillExpired <= 0) {
            it.delete()
            return@forEach
        }

        Bukkit.getScheduler().runTaskLater(
            ImprovedFactionsPlugin.instance, Runnable { transaction { it.delete() } }, ticksTillExpired
        )
    }
}