package io.github.toberocat.improvedfactions.commands.general

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.sendCommandResult
import io.github.toberocat.improvedfactions.database.storage.FactionSnapshot
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule
import io.github.toberocat.improvedfactions.modules.relations.RelationsModule
import io.github.toberocat.improvedfactions.translation.sendLocalized
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

    open fun process(sender: Player, faction: FactionSnapshot): CommandProcessResult {
        val actualFaction = StorageManager.cache.user(sender.uniqueId)?.factionId
            ?.let(StorageManager.cache::faction) ?: faction
        return sendInfo(sender, actualFaction)
    }

    fun process(sender: CommandSender, faction: FactionSnapshot) = sendInfo(sender, faction)

    private fun sendInfo(sender: CommandSender, faction: FactionSnapshot): CommandProcessResult {
        sender.sendCommandResult(infoHeader("faction" to faction.name))

        sender.sendCommandResult("Members", StorageManager.cache.factionMembers(faction.id).size.toString(), "/f members")
        sender.sendCommandResult("Ranks", StorageManager.cache.ranks(faction.id).size.toString(), "/f rank")
        sender.sendCommandResult("Claims", faction.claimCount.toString(), "/f map")

        if (PowerRaidsModule.powerRaidModule().isEnabled) {
            sender.sendCommandResult("Power", faction.accumulatedPower.toString(), "/f power")
        }
        if (RelationsModule.isEnabled) {
            sender.sendCommandResult("Allies", StorageManager.cache.relations(faction.id, "ALLY").size.toString(), "/f allies")
            sender.sendCommandResult("Enemies", StorageManager.cache.relations(faction.id, "ENEMY").size.toString(), "/f enemies")
        }

        return showDetails("Join Type", faction.joinType)
    }

    private fun CommandSender.sendCommandResult(key: String, value: String, cmd: String = "") =
        sendCommandResult(showDetails(key, value, cmd))

    private fun showDetails(key: String, value: String, cmd: String = "") = infoDetail(
        "cmd" to cmd,
        "key" to key,
        "value" to value
    )
}
