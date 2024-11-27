package io.github.toberocat.improvedfactions.commands.rank

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.ranks.FactionRank
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.utils.arguments.entity.RankArgument
import io.github.toberocat.improvedfactions.annotations.CommandCategory
import io.github.toberocat.improvedfactions.annotations.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.InFactionOption
import io.github.toberocat.improvedfactions.utils.options.RankNameOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.ArgLengthOption
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

const val EDIT_RANK_COMMAND_DESCRIPTION = "base.command.rank.edit.description"
const val EDIT_RANK_COMMAND_CATEGORY = CommandCategory.PERMISSION_CATEGORY


@CommandMeta(
    description = EDIT_RANK_COMMAND_DESCRIPTION,
    category = EDIT_RANK_COMMAND_CATEGORY
)
open class EditPermissionsCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("edit") {
    override fun options(): Options = Options.getFromConfig(plugin, label)
        .cmdOpt(InFactionOption(true))
        .cmdOpt(RankNameOption(0))
        .cmdOpt(ArgLengthOption(1))

    override fun arguments(): Array<Argument<*>> = arrayOf(
        RankArgument()
    )

    override fun handle(player: Player, args: Array<out String>): Boolean {
        val rank = parseArgs(player, args).get<FactionRank>(0) ?: return false

        player.sendLocalized("base.command.rank.edit.header")
        loggedTransaction {
            rank.permissions().forEach {
                player.sendLocalized("base.command.rank.edit.permission-details", mapOf(
                    "rank" to rank.name,
                    "permission" to it.permission,
                    "value" to it.allowed.toString()
                ))
            }
        }
        return true
    }
}