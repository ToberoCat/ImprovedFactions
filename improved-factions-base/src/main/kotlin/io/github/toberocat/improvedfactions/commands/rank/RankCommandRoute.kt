package io.github.toberocat.improvedfactions.commands.rank

import io.github.toberocat.guiengine.components.container.tab.PagedContainer
import io.github.toberocat.guiengine.function.GuiFunction
import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.components.rank.FactionRankComponentBuilder
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.ranks.listRanks
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.FactionPermissionOption
import io.github.toberocat.improvedfactions.utils.options.InFactionOption
import io.github.toberocat.toberocore.command.CommandRoute
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.transactions.transaction

@CommandMeta(
    description = "base.command.rank.description",
    category = CommandCategory.PERMISSION_CATEGORY
)
class RankCommandRoute(private val plugin: ImprovedFactionsPlugin) : CommandRoute("rank", plugin) {
    init {
        addChild(CreateRankCommand(plugin))
        addChild(AssignRankCommand(plugin))
        addChild(DeleteRankCommand(plugin))
        addChild(SetPermissionCommand(plugin))
        addChild(DefaultRankCommand(plugin))
        addChild(EditPermissionsCommand(plugin))
    }

    override fun options(): Options = super.options()
        .cmdOpt(FactionPermissionOption(Permissions.MANAGE_PERMISSIONS))
        .cmdOpt(InFactionOption(true))

    override fun handle(player: Player, p1: Array<out String>): Boolean {
        val context = plugin.guiEngineApi.openGui(player, "rank/rank-overview")
        val container = context.findComponentByClass<PagedContainer>()
            ?: return false
        transaction {
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