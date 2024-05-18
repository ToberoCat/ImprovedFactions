package io.github.toberocat.improvedfactions.commands.invite

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.invites.FactionInvite
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.utils.arguments.entity.InviteIdArgument
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.InFactionOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.exceptions.CommandException
import io.github.toberocat.toberocore.command.options.ArgLengthOption
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

@CommandMeta(
    description = "base.command.inviteaccept.description",
    category = CommandCategory.INVITE_CATEGORY
)
class InviteAcceptCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("inviteaccept") {
    override fun options(): Options = Options.getFromConfig(plugin, "inviteaccept") { options, _ ->
        options.opt(InFactionOption(false)).opt(ArgLengthOption(1))
    }

    override fun arguments(): Array<Argument<*>> = arrayOf(
        InviteIdArgument()
    )

    override fun handle(player: Player, args: Array<out String>): Boolean {
        val invite = parseArgs(player, args).get<FactionInvite>(0) ?: return false
        loggedTransaction {
            val faction = Faction.findById(invite.factionId)
                ?: throw CommandException("base.command.inviteaccept.faction-deleted", emptyMap())
            invite.delete()
            faction.join(player.uniqueId, invite.rankId)
        }

        player.sendLocalized("base.command.inviteaccept.joined")
        return true
    }
}