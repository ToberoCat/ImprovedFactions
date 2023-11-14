package io.github.toberocat.improvedfactions.commands.manage

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.factions.FactionHandler
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.utils.arguments.FactionNameInputArgument
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.FactionExistOption
import io.github.toberocat.improvedfactions.utils.options.InFactionOption
import io.github.toberocat.improvedfactions.utils.options.addFactionNameOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.ArgLengthOption
import io.github.toberocat.toberocore.command.options.Options
import io.github.toberocat.toberocore.util.placeholder.PlaceholderBuilder
import org.bukkit.entity.Player

/**
 * Created: 04.08.2023
 * @author Tobias Madlberger (Tobias)
 */

@CommandMeta(
    description = "base.command.create.description",
    category = CommandCategory.MANAGE_CATEGORY
)
class CreateCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("create") {
    override fun options(): Options = Options.getFromConfig(plugin, "create") { options, _ ->
        options
            .opt(InFactionOption(false))
            .addFactionNameOption(0)
            .opt(ArgLengthOption(1))
            .opt(FactionExistOption(0, false))
    }

    override fun arguments(): Array<Argument<*>> = arrayOf(
        FactionNameInputArgument()
    )

    override fun handle(player: Player, args: Array<out String>): Boolean {
        val name = parseArgs(player, args).get<String>(0) ?: return false
        val faction: Faction = FactionHandler.createFaction(player.uniqueId, name)
        player.sendLocalized(
            "base.commands.create.created-faction", PlaceholderBuilder()
                .placeholder("faction", faction)
                .placeholders
        )
        return true
    }
}