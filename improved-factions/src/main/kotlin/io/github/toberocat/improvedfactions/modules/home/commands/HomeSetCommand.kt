package io.github.toberocat.improvedfactions.modules.home.commands

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.respondAfter
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.modules.home.HomeModule
import io.github.toberocat.improvedfactions.permissions.Permissions
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

    fun process(player: Player): CommandProcessResult? {
        val factionUser = player.cachedUser()
        if (!factionUser.isInFaction()) {
            return notInFaction()
        }

        if (!factionUser.hasPermission(Permissions.SET_HOME)) {
            return noPermission()
        }

        val faction = factionUser.faction() ?: return setHomeFailed()
        if (StorageManager.cache.claim(player.location.claimKey())?.factionId != faction.id) return setHomeFailed()
        val location = player.location
        val world = location.world?.name ?: return setHomeFailed()
        val home = HomeSnapshot(faction.id, world, location.x, location.y, location.z)
        return player.respondAfter(GameStateCommands.setHome(faction.id, home)) { setHomeSuccess() }
    }
}
