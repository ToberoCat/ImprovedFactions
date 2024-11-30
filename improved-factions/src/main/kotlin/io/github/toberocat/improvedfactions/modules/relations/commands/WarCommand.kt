package io.github.toberocat.improvedfactions.modules.relations.commands

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandMeta
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.modules.relations.RelationsModule.declareWar
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.arguments.entity.FactionArgument
import io.github.toberocat.improvedfactions.utils.options.FactionPermissionOption
import io.github.toberocat.improvedfactions.utils.options.InFactionOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.ArgLengthOption
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

@CommandMeta(
    description = "relations.command.war.description",
    module = "relations",
    category = CommandCategory.RELATIONS_CATEGORY
)
class WarCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("war") {
    override fun options(): Options = Options.getFromConfig(plugin, label) { options, _ ->
        options//.addFactionNameOption(0)
            .cmdOpt(InFactionOption(true))
            .cmdOpt(FactionPermissionOption(Permissions.MANAGE_RELATION))
            .cmdOpt(ArgLengthOption(1))
    }

    override fun arguments(): Array<Argument<*>> = arrayOf(
        FactionArgument()
    )

    override fun handle(player: Player, args: Array<String>): Boolean {
        val arguments = parseArgs(player, args)
        loggedTransaction {
            val targetFaction = arguments.get<Faction>(0) ?: return@loggedTransaction false
            val faction = player.factionUser().faction() ?: return@loggedTransaction false
            faction.declareWar(targetFaction)
            player.sendLocalized("relations.command.war.success")
        }
        return true
    }
}