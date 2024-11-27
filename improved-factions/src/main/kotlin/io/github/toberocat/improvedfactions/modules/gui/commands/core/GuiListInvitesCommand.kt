package io.github.toberocat.improvedfactions.modules.gui.commands.core

import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.components.container.tab.PagedContainer
import io.github.toberocat.guiengine.components.provided.item.SimpleItemComponent
import io.github.toberocat.guiengine.components.provided.paged.PagedComponent
import io.github.toberocat.guiengine.event.spigot.GuiCloseEvent
import io.github.toberocat.guiengine.function.GuiFunction
import io.github.toberocat.guiengine.render.RenderPriority
import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.commands.invite.INVITES_COMMAND_CATEGORY
import io.github.toberocat.improvedfactions.commands.invite.INVITES_COMMAND_DESCRIPTION
import io.github.toberocat.improvedfactions.commands.invite.ListInvitesCommand
import io.github.toberocat.improvedfactions.database.DatabaseManager
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.invites.FactionInvite
import io.github.toberocat.improvedfactions.invites.FactionInvites
import io.github.toberocat.improvedfactions.ranks.FactionRank
import io.github.toberocat.improvedfactions.user.FactionUser
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.annotations.CommandMeta
import io.github.toberocat.improvedfactions.utils.compute
import io.github.toberocat.toberocore.command.exceptions.CommandException
import io.github.toberocat.toberocore.util.ItemUtils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

@CommandMeta(
    description = INVITES_COMMAND_DESCRIPTION,
    category = INVITES_COMMAND_CATEGORY
)
class GuiListInvitesCommand(
    private val guiEngineApi: GuiEngineApi,
    private val plugin: ImprovedFactionsPlugin
) : ListInvitesCommand(plugin) {

    override fun handle(player: Player, args: Array<String>): Boolean {
        val context = guiEngineApi.openGui(player, "invites")
        val paged = context.findComponentByClass<PagedComponent>()
            ?: throw CommandException("base.gui.exceptions.missing-container", emptyMap())

        val invitedId = player.factionUser().id.value
        val taskId = Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            populateContainer(player, invitedId, paged)
            context.render()
        }, 0, 20L).taskId

        context.listen(GuiCloseEvent::class.java) { Bukkit.getScheduler().cancelTask(taskId) }
        return true
    }

    private fun populateContainer(player: Player, invited: Int, container: PagedContainer) {
        container.clearContainer()
        DatabaseManager.loggedTransaction {
            FactionInvite.find { FactionInvites.invitedId eq invited }.forEach { invite ->
                container.addComponent(createComponent(Faction.findById(invite.factionId), invite, player))
            }
        }
    }

    private fun createComponent(
        faction: Faction?, invite: FactionInvite, player: Player
    ): SimpleItemComponent {
        val icon = faction?.icon ?: ItemStack(Material.OAK_SIGN)
        ItemUtils.editMeta(icon) {
            val lore = it.lore ?: mutableListOf()
            lore.addAll(arrayOf("",
                "§7Expires in: §e${invite.expiresInFormatted()}",
                "§7Joining as: §e${FactionRank.findById(invite.rankId)?.name ?: "§cNot found"}",
                "§7Invited by: §e${
                    FactionUser.findById(invite.inviterId)
                        ?.compute { Bukkit.getPlayer(it.uniqueId)?.name } ?: "§c${invite.inviterId}"
                }"))
            it.lore = lore
        }
        return SimpleItemComponent(
            0, 0, RenderPriority.NORMAL, UUID.randomUUID().toString(),
            listOf(GuiFunction.anonymous {
                it.api.openGui(
                    player, "invite-detail-page", mapOf(
                        "invite" to invite.id.toString(), "faction" to (faction?.name ?: "§cDeleted")
                    )
                )
            }), emptyList(), emptyList(), icon, false
        )
    }
}