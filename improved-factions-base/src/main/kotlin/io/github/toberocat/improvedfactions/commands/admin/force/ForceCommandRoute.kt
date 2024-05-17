package io.github.toberocat.improvedfactions.commands.admin.force

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.toberocore.command.CommandRoute
import org.bukkit.Bukkit
import org.bukkit.entity.Player

@CommandMeta(
    description = "base.command.force.description",
    category = CommandCategory.ADMIN_CATEGORY
)
class ForceCommandRoute(plugin: ImprovedFactionsPlugin) : CommandRoute("force", plugin) {

    init {
        addChild(ForceJoinCommand(plugin))
    }

    override fun handle(player: Player, p1: Array<out String>): Boolean {
        Bukkit.dispatchCommand(player, "factions help category:${CommandCategory.ADMIN_CATEGORY}")
        return true
    }
}