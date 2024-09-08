package io.github.toberocat.improvedfactions.commands.member

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
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
        loggedTransaction { player.factionUser().faction()?.transferOwnership(user.uniqueId) }
        return true
    }
}