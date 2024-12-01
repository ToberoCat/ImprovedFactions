package io.github.toberocat.improvedfactions.modules.home.commands

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.modules.home.HomeModule
import io.github.toberocat.improvedfactions.modules.home.HomeModule.setHome
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "sethome",
    category = CommandCategory.MANAGE_CATEGORY,
    module = HomeModule.MODULE_NAME,
    responses = [
        CommandResponse("setHomeSuccess"),
        CommandResponse("setHomeFailed"),
        CommandResponse("notInFaction"),
        CommandResponse("noPermission")
    ]
)
abstract class HomeSetCommand : HomeSetCommandContext() {

    fun process(player: Player): CommandProcessResult {
        val factionUser = player.factionUser()
        if (!factionUser.isInFaction()) {
            return notInFaction()
        }

        if (!factionUser.hasPermission(Permissions.SET_HOME)) {
            return noPermission()
        }

        factionUser.faction()?.setHome(player.location) ?: return setHomeFailed()
        return setHomeSuccess()
    }
}