package io.github.toberocat.improvedfactions.commands.rank

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.ranks.FactionRank
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.arguments.PlayerArgument
import io.github.toberocat.improvedfactions.utils.arguments.entity.RankArgument
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.InFactionOption
import io.github.toberocat.improvedfactions.utils.options.PlayerNameOption
import io.github.toberocat.improvedfactions.utils.options.RankNameOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.ArgLengthOption
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.transactions.transaction

@CommandMeta(
    description = "base.command.rank.assign.description",
    category = CommandCategory.PERMISSION_CATEGORY
)
class AssignRankCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("assign") {
    override fun options(): Options = Options.getFromConfig(plugin, label)
        .cmdOpt(InFactionOption(true))
        .cmdOpt(PlayerNameOption(0))
        .cmdOpt(RankNameOption(1))
        .cmdOpt(ArgLengthOption(2))

    override fun arguments(): Array<Argument<*>> = arrayOf(
        PlayerArgument(),
        RankArgument()
    )

    override fun handle(player: Player, args: Array<out String>): Boolean {
        val arguments = parseArgs(player, args)
        val target = arguments.get<Player>(0) ?: return false
        val rank = arguments.get<FactionRank>(1) ?: return false

        transaction {
            target.factionUser().assignedRank = rank.id.value
        }
        player.sendLocalized("base.command.rank.assign.assigned", emptyMap())
        return true
    }
}