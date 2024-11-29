package io.github.toberocat.improvedfactions.commands.invite

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.invites.FactionInvite
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.utils.arguments.entity.InviteIdArgument
import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandMeta
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.exceptions.CommandException
import io.github.toberocat.toberocore.command.options.ArgLengthOption
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

@CommandMeta(
    description = "base.command.invitediscard.description",
    category = CommandCategory.INVITE_CATEGORY
)
class InviteDiscardCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("invitediscard") {
    override fun options(): Options = Options.getFromConfig(plugin, "invitediscard") { options, _ ->
        options.cmdOpt(ArgLengthOption(1))
    }

    override fun arguments(): Array<Argument<*>> = arrayOf(
        InviteIdArgument()
    )

    override fun handle(player: Player, args: Array<out String>): Boolean {
        val inviteId = parseArgs(player, args).get<FactionInvite>(0) ?: return false
        loggedTransaction {
            val invite = FactionInvite.findById(inviteId.id) ?: throw CommandException(
                "base.command.invitediscard.invalid-invite",
                emptyMap()
            )

            invite.delete()
        }

        player.sendLocalized("base.command.invitediscard.deleted")
        return true
    }
}