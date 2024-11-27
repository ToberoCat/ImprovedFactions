package io.github.toberocat.improvedfactions.modules.relations.commands

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.annotations.CommandCategory
import io.github.toberocat.improvedfactions.annotations.CommandMeta
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

@CommandMeta(
    description = "base.command.peace.description",
    module = "relations",
    category = CommandCategory.GENERAL_CATEGORY
)
class PeaceCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("peace") {
    override fun options(): Options = Options.getFromConfig(plugin, label) { options, _ ->
    }

    override fun arguments(): Array<Argument<*>> = arrayOf(
    )

    override fun handle(player: Player, args: Array<String>): Boolean {
        val arguments = parseArgs(player, args)
        TODO("Not yet implemented")
    }
}