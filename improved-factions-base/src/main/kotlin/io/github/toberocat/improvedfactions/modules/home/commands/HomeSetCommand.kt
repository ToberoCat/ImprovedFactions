package io.github.toberocat.improvedfactions.modules.home.commands

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.modules.home.HomeModule
import io.github.toberocat.improvedfactions.modules.home.HomeModule.setHome
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.FactionPermissionOption
import io.github.toberocat.improvedfactions.utils.options.InFactionOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

@CommandMeta(
    category = CommandCategory.MANAGE_CATEGORY,
    description = "home.commands.sethome.description",
    module = HomeModule.MODULE_NAME
)
class HomeSetCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("sethome") {
    override fun options(): Options = Options.getFromConfig(plugin, label) { options, _ ->
        options.cmdOpt(InFactionOption(true))
            .cmdOpt(FactionPermissionOption(Permissions.SET_HOME))
    }

    override fun arguments() = emptyArray<Argument<*>>()
    override fun handle(player: Player, args: Array<String>): Boolean {
        loggedTransaction { player.factionUser().faction()?.setHome(player.location) }
            ?: player.sendLocalized("home.commands.sethome.failed")
        player.sendLocalized("home.commands.sethome.success")
        return true
    }
}