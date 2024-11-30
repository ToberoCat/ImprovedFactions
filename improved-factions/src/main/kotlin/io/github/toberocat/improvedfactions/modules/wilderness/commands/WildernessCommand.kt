package io.github.toberocat.improvedfactions.modules.wilderness.commands

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.annotations.localization.Localization
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.sendCommandResult
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.modules.wilderness.WildernessModule
import io.github.toberocat.improvedfactions.modules.wilderness.config.WildernessModuleConfig
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.utils.PlayerTeleporter
import io.github.toberocat.improvedfactions.utils.options.limit.PlayerUsageLimits
import io.github.toberocat.toberocore.util.CooldownManager
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

@GeneratedCommandMeta(
    label = "wilderness",
    category = CommandCategory.GENERAL_CATEGORY,
    module = WildernessModule.MODULE_NAME,
    responses = [
        CommandResponse("teleportStarted"),
        CommandResponse("teleportSuccess"),
        CommandResponse("teleportFailed"),
    ]
)
abstract class WildernessCommand : WildernessCommandContext() {

    private val cooldownManager = CooldownManager.createManager(
        WildernessModule.config.timeUnit,
        WildernessModule.config.cooldown
    )

    @Localization("wilderness.messages.teleporting-title")
    @Localization("wilderness.messages.teleporting-subtitle")
    fun process(player: Player): CommandProcessResult {
        val location = WildernessModule.config.getRandomLocation(player.location)

        if (location == null) {
            cancelCommand(player)
            return teleportFailed()
        }

        PlayerTeleporter(
            plugin = BaseModule.plugin,
            player = player,
            titleKey = "wilderness.messages.teleporting-title",
            subtitleKey = "wilderness.messages.teleporting-subtitle",
            { teleport(location, player) },
            WildernessModule.config.standStillUnit.toMillis(WildernessModule.config.standStillValue)
        ).startTeleport()

        return teleportStarted()
    }

    private fun teleport(location: Location, player: Player) {
        if (WildernessModule.config.gainResistance && !player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
            player.addPotionEffect(
                PotionEffect(
                    PotionEffectType.DAMAGE_RESISTANCE,
                    WildernessModule.config.resistanceDuration * 20,
                    WildernessModule.config.resistanceAmplifier,
                    false,
                    false,
                    true
                )
            )
        }

        player.teleport(location.add(0.0, 2.0, 0.0))
        player.sendCommandResult(teleportSuccess())
    }

    private fun cancelCommand(player: Player) {
        cooldownManager.removeCooldown(player.uniqueId)
        PlayerUsageLimits.getUsageLimit("wilderness", player.uniqueId).used--
    }
}