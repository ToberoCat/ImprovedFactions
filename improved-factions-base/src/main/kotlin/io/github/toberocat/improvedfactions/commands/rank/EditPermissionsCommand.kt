package io.github.toberocat.improvedfactions.commands.rank

import io.github.toberocat.guiengine.components.container.tab.PagedContainer
import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.components.permission.FactionPermissionComponentBuilder
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.ranks.FactionRank
import io.github.toberocat.improvedfactions.utils.arguments.entity.RankArgument
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.InFactionOption
import io.github.toberocat.improvedfactions.utils.options.RankNameOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.ArgLengthOption
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

@CommandMeta(
    description = "base.command.rank.edit.description",
    category = CommandCategory.PERMISSION_CATEGORY
)
class EditPermissionsCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("edit") {
    override fun options(): Options = Options.getFromConfig(plugin, label)
        .cmdOpt(InFactionOption(true))
        .cmdOpt(RankNameOption(0))
        .cmdOpt(ArgLengthOption(1))

    override fun arguments(): Array<Argument<*>> = arrayOf(
        RankArgument()
    )

    override fun handle(player: Player, args: Array<out String>): Boolean {
        val rank = parseArgs(player, args).get<FactionRank>(0) ?: return false
        val context = plugin.guiEngineApi.openGui(
            player, "rank/rank-detail", mapOf(
                "rank" to rank.name
            )
        )
        val container = context.findComponentByClass<PagedContainer>() ?: return false
        loggedTransaction {
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