package io.github.toberocat.improvedfactions.commands.admin

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.annotations.command.PermissionConfig
import io.github.toberocat.improvedfactions.annotations.permission.PermissionConfigurations
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.cancelledCommandResult
import io.github.toberocat.improvedfactions.commands.respondAfter
import io.github.toberocat.improvedfactions.api.events.FactionDeleteEvent
import io.github.toberocat.improvedfactions.database.storage.FactionSnapshot
import io.github.toberocat.improvedfactions.database.storage.GameStateCommands
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import org.bukkit.command.CommandSender
import org.bukkit.Bukkit

@PermissionConfig(config = PermissionConfigurations.OP_ONLY)
@GeneratedCommandMeta(
    label = "admin disband",
    category = CommandCategory.ADMIN_CATEGORY,
    module = BaseModule.MODULE_NAME,
    responses = [
        CommandResponse("factionDisbanded")
    ]
)
abstract class DisbandCommand : DisbandCommandContext() {
    fun process(sender: CommandSender, faction: FactionSnapshot): CommandProcessResult? {
        val event = FactionDeleteEvent(faction)
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) return cancelledCommandResult()
        return sender.respondAfter(GameStateCommands.deleteFaction(faction.id)) { factionDisbanded() }
    }
}
