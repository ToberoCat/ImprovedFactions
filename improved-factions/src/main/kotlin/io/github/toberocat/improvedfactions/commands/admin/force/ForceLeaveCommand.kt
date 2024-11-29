package io.github.toberocat.improvedfactions.commands.admin.force

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.arguments.OfflinePlayerArgument
import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.PlayerNameOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.exceptions.CommandException
import io.github.toberocat.toberocore.command.options.ArgLengthOption
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

@CommandMeta(
    description = "base.command.force.leave.description",
    category = CommandCategory.ADMIN_CATEGORY
)
class ForceLeaveCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("leave") {
    override fun options(): Options = Options.getFromConfig(plugin, "force.leave") { options, _ ->
        options.cmdOpt(PlayerNameOption(0))
            .cmdOpt(ArgLengthOption(1))
    }

    override fun arguments(): Array<Argument<*>> = arrayOf(
        OfflinePlayerArgument()
    )

    override fun handle(player: Player, args: Array<out String>?): Boolean {
        val arguments = parseArgs(player, args)
        val target = arguments.get<OfflinePlayer>(0) ?: return false

        loggedTransaction {
            val user = target.factionUser()
            val faction = user.faction() ?: throw CommandException("base.command.force.leave.not-in-a-faction", emptyMap())
            runCatching {
                faction.leave(target.uniqueId)
            }.onFailure {
                val nextBestOwner = faction.members()
                    .sortedBy { it.rank().priority }
                    .firstOrNull { it.uniqueId != target.uniqueId }
                when (nextBestOwner) {
                    null -> faction.delete()
                    else -> faction.transferOwnership(nextBestOwner.uniqueId)
                }
            }

            player.sendLocalized("base.command.force.leave.success")
        }
        return true
    }
}