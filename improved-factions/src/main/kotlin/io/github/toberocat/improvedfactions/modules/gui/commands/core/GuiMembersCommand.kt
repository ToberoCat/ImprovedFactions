package io.github.toberocat.improvedfactions.modules.gui.commands.core

import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.components.container.tab.PagedContainer
import io.github.toberocat.guiengine.components.provided.item.SimpleItemComponentBuilder
import io.github.toberocat.guiengine.function.GuiFunction
import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.commands.member.MEMBERS_COMMAND_CATEGORY
import io.github.toberocat.improvedfactions.commands.member.MEMBERS_COMMAND_DESCRIPTION
import io.github.toberocat.improvedfactions.commands.member.MembersCommand
import io.github.toberocat.improvedfactions.database.DatabaseManager
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.annotations.CommandMeta
import org.bukkit.entity.Player

@CommandMeta(
    description = MEMBERS_COMMAND_DESCRIPTION,
    category = MEMBERS_COMMAND_CATEGORY
)
class GuiMembersCommand(private val guiEngineApi: GuiEngineApi,
                        plugin: ImprovedFactionsPlugin
) : MembersCommand(plugin) {

    override fun handle(player: Player, args: Array<String>): Boolean {
        val context = guiEngineApi.openGui(player, "member/member-overview-page")
        val container = context.findComponentByClass<PagedContainer>() ?: return false
        DatabaseManager.loggedTransaction {
            player.factionUser().faction()?.members()?.forEach {
                val member = it.offlinePlayer()
                container.addComponent(
                    SimpleItemComponentBuilder()
                        .setName("§e${member.name}")
                        .setLore(
                            arrayOf(
                                "",
                                "§7Status: ${getLastSeen(member)}",
                                "§7Rank: §e${it.rank().name}"
                            )
                        )
                        .setOwner(it.uniqueId)
                        .setClickFunctions(listOf(
                            GuiFunction.anonymous {
                                guiEngineApi.openGui(
                                    player, "member/member-detail-page", mapOf(
                                        "member" to (member.name ?: "")
                                    )
                                )
                            }
                        ))
                        .createComponent()
                )
            }
        }

        context.render()
        return true
    }
}