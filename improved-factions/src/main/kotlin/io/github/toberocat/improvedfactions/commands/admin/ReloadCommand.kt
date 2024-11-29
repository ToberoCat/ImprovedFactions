package io.github.toberocat.improvedfactions.commands.admin

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandMeta
import io.github.toberocat.toberocore.command.SubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.command.CommandSender
import java.util.*

@CommandMeta(
    description = "base.command.reload.description",
    category = CommandCategory.ADMIN_CATEGORY
)
class ReloadCommand(private val plugin: ImprovedFactionsPlugin) : SubCommand("reload") {
    override fun options(): Options = Options.getFromConfig(plugin, label)

    override fun arguments(): Array<Argument<*>> = emptyArray()

    override fun handleCommand(sender: CommandSender, args: Array<out String>): Boolean {
        sender.sendMessage("Plugin reload scheduled...")

        plugin.reloadConfig()
        plugin.loadConfig()
        ResourceBundle.clearCache()

        sender.sendMessage("Plugin has been reloaded")
        return true
    }
}