package io.github.toberocat.improvedfactions.commands.admin.force

import io.github.toberocat.improvedfactions.annotations.command.*
import io.github.toberocat.improvedfactions.annotations.localization.Localization
import io.github.toberocat.improvedfactions.annotations.permission.PermissionConfigurations
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.cancelledCommandResult
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParsingException
import io.github.toberocat.improvedfactions.commands.arguments.ParsingContext
import io.github.toberocat.improvedfactions.commands.respondAfter
import io.github.toberocat.improvedfactions.api.events.FactionJoinEvent
import io.github.toberocat.improvedfactions.database.storage.*
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.Bukkit

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
        faction: FactionSnapshot,
        target: OfflinePlayer,
        @ManualArgument rank: RankSnapshot,
    ) =
        forceJoin(sender, target, faction, rank)

    override fun rankArgument(): ArgumentParser {
        val factionParser = getArgumentParser(FactionSnapshot::class.java)
            ?: throw IllegalStateException("Faction parser not found")
        return object : ArgumentParser {

            override fun parse(sender: CommandSender, arg: String, args: Array<String>): Any {
                val faction = factionParser.parse(sender, args[0], args) as FactionSnapshot
                return StorageManager.cache.ranks(faction.id).firstOrNull { it.name.equals(arg, true) }
                    ?: throw ArgumentParsingException("base.arguments.factionRank.error")
            }

            override fun rawTabComplete(pCtx: ParsingContext): List<String> {
                val faction = factionParser.parse(pCtx.sender, pCtx.args[0], pCtx.args) as FactionSnapshot
                return StorageManager.cache.ranks(faction.id).map { it.name }
            }
        }
    }

    private fun forceJoin(sender: CommandSender, target: OfflinePlayer, faction: FactionSnapshot, rank: RankSnapshot): CommandProcessResult? {
        val user = target.cachedUser()

        if (user.isInFaction()) {
            return playerAlreadyInFaction()
        }

        val event = FactionJoinEvent(faction, user)
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) return cancelledCommandResult()

        val targetName = target.name ?: "No Name"
        return sender.respondAfter(GameStateCommands.setUserFaction(target.uniqueId, faction.id, rank.id)) {
            success("player" to targetName, "faction" to faction.name)
        }
    }
}
