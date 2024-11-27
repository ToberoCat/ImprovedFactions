package io.github.toberocat.improvedfactions.commands.rank

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.permissions.FactionPermission
import io.github.toberocat.improvedfactions.permissions.FactionPermissions
import io.github.toberocat.improvedfactions.ranks.FactionRank
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.utils.arguments.BooleanArgument
import io.github.toberocat.improvedfactions.utils.arguments.PermissionArgument
import io.github.toberocat.improvedfactions.utils.arguments.entity.RankArgument
import io.github.toberocat.improvedfactions.annotations.CommandCategory
import io.github.toberocat.improvedfactions.annotations.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.BoolOption
import io.github.toberocat.improvedfactions.utils.options.InFactionOption
import io.github.toberocat.improvedfactions.utils.options.PermissionOption
import io.github.toberocat.improvedfactions.utils.options.RankNameOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.ArgLengthOption
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and

@CommandMeta(
    description = "base.command.rank.set.description",
    category = CommandCategory.PERMISSION_CATEGORY
)
class SetPermissionCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("set") {
    override fun options(): Options =
        Options.getFromConfig(plugin, label)
            .cmdOpt(InFactionOption(true))
            .cmdOpt(RankNameOption(0))
            .cmdOpt(PermissionOption(1))
            .cmdOpt(BoolOption(2))
            .cmdOpt(ArgLengthOption(3))

    override fun arguments(): Array<Argument<*>> = arrayOf(
        RankArgument(), PermissionArgument(), BooleanArgument()
    )

    override fun handle(player: Player, args: Array<out String>): Boolean {
        val arguments = parseArgs(player, args)
        val rank = arguments.get<FactionRank>(0) ?: return false
        val permission = arguments.get<String>(1) ?: return false
        val value = arguments.get<Boolean>(2) ?: return false

        loggedTransaction {
            val factionPermission = FactionPermission.find(
                FactionPermissions.rankId eq rank.id.value and (FactionPermissions.permission eq permission)
            ).firstOrNull() ?: FactionPermission.new {
                rankId = rank.id.value
                this.permission = permission
            }
            factionPermission.allowed = value
        }
        player.sendLocalized("base.command.rank.set.updated")
        return true
    }
}