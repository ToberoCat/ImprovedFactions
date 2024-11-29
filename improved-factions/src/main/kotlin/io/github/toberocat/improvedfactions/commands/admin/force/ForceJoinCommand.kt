package io.github.toberocat.improvedfactions.commands.admin.force

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.ranks.listRanks
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.arguments.OfflinePlayerArgument
import io.github.toberocat.improvedfactions.utils.arguments.entity.FactionArgument
import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.PlayerNameOption
import io.github.toberocat.improvedfactions.utils.options.addFactionNameOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.exceptions.CommandException
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

@CommandMeta(
    description = "base.command.force.join.description",
    category = CommandCategory.ADMIN_CATEGORY
)
class ForceJoinCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("join") {
    override fun options(): Options = Options.getFromConfig(plugin, "force.join") { options, _ ->
        options
            .cmdOpt(PlayerNameOption(0))
            .addFactionNameOption(1)
    }

    override fun arguments(): Array<Argument<*>> = arrayOf(
        OfflinePlayerArgument(),
        FactionArgument()
    )

    override fun handle(player: Player, args: Array<String>): Boolean {
        val parsedArgs = parseArgs(player, args)
        val faction = parsedArgs.get<Faction>(1) ?: return false
        val playerToJoin = parsedArgs.get<OfflinePlayer>(0) ?: return false

        loggedTransaction {
            val user = player.factionUser()
            if (user.factionId == faction.id.value) {
                player.sendLocalized("base.command.force.join.already-member")
                return@loggedTransaction true
            }

            try {
                if (user.isInFaction()) {
                    user.faction()?.leave(player.uniqueId)
                }

                faction.join(playerToJoin.uniqueId, faction.listRanks().lastOrNull()?.id?.value ?: faction.defaultRank)
                player.sendLocalized("base.command.force.join.success")
                playerToJoin.sendLocalized("base.command.force.join.player-joined", mapOf("faction" to faction.name))
            } catch (e: CommandException) {
                player.sendLocalized("base.command.force.join.error", e.placeholders)
                throw CommandException(e.message, e.placeholders)
            }
        }
        return true
    }
}