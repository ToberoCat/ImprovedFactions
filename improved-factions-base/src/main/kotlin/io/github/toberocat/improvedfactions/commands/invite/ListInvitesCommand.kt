package io.github.toberocat.improvedfactions.commands.invite

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.invites.FactionInvite
import io.github.toberocat.improvedfactions.invites.FactionInvites
import io.github.toberocat.improvedfactions.ranks.FactionRank
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

const val INVITES_COMMAND_DESCRIPTION = "base.command.invites.description"
const val INVITES_COMMAND_CATEGORY = CommandCategory.INVITE_CATEGORY

@CommandMeta(
    description = INVITES_COMMAND_DESCRIPTION,
    category = INVITES_COMMAND_CATEGORY
)
open class ListInvitesCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("invites") {
    override fun options(): Options = Options.getFromConfig(plugin, label)

    override fun arguments(): Array<Argument<*>> = emptyArray()
    override fun handle(player: Player, args: Array<String>): Boolean {
        val invitedId = player.factionUser().id.value
        player.sendLocalized("base.command.invites.header")
        loggedTransaction {
            FactionInvite.find { FactionInvites.invitedId eq invitedId }.forEach { invite ->
                val faction = Faction.findById(invite.factionId) ?: return@forEach
                val rankId = FactionRank.findById(invite.rankId) ?: return@forEach
                player.sendLocalized("base.command.invites.detail", mapOf(
                    "faction" to faction.name,
                    "rank" to rankId.name,
                    "expires" to invite.expiresInFormatted(),
                    "id" to invite.id.value.toString()
                ))
            }
        }
        return true
    }
}