package io.github.toberocat.improvedfactions.commands.admin.force

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.annotations.command.PermissionConfig
import io.github.toberocat.improvedfactions.annotations.permission.PermissionConfigurations
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.sendCommandResult
import io.github.toberocat.improvedfactions.database.storage.cachedUser
import io.github.toberocat.improvedfactions.database.storage.faction
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender

@PermissionConfig(config = PermissionConfigurations.OP_ONLY)
@GeneratedCommandMeta(
    label = "admin playerInfo",
    category = CommandCategory.ADMIN_CATEGORY,
    module = "base",
    responses = [
        CommandResponse("factionInfoHeader"),
        CommandResponse("factionInfoDetail")
    ]
)
abstract class ForceInfoCommand : ForceInfoCommandContext() {

    fun processConsole(sender: CommandSender, target: OfflinePlayer): CommandProcessResult {
        return printInfo(sender, target)
    }

    private fun printInfo(sender: CommandSender, target: OfflinePlayer): CommandProcessResult {
        sender.sendCommandResult(factionInfoHeader("player" to (target.name ?: "Unknown")))

        sender.sendCommandResult(
            details(
                "Faction",
                target.cachedUser().faction()?.name ?: "No Faction",
                "/f info ${target.cachedUser().faction()?.name}"
            )
        )
        return details("Rank", target.cachedUser().rankName, "")
    }

    private fun details(key: String, value: String, cmd: String = "") = factionInfoDetail(
        "key" to key,
        "value" to value
    )
}
