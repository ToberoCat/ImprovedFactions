package io.github.toberocat.improvedfactions.commands.admin.force

import io.github.toberocat.improvedfactions.annotations.command.*
import io.github.toberocat.improvedfactions.annotations.localization.Localization
import io.github.toberocat.improvedfactions.annotations.permission.PermissionConfigurations
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParsingException
import io.github.toberocat.improvedfactions.commands.arguments.ParsingContext
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.ranks.FactionRank
import io.github.toberocat.improvedfactions.ranks.anyRank
import io.github.toberocat.improvedfactions.ranks.listRanks
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender

@PermissionConfig(config = PermissionConfigurations.OP_ONLY)
@GeneratedCommandMeta(
    label = "admin join",
    category = CommandCategory.ADMIN_CATEGORY,
    module = "base",
    responses = [
        CommandResponse("success"),
        CommandResponse("playerAlreadyInFaction")
    ]
)
abstract class ForceJoinCommand : ForceJoinCommandContext() {

    fun processSender(
        sender: CommandSender,
        faction: Faction,
        target: OfflinePlayer,
        @ManualArgument rank: FactionRank,
    ) =
        forceJoin(target, faction, rank)

    override fun rankArgument(): ArgumentParser {
        val factionParser = getArgumentParser(Faction::class.java)
            ?: throw IllegalStateException("Faction parser not found")
        return object : ArgumentParser {

            override fun parse(sender: CommandSender, arg: String, args: Array<String>): Any {
                val faction = factionParser.parse(sender, args[0], args) as Faction
                return faction.anyRank(arg) ?: throw ArgumentParsingException("base.arguments.factionRank.error")
            }

            override fun tabComplete(pCtx: ParsingContext): List<String> {
                val faction = factionParser.parse(pCtx.sender, pCtx.args[0], pCtx.args) as Faction
                return faction.listRanks().map { it.name }
            }
        }
    }

    private fun forceJoin(target: OfflinePlayer, faction: Faction, rank: FactionRank): CommandProcessResult {
        val user = target.factionUser()

        if (user.isInFaction()) {
            return playerAlreadyInFaction()
        }

        faction.join(target.uniqueId, rank.factionId)

        return success("player" to (target.name ?: "No Name"), "faction" to faction.name)
    }
}