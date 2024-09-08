package io.github.toberocat.improvedfactions.commands.member

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.InFactionOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.ArgLengthOption
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.text.SimpleDateFormat

const val MEMBERS_COMMAND_DESCRIPTION = "base.command.members.description"
const val MEMBERS_COMMAND_CATEGORY = CommandCategory.MEMBER_CATEGORY

@CommandMeta(
    description = MEMBERS_COMMAND_DESCRIPTION,
    category = MEMBERS_COMMAND_CATEGORY
)
open class MembersCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("members") {
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy")

    override fun options(): Options = Options.getFromConfig(plugin, label) { options, _ ->
        options.cmdOpt(InFactionOption(true))
            .cmdOpt(ArgLengthOption(0))
    }

    override fun arguments(): Array<Argument<*>> = emptyArray()

    override fun handle(player: Player, args: Array<String>): Boolean {
        player.sendLocalized("base.command.members.header")

        loggedTransaction {
            player.factionUser().faction()?.members()?.forEach {
                val offlinePlayer = it.offlinePlayer()
                player.sendLocalized(
                    "base.command.members.detail", mapOf(
                        "name" to (offlinePlayer.name ?: "Unknown"),
                        "lastSeen" to getLastSeen(offlinePlayer),
                        "rank" to it.rank().name
                    )
                )
            }
        }
        return true
    }

    protected fun getLastSeen(member: OfflinePlayer) = when {
        member.isOnline -> "§aOnline"
        else -> "§e${dateFormat.format(member.lastPlayed)}"
    }
}