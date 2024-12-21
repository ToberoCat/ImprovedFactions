package io.github.toberocat.improvedfactions.commands.admin

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.annotations.command.PermissionConfig
import io.github.toberocat.improvedfactions.annotations.permission.PermissionConfigurations
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import org.bukkit.command.CommandSender

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
    fun process(sender: CommandSender, faction: Faction): CommandProcessResult {
        faction.delete()
        return factionDisbanded()
    }
}