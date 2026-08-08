package io.github.toberocat.improvedfactions.commands.claim

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.respondAfter
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.permissions.Permissions
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "unclaim",
    category = CommandCategory.CLAIM_CATEGORY,
    module = "base",
    responses = [
        CommandResponse("unclaimed"),
        CommandResponse("unclaimedRadius"),
        CommandResponse("notInFaction"),
        CommandResponse("noPermission")
    ]
)
abstract class UnclaimCommand : UnclaimCommandContext() {

    fun process(player: Player, radius: Int?): CommandProcessResult? {
        val factionUser = player.cachedUser()
        if (!factionUser.isInFaction()) {
            return notInFaction()
        }

        if (!factionUser.hasPermission(Permissions.MANAGE_CLAIMS)) {
            return noPermission()
        }

        val faction = factionUser.faction() ?: return notInFaction()
        val squareRadius = radius ?: 0
        val center = player.location.chunk
        val keys = buildList {
            for (x in center.x - squareRadius..center.x + squareRadius)
                for (z in center.z - squareRadius..center.z + squareRadius)
                    add(ClaimKey(center.world.name, x, z))
        }
        if (keys.any { StorageManager.cache.claim(it)?.factionId != faction.id }) return unclaimed()
        return player.respondAfter(GameStateCommands.unclaimAll(keys, faction.id)) { count ->
            if (radius == null) unclaimed() else unclaimedRadius(
                "radius" to radius.toString(), "successful-claims" to count.toString(), "total-claims" to keys.size.toString()
            )
        }
    }
}
