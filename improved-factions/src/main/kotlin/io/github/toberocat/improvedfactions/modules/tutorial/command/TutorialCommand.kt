package io.github.toberocat.improvedfactions.modules.tutorial.command

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.modules.tutorial.Tutorial
import io.github.toberocat.improvedfactions.modules.tutorial.scene.SampleScene
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.ArgLengthOption
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

@CommandMeta(
    description = "tutorials.command.tutorial.description",
    module = "tutorials",
    category = CommandCategory.GENERAL_CATEGORY
)
class TutorialCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("tutorial") {
    override fun options(): Options = Options.getFromConfig(plugin, label)

    override fun arguments(): Array<Argument<*>> = arrayOf(
    )

    override fun handle(player: Player, args: Array<String>): Boolean {
        val arguments = parseArgs(player, args)
        Tutorial(
            player, plugin, listOf(
                SampleScene(player, plugin)
            )
        )
        return true
    }
}