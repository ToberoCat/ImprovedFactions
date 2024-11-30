package io.github.toberocat.improvedfactions.ranks

import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.permissions.FactionPermission
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.user.noFactionId
import io.github.toberocat.toberocore.command.exceptions.CommandException
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.modules.base.BaseModule

fun Faction.listRanks(): SizedIterable<FactionRank> = FactionRank.find { FactionRanks.factionId eq id.value }

fun Faction.anyRank(name: String): FactionRank? =
    FactionRank.find { FactionRanks.name eq name and (FactionRanks.factionId eq id.value) }.firstOrNull()


object FactionRankHandler {
    var guestRankId: Int = 0
    lateinit var guestRank: FactionRank

    fun createRank(
        factionId: Int, rankName: String, priority: Int, allowedPermissions: Collection<String>
    ): FactionRank {
        if (rankName.length > BaseModule.config.maxRankNameLength) throw CommandException(
            "base.exceptions.rank-name-exceeds-limit", emptyMap()
        )
        if (!BaseModule.config.rankNameRegex.matches(rankName))
            throw CommandException("base.exceptions.rank-name-not-matching", emptyMap())

        return loggedTransaction {
            val rank = FactionRank.new {
                this.factionId = factionId
                this.name = rankName
                this.priority = priority
            }

            Permissions.knownPermissions.keys.forEach {
                FactionPermission.new {
                    permission = it
                    rankId = rank.id.value
                    allowed = allowedPermissions.contains(it)
                }
            }
            return@loggedTransaction rank
        }
    }

    fun initRanks() {
        loggedTransaction {
            guestRank = FactionRank.find { FactionRanks.name eq BaseModule.config.guestRankName }.firstOrNull() ?: createRank(
                noFactionId, BaseModule.config.guestRankName, -1, emptyList()
            )
            guestRankId = guestRank.id.value

            val ranks = FactionRank.all()
            ranks.forEach { rank ->
                val leftPermissions =
                    Permissions.knownPermissions.keys - rank.permissions().map { it.permission }.toSet()
                leftPermissions.forEach {
                    FactionPermission.new {
                        permission = it
                        rankId = rank.id.value
                    }
                }
            }
        }
    }
}