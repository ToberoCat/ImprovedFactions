package io.github.toberocat.improvedfactions.commands.member

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.FactionUser
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.options.ConfirmArgOption
import io.github.toberocat.improvedfactions.utils.options.FactionPermissionOption
import io.github.toberocat.improvedfactions.utils.options.PlayerNameOption
import io.github.toberocat.improvedfactions.utils.arguments.entity.FactionMemberArgument
import io.github.toberocat.improvedfactions.utils.options.InFactionOption
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.exceptions.CommandException
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

@CommandMeta(
    description = "base.commands.transferowner.description",
    category = CommandCategory.MEMBER_CATEGORY
)
class TransferOwnershipCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("transferowner") {
    override fun options(): Options = Options.getFromConfig(plugin, label) { options, _ ->
        options.cmdOpt(PlayerNameOption(0))
            .cmdOpt(ConfirmArgOption(1))
            .cmdOpt(InFactionOption(true))
            .cmdOpt(FactionPermissionOption(Permissions.TRANSFER_OWNERSHIP))
    }

    override fun arguments(): Array<Argument<*>> = arrayOf(
        FactionMemberArgument()
    )
    override fun handle(player: Player, args: Array<out String>): Boolean {
        val user = parseArgs(player, args).get<FactionUser>(0) ?: return false
        if (user.factionId != player.factionUser().factionId)
            throw CommandException("base.exceptions.player-not-in-faction", emptyMap())
        if (user.faction()?.owner == player.uniqueId)
            throw CommandException("base.exceptions.player-already-owner", emptyMap())
        player.factionUser().faction()?.let {
            it.owner = user.uniqueId

            val previousOwner =  player.factionUser()
            user.assignedRank = previousOwner.assignedRank
            previousOwner.assignedRank = it.defaultRank
        }

        player.sendLocalized("base.commands.transferowner.owner-transfered")
        user.player()?.sendLocalized("base.commands.transferowner.youre-the-owner-now")
        return true
    }
}