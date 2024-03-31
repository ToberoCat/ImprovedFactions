package io.github.toberocat.improvedfactions.commands.rank

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.ranks.FactionRank
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.FactionUser
import io.github.toberocat.improvedfactions.user.FactionUsers
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.arguments.FallbackRankArgument
import io.github.toberocat.improvedfactions.utils.arguments.entity.RankArgument
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.InFactionOption
import io.github.toberocat.improvedfactions.utils.options.RankNameOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.exceptions.CommandException
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.transactions.transaction

@CommandMeta(
    description = "base.command.rank.delete.description",
    category = CommandCategory.PERMISSION_CATEGORY
)
class DeleteRankCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("delete") {
    override fun options(): Options =
        Options.getFromConfig(plugin, label).cmdOpt(InFactionOption(true)).cmdOpt(RankNameOption(0))
            .cmdOpt(RankNameOption(1))

    override fun arguments(): Array<Argument<*>> = arrayOf(
        RankArgument(), FallbackRankArgument()
    )

    override fun handle(player: Player, args: Array<out String>): Boolean {
        val arguments = parseArgs(player, args)
        val rank = arguments.get<FactionRank>(0) ?: return false
        val fallbackRank = arguments.get<FactionRank>(1) ?: return false
        transaction {
            if (player.factionUser().faction()?.defaultRank == rank.id.value)
                throw CommandException("base.command.rank.delete.rank-is-default", emptyMap())
            FactionUser.find { FactionUsers.assignedRank eq rank.id.value }
                .forEach { it.assignedRank = fallbackRank.id.value }
            rank.delete()
        }
        player.sendLocalized("base.command.rank.delete.deleted")
        return true
    }
}