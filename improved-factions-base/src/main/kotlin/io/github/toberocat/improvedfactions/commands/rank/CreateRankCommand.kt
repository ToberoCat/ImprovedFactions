package io.github.toberocat.improvedfactions.commands.rank

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.ranks.FactionRankHandler
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.arguments.PriorityArgument
import io.github.toberocat.improvedfactions.utils.arguments.RankNameInputArgument
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.InFactionOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.ArgLengthOption
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

@CommandMeta(
    category = CommandCategory.PERMISSION_CATEGORY,
    description = "base.command.rank.create.description"
)
class CreateRankCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("create") {

    override fun options(): Options =
        Options.getFromConfig(plugin, label)
            .cmdOpt(InFactionOption(true))
            .cmdOpt(ArgLengthOption(2))


    override fun arguments(): Array<Argument<*>> = arrayOf(
        RankNameInputArgument(),
        PriorityArgument()
    )

    override fun handle(player: Player, args: Array<out String>): Boolean {
        val arguments = parseArgs(player, args)
        val rank = arguments.get<String>(0) ?: return false
        val priority = arguments.get<Int>(1) ?: return false
        loggedTransaction {
            val faction = player.factionUser().faction() ?: return@loggedTransaction
            FactionRankHandler.createRank(faction.id.value, rank, priority, emptyList())
        }
        player.sendLocalized("base.command.rank.create.created")
        return true
    }
}