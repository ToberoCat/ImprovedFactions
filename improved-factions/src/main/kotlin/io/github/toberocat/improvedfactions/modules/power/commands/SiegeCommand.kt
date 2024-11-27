package io.github.toberocat.improvedfactions.modules.power.commands

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.getFactionClaim
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule
import io.github.toberocat.improvedfactions.modules.power.impl.FactionPowerRaidModuleHandleImpl
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.user.noFactionId
import io.github.toberocat.improvedfactions.annotations.CommandCategory
import io.github.toberocat.improvedfactions.annotations.CommandMeta
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

@CommandMeta(
    description = "base.command.power.siege.description",
    category = CommandCategory.POWER_CATEGORY,
    module = PowerRaidsModule.MODULE_NAME
)
class SiegeCommand(
    private val plugin: ImprovedFactionsPlugin,
    private val powerHandle: FactionPowerRaidModuleHandleImpl
) : PlayerSubCommand("siege") {
    override fun options(): Options = Options.getFromConfig(plugin, label)

    override fun arguments(): Array<Argument<*>> = arrayOf()

    override fun handle(player: Player, args: Array<String>) = loggedTransaction {
        val claim = player.location.getFactionClaim()
        if (claim == null || claim.factionId == noFactionId) {
            player.sendLocalized("power.siege.not-in-claim")
            return@loggedTransaction true
        }
        if (claim.factionId == player.factionUser().factionId) {
            player.sendLocalized("power.siege.own-claim")
            return@loggedTransaction true
        }

        claim.siegeManager.startSiege(player)
        player.sendLocalized("power.siege.entered")
        return@loggedTransaction true
    }

}