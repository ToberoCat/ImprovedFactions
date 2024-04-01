package io.github.toberocat.improvedfactions.modules.wilderness.commands

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.modules.wilderness.config.WildernessModuleConfig
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.CooldownOption
import io.github.toberocat.toberocore.command.options.CooldownTabOption
import io.github.toberocat.toberocore.command.options.Options
import io.github.toberocat.toberocore.util.CooldownManager
import org.bukkit.entity.Player

class WildernessCommand(
    private val plugin: ImprovedFactionsPlugin,
    private val config: WildernessModuleConfig
) : PlayerSubCommand("wilderness") {
    override fun options() = Options.getFromConfig(plugin, label) { options, _ ->
        val cooldownManager = CooldownManager.createManager(config.timeUnit, config.cooldown)
        options.cmdOpt(CooldownOption(plugin, cooldownManager))
            .tabOpt(CooldownTabOption(cooldownManager, false))
    }

    override fun arguments() = arrayOf<Argument<*>>()

    override fun handle(player: Player, args: Array<String>): Boolean {
        return true
    }
}