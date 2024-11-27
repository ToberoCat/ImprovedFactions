package io.github.toberocat.improvedfactions.commands.admin.force

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.ranks.listRanks
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.arguments.OfflinePlayerArgument
import io.github.toberocat.improvedfactions.annotations.CommandCategory
import io.github.toberocat.improvedfactions.annotations.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.PlayerNameOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.ArgLengthOption
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

@CommandMeta(
    description = "base.command.force.info.description",
    module = "core",
    category = CommandCategory.ADMIN_CATEGORY
)
class ForceInfoCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("info") {
    override fun options(): Options = Options.getFromConfig(plugin, label) { options, _ ->
        options.cmdOpt(PlayerNameOption(0))
            .cmdOpt(ArgLengthOption(1))
    }

    override fun arguments(): Array<Argument<*>> = arrayOf(
        OfflinePlayerArgument()
    )

    override fun handle(player: Player, args: Array<String>): Boolean {
        val arguments = parseArgs(player, args)
        val target = arguments.get<OfflinePlayer>(0) ?: return false

        player.sendLocalized(
            "base.command.force.info.header", mapOf(
                "player" to (target.name ?: "Unknown")
            )
        )

        loggedTransaction {
            player.showDetails(
                "Faction",
                target.factionUser().faction()?.name ?: "No Faction",
                "/f info ${target.factionUser().faction()?.name}"
            )
            player.showDetails("Rank", target.factionUser().rank().name, "")
        }
        return true
    }

    private fun Player.showDetails(key: String, value: String, cmd: String = "") {
        sendLocalized(
            "base.command.force.info.detail", mapOf(
                "key" to key,
                "value" to value
            )
        )
    }
}