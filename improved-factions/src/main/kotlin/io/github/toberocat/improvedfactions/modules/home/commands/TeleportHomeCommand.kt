package io.github.toberocat.improvedfactions.modules.home.commands

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.cancelledCommandResult
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.modules.home.HomeModule
import io.github.toberocat.improvedfactions.modules.home.HomeModule.teleportToFactionHome
import io.github.toberocat.improvedfactions.permissions.Permissions
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "home",
    category = CommandCategory.GENERAL_CATEGORY,
    module = HomeModule.MODULE_NAME,
    responses = [
        CommandResponse("teleportStarted"),
        CommandResponse("teleportHomeSuccess"),
        CommandResponse("teleportHomeFailed"),
        CommandResponse("homeUnreachable"),
        CommandResponse("teleportCancelled"),
        CommandResponse("notInFaction"),
        CommandResponse("noPermission")
    ]
)
abstract class TeleportHomeCommand : TeleportHomeCommandContext() {

    fun process(player: Player): CommandProcessResult {
        val factionUser = player.cachedUser()
        if (!factionUser.isInFaction()) {
            return notInFaction()
        }

        if (!factionUser.hasPermission(Permissions.HOME)) {
            return noPermission()
        }

        return when (player.teleportToFactionHome()) {
            HomeModule.HomeTeleportStartResult.STARTED -> teleportStarted()
            HomeModule.HomeTeleportStartResult.UNREACHABLE -> cancelledCommandResult()
            HomeModule.HomeTeleportStartResult.FAILED -> teleportHomeFailed()
        }
    }
}
