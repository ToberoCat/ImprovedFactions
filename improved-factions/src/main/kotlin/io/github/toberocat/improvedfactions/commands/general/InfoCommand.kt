package io.github.toberocat.improvedfactions.commands.general

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.sendCommandResult
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule
import io.github.toberocat.improvedfactions.modules.relations.RelationsModule
import io.github.toberocat.improvedfactions.modules.relations.RelationsModule.allies
import io.github.toberocat.improvedfactions.modules.relations.RelationsModule.enemies
import io.github.toberocat.improvedfactions.ranks.listRanks
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "info",
    category = CommandCategory.GENERAL_CATEGORY,
    module = "base",
    responses = [
        CommandResponse("infoHeader"),
        CommandResponse("infoDetail"),
        CommandResponse("noFactionFound")
    ]
)
abstract class InfoCommand : InfoCommandContext() {

    open fun process(sender: Player, faction: Faction): CommandProcessResult {
        val actualFaction = sender.factionUser().faction() ?: faction
        return sendInfo(sender, actualFaction)
    }

    fun process(sender: CommandSender, faction: Faction) = sendInfo(sender, faction)

    private fun sendInfo(sender: CommandSender, faction: Faction): CommandProcessResult {
        sender.sendCommandResult(infoHeader("faction" to faction.name))

        sender.sendCommandResult("Members", faction.members().count().toString(), "/f members")
        sender.sendCommandResult("Ranks", faction.listRanks().count().toString(), "/f rank")
        sender.sendCommandResult("Claims", faction.claims().count().toString(), "/f map")

        if (PowerRaidsModule.powerRaidModule().isEnabled) {
            sender.sendCommandResult("Power", faction.accumulatedPower.toString(), "/f power")
        }
        if (RelationsModule.isEnabled) {
            sender.sendCommandResult("Allies", faction.allies().count().toString(), "/f allies")
            sender.sendCommandResult("Enemies", faction.enemies().count().toString(), "/f enemies")
        }

        return showDetails("Join Type", faction.factionJoinType.name.lowercase())
    }

    private fun CommandSender.sendCommandResult(key: String, value: String, cmd: String = "") =
        sendCommandResult(showDetails(key, value, cmd))

    private fun showDetails(key: String, value: String, cmd: String = "") = infoDetail(
        "cmd" to cmd,
        "key" to key,
        "value" to value
    )
}
