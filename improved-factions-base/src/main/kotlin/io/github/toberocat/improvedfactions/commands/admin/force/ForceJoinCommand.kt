package io.github.toberocat.improvedfactions.commands.admin.force

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.squareClaimAction
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.ranks.FactionRank
import io.github.toberocat.improvedfactions.ranks.anyRank
import io.github.toberocat.improvedfactions.ranks.listRanks
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.arguments.ClaimRadiusArgument
import io.github.toberocat.improvedfactions.utils.arguments.PlayerArgument
import io.github.toberocat.improvedfactions.utils.arguments.StringArgument
import io.github.toberocat.improvedfactions.utils.arguments.ZoneArgument
import io.github.toberocat.improvedfactions.utils.arguments.entity.FactionArgument
import io.github.toberocat.improvedfactions.utils.arguments.entity.RankArgument
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.*
import io.github.toberocat.improvedfactions.zone.Zone
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.exceptions.CommandException
import io.github.toberocat.toberocore.command.options.ArgLengthOption
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.transactions.transaction

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
        PlayerArgument(),
        FactionArgument()
    )

    override fun handle(player: Player, args: Array<String>): Boolean {
        val parsedArgs = parseArgs(player, args)
        val faction = parsedArgs.get<Faction>(1) ?: return false
        val playerToJoin = parsedArgs.get<Player>(0) ?: return false

        transaction {
            val user = player.factionUser()
            if (user.factionId == faction.id.value) {
                player.sendLocalized("base.command.force.join.already-member")
                return@transaction true
            }

            try {
                if (user.isInFaction()) {
                    user.faction()?.leave(player.uniqueId)
                }

                faction.join(playerToJoin.uniqueId, faction.defaultRank)
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