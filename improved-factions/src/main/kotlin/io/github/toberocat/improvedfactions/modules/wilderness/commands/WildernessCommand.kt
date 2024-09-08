package io.github.toberocat.improvedfactions.modules.wilderness.commands

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.modules.wilderness.WildernessModule
import io.github.toberocat.improvedfactions.modules.wilderness.config.WildernessModuleConfig
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.utils.PlayerTeleporter
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.limit.PlayerUsageLimits
import io.github.toberocat.improvedfactions.utils.options.limit.UsageLimitOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.exceptions.CommandException
import io.github.toberocat.toberocore.command.options.CooldownOption
import io.github.toberocat.toberocore.command.options.CooldownTabOption
import io.github.toberocat.toberocore.command.options.Options
import io.github.toberocat.toberocore.util.CooldownManager
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

@CommandMeta(
    category = CommandCategory.GENERAL_CATEGORY,
    description = "wilderness.command.wilderness.description",
    module = WildernessModule.MODULE_NAME

)
class WildernessCommand(
    private val plugin: ImprovedFactionsPlugin,
    private val config: WildernessModuleConfig
) : PlayerSubCommand("wilderness") {
    private val cooldownManager = CooldownManager.createManager(config.timeUnit, config.cooldown)
    override fun options() = Options.getFromConfig(plugin, label) { options, _ ->
        options.cmdOpt(CooldownOption(plugin, cooldownManager))
            .tabOpt(CooldownTabOption(cooldownManager, false))
            .cmdOpt(UsageLimitOption(config.usageLimit, "wilderness"))
    }

    override fun arguments() = arrayOf<Argument<*>>()

    override fun handle(player: Player, args: Array<String>): Boolean {
        val location = config.getRandomLocation(player.location)
        if (location == null) {
            cancelCommand(player)
            throw CommandException("wilderness.command.wilderness.failed-to-find", emptyMap())
        }

        PlayerTeleporter(
            plugin,
            player,
            "wilderness.messages.teleporting-title",
            "wilderness.messages.teleporting-subtitle",
            { teleport(location, player) },
            config.standStillUnit.toMillis(config.standStillValue)
        ).startTeleport()
        return true
    }

    private fun teleport(location: Location, player: Player) {
        if (config.gainResistance && !player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
            player.addPotionEffect(
                PotionEffect(
                    PotionEffectType.DAMAGE_RESISTANCE,
                    config.resistanceDuration * 20,
                    config.resistanceAmplifier,
                    false,
                    false,
                    true
                )
            )
        }
        player.teleport(location.add(0.0, 2.0, 0.0))
        player.sendLocalized("wilderness.command.wilderness.teleported", emptyMap())
    }

    private fun cancelCommand(player: Player) {
        cooldownManager.removeCooldown(player.uniqueId)
        loggedTransaction {
            PlayerUsageLimits.getUsageLimit("wilderness", player.uniqueId).used--
        }
    }
}