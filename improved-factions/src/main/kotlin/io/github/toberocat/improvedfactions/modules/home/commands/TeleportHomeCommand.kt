package io.github.toberocat.improvedfactions.modules.home.commands

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.modules.home.HomeModule
import io.github.toberocat.improvedfactions.modules.home.HomeModule.teleportToFactionHome
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.annotations.CommandCategory
import io.github.toberocat.improvedfactions.annotations.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.FactionPermissionOption
import io.github.toberocat.improvedfactions.utils.options.InFactionOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

@CommandMeta(
    category = CommandCategory.GENERAL_CATEGORY,
    description = "home.commands.teleport-home.description",
    module = HomeModule.MODULE_NAME
)
class TeleportHomeCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("home") {
    override fun options(): Options = Options.getFromConfig(plugin, label) { options, _ ->
        options.cmdOpt(InFactionOption(true))
            .cmdOpt(FactionPermissionOption(Permissions.HOME))
    }

    override fun arguments() = emptyArray<Argument<*>>()
    override fun handle(player: Player, args: Array<String>): Boolean {
        val success = player.teleportToFactionHome() ?: false
        if (!success) {
            player.sendLocalized("home.commands.teleport-home.failed")
        }

        return true
    }
}
