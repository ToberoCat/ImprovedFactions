package io.github.toberocat.improvedfactions.commands.rank

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.ranks.FactionRank
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.arguments.entity.RankArgument
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.InFactionOption
import io.github.toberocat.improvedfactions.utils.options.RankNameOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.ArgLengthOption
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

@CommandMeta(
    description = "base.command.rank.default.description",
    category = CommandCategory.PERMISSION_CATEGORY
)
class DefaultRankCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("joinas") {
    override fun options(): Options = Options.getFromConfig(plugin, label)
        .cmdOpt(InFactionOption(true))
        .cmdOpt(RankNameOption(0))
        .cmdOpt(ArgLengthOption(1))

    override fun arguments(): Array<Argument<*>> = arrayOf(
        RankArgument()
    )

    override fun handle(player: Player, args: Array<out String>): Boolean {
        val rank = parseArgs(player, args).get<FactionRank>(0) ?: return false
        loggedTransaction {
           player.factionUser().faction()?.defaultRank = rank.id.value
        }
        player.sendLocalized("base.command.rank.default.set")
        return true
    }
}