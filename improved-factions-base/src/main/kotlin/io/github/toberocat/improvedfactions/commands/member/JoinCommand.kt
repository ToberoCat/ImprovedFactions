package io.github.toberocat.improvedfactions.commands.member

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.utils.arguments.entity.FactionArgument
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.FactionExistOption
import io.github.toberocat.improvedfactions.utils.options.InFactionOption
import io.github.toberocat.improvedfactions.utils.options.addFactionNameOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.ArgLengthOption
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

/**
 * Created: 05.08.2023
 * @author Tobias Madlberger (Tobias)
 */

@CommandMeta(
    description = "base.command.join.description",
    category = CommandCategory.MEMBER_CATEGORY
)
class JoinCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("join") {
    override fun options(): Options = Options.getFromConfig(plugin, "join") { options, _ ->
        options.cmdOpt(InFactionOption(false))
            .addFactionNameOption(0)
            .cmdOpt(ArgLengthOption(1))
            .cmdOpt(FactionExistOption(0, true))
    }

    override fun arguments(): Array<Argument<*>> = arrayOf(
        FactionArgument()
    )

    override fun handle(player: Player, args: Array<out String>): Boolean {
        val faction = parseArgs(player, args).get<Faction>(0) ?: return false
        loggedTransaction { faction.join(player.uniqueId, faction.defaultRank) }
        player.sendLocalized("base.command.join.joined")
        return true
    }
}