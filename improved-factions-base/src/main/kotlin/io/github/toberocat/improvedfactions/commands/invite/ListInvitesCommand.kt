package io.github.toberocat.improvedfactions.commands.invite

import io.github.toberocat.guiengine.components.container.tab.PagedContainer
import io.github.toberocat.guiengine.components.provided.item.SimpleItemComponent
import io.github.toberocat.guiengine.components.provided.paged.PagedComponent
import io.github.toberocat.guiengine.event.GuiEvents
import io.github.toberocat.guiengine.event.spigot.GuiCloseEvent
import io.github.toberocat.guiengine.event.spigot.GuiEngineEvent
import io.github.toberocat.guiengine.function.GuiFunction
import io.github.toberocat.guiengine.render.RenderPriority
import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.invites.FactionInvite
import io.github.toberocat.improvedfactions.invites.FactionInvites
import io.github.toberocat.improvedfactions.ranks.FactionRank
import io.github.toberocat.improvedfactions.user.FactionUser
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.compute
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.exceptions.CommandException
import io.github.toberocat.toberocore.command.options.Options
import io.github.toberocat.toberocore.util.ItemUtils
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlin.time.Duration

@CommandMeta(
    description = "base.command.invites.description",
    category = CommandCategory.INVITE_CATEGORY
)
class ListInvitesCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("invites") {
    override fun options(): Options = Options.getFromConfig(plugin, label)

    override fun arguments(): Array<Argument<*>> = emptyArray()

    override fun handle(player: Player, args: Array<out String>): Boolean {
        val context = plugin.guiEngineApi.openGui(player, "invites")
        val paged = context.findComponentByClass<PagedComponent>()
            ?: throw CommandException("base.gui.exceptions.missing-container", emptyMap())

        val invitedId = player.factionUser().id.value
        val taskId = Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            populateContainer(player, invitedId, paged)
            context.render()
        }, 0, 20L).taskId

        context.listen(GuiCloseEvent::class.java) {  Bukkit.getScheduler().cancelTask(taskId) }
        return true
    }

    private fun populateContainer(player: Player, invited: Int, container: PagedContainer) {
        container.clearContainer()
        transaction {
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
                "§7Expires in: §e${formatDuration(invite.expirationDate.toInstant(TimeZone.UTC) - Clock.System.now())}",
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

    private fun formatDuration(duration: Duration): String {
        val minutes = duration.inWholeSeconds / 60
        val seconds = duration.inWholeSeconds % 60
        return String.format("%02dm %02ds", minutes, seconds)
    }
}