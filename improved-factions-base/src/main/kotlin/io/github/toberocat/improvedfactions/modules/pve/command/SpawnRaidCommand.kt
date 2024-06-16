package io.github.toberocat.improvedfactions.modules.pve.command

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.modules.pve.handle.PveModuleHandle
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.utils.arguments.entity.FactionArgument
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.addFactionNameOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

@CommandMeta(
    description = "pve.commands.spawnraids.description",
    category = CommandCategory.ADMIN_CATEGORY
)
class SpawnRaidCommand(
    private val plugin: ImprovedFactionsPlugin,
    private val handle: PveModuleHandle
) : PlayerSubCommand("spawnraid") {
    override fun options(): Options = Options.getFromConfig(plugin, label) { options, _ ->
        options.addFactionNameOption(0)
    }

    override fun arguments() = arrayOf(
        FactionArgument()
    )

    override fun handle(player: Player, args: Array<String>): Boolean {
        val faction = parseArgs(player, args).get<Faction>(0) ?: return false
        handle.spawnRaid(faction)
        player.sendLocalized("pve.commands.spawnraids.success", mapOf("faction" to faction.name))
        return true
    }
}