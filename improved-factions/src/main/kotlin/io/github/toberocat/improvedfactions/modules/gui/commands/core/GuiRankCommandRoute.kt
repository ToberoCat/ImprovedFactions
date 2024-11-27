package io.github.toberocat.improvedfactions.modules.gui.commands.core

import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.components.container.tab.PagedContainer
import io.github.toberocat.guiengine.function.GuiFunction
import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.commands.rank.CreateRankCommand
import io.github.toberocat.improvedfactions.commands.rank.RANK_COMMAND_CATEGORY
import io.github.toberocat.improvedfactions.commands.rank.RANK_COMMAND_DESCRIPTION
import io.github.toberocat.improvedfactions.commands.rank.RankCommandRoute
import io.github.toberocat.improvedfactions.database.DatabaseManager
import io.github.toberocat.improvedfactions.modules.gui.actions.GuiEngineCommandMapperAction
import io.github.toberocat.improvedfactions.modules.gui.components.rank.FactionRankComponentBuilder
import io.github.toberocat.improvedfactions.ranks.listRanks
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.annotations.CommandMeta
import org.bukkit.Bukkit
import org.bukkit.entity.Player

@CommandMeta(
    description = RANK_COMMAND_DESCRIPTION,
    category = RANK_COMMAND_CATEGORY
)
class GuiRankCommandRoute(
    private val guiEngineApi: GuiEngineApi,
    plugin: ImprovedFactionsPlugin
) : RankCommandRoute(plugin) {

    init {
        addChild(GuiEditPermissionsCommand(guiEngineApi, plugin))
    }

    override fun handle(player: Player, args: Array<String>): Boolean {
        val context = guiEngineApi.openGui(player, "rank/rank-overview")
        val container = context.findComponentByClass<PagedContainer>()
            ?: return false
        DatabaseManager.loggedTransaction {
            val user = player.factionUser()
            user.faction()
                ?.listRanks()
                ?.filter { user.canManage(it) }
                ?.forEach {
                    container.addComponent(
                        FactionRankComponentBuilder()
                            .setRank(it)
                            .setClickFunctions(listOf(GuiFunction.anonymousSync { _ ->
                                Bukkit.dispatchCommand(
                                    player,
                                    "factions rank edit ${it.name}"
                                )
                            }))
                            .createComponent()
                    )
                }
        }
        context.render()
        return true
    }
}