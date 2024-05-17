package io.github.toberocat.improvedfactions.commands.admin

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.managers.ByPassManager
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.utils.arguments.OptionalPlayerArgument
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.PlayerNameOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

@CommandMeta(
    category = CommandCategory.ADMIN_CATEGORY,
    description = "base.commands.bypass.description"
)
class ByPassCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("bypass") {
    override fun options(): Options = Options.getFromConfig(plugin, label) { options, _ ->
        options.opt(PlayerNameOption(0))
    }

    override fun arguments(): Array<Argument<*>> = arrayOf(
        OptionalPlayerArgument()
    )
    override fun handle(executor: Player, args: Array<out String>): Boolean {
        val target = parseArgs(executor, args).get<Player>(0) ?: return false
        val targetId = target.uniqueId
        when (ByPassManager.isBypassing(targetId)) {
            true -> {
                ByPassManager.removeBypass(targetId)
                executor.sendLocalized("base.commands.bypass.remove-bypass")
            }
            false -> {
                ByPassManager.addBypass(targetId)
                executor.sendLocalized("base.commands.bypass.add-bypass")
            }
        }
        return true
    }
}