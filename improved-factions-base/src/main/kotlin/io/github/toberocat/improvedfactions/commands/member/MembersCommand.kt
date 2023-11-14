package io.github.toberocat.improvedfactions.commands.member

import io.github.toberocat.guiengine.components.container.tab.PagedContainer
import io.github.toberocat.guiengine.components.provided.item.SimpleItemComponentBuilder
import io.github.toberocat.guiengine.function.GuiFunction
import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.InFactionOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.ArgLengthOption
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.transactions.transaction
import java.text.SimpleDateFormat

@CommandMeta(
    description = "base.commands.members.description",
    category = CommandCategory.MEMBER_CATEGORY
)
class MembersCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("members") {
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy")

    override fun options(): Options = Options.getFromConfig(plugin, label) { options, _ ->
        options.opt(InFactionOption(true))
            .opt(ArgLengthOption(0))
    }

    override fun arguments(): Array<Argument<*>> = emptyArray()

    override fun handle(player: Player, args: Array<out String>): Boolean {
        val context = plugin.guiEngineApi.openGui(player, "member/member-overview-page")
        val container = context.findComponentByClass<PagedContainer>() ?: return false
        transaction {

            player.factionUser().faction()?.members()?.forEach {
                val member = it.offlinePlayer()
                container.addComponent(
                    SimpleItemComponentBuilder()
                        .setName("§e${member.name}")
                        .setLore(
                            arrayOf(
                                "",
                                "§7Status: ${
                                    if (member.isOnline) "§aOnline"
                                    else "§e${dateFormat.format(member.lastPlayed)}"
                                }",
                                "§7Rank: §e${it.rank().name}"
                            )
                        )
                        .setOwner(it.uniqueId)
                        .setClickFunctions(listOf(
                            GuiFunction.anonymous {
                                plugin.guiEngineApi.openGui(player, "member/member-detail-page", mapOf(
                                    "member" to (member.name ?: "")
                                ))
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