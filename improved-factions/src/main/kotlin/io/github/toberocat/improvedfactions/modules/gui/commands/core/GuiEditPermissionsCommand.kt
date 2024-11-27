package io.github.toberocat.improvedfactions.modules.gui.commands.core

import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.components.container.tab.PagedContainer
import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.commands.rank.EDIT_RANK_COMMAND_CATEGORY
import io.github.toberocat.improvedfactions.commands.rank.EDIT_RANK_COMMAND_DESCRIPTION
import io.github.toberocat.improvedfactions.commands.rank.EditPermissionsCommand
import io.github.toberocat.improvedfactions.database.DatabaseManager
import io.github.toberocat.improvedfactions.modules.gui.components.permission.FactionPermissionComponentBuilder
import io.github.toberocat.improvedfactions.ranks.FactionRank
import io.github.toberocat.improvedfactions.annotations.CommandMeta
import org.bukkit.entity.Player

@CommandMeta(
    description = EDIT_RANK_COMMAND_DESCRIPTION,
    category = EDIT_RANK_COMMAND_CATEGORY
)
class GuiEditPermissionsCommand(private val guiEngineApi: GuiEngineApi,
                                plugin: ImprovedFactionsPlugin
) : EditPermissionsCommand(plugin) {

    override fun handle(player: Player, args: Array<out String>): Boolean {
        val rank = parseArgs(player, args).get<FactionRank>(0) ?: return false
        val context = guiEngineApi.openGui(
            player, "rank/rank-detail", mapOf(
                "rank" to rank.name
            )
        )
        val container = context.findComponentByClass<PagedContainer>() ?: return false
        DatabaseManager.loggedTransaction {
            rank.permissions().forEach {
                container.addComponent(
                    FactionPermissionComponentBuilder()
                        .setPermission(it)
                        .createComponent()
                )
            }
        }

        context.render()
        return true
    }
}