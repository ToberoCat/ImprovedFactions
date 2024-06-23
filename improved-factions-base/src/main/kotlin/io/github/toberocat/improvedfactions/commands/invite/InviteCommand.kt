package io.github.toberocat.improvedfactions.commands.invite

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.ranks.FactionRank
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.arguments.OfflinePlayerArgument
import io.github.toberocat.improvedfactions.utils.arguments.entity.RankArgument
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.*
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.exceptions.CommandException
import io.github.toberocat.toberocore.command.options.ArgLengthOption
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

@CommandMeta(
    description = "base.command.invite.description",
    category = CommandCategory.INVITE_CATEGORY
)
class InviteCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("invite") {
    override fun options(): Options = Options.getFromConfig(plugin, "invite") { options, _ ->
        options.cmdOpt(InFactionOption(true))
            .cmdOpt(PlayerNameOption(0))
            .cmdOpt(RankNameOption(1))
            .cmdOpt(CanManageRankOption(1))
            .cmdOpt(ArgLengthOption(2))
            .cmdOpt(FactionPermissionOption(Permissions.SEND_INVITES))
    }

    override fun arguments(): Array<Argument<*>> = arrayOf(
        OfflinePlayerArgument(),
        RankArgument()
    )

    override fun handle(inviter: Player, args: Array<out String>): Boolean {
        val arguments = parseArgs(inviter, args)
        val invited = arguments.get<OfflinePlayer>(0) ?: return false
        val rank = arguments.get<FactionRank>(1) ?: return false

        val faction = loggedTransaction {
            val faction = inviter.factionUser().faction() ?: throw CommandException(
                "base.command.invite.player-no-faction", emptyMap()
            )

            faction.invite(inviter.uniqueId, invited.uniqueId, rank.id.value)
            return@loggedTransaction faction
        }
        inviter.sendLocalized(
            "base.command.invite.invited-player", mapOf(
                "player" to (invited.name ?: "unknown")
            )
        )
        invited.sendLocalized(
            "base.command.invite.you-have-been-invited", mapOf(
                "faction" to faction.name,
                "inviter" to inviter.displayName,
                "rank" to rank.name
            )
        )
        return true
    }
}