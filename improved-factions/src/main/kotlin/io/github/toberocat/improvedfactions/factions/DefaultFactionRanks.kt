package io.github.toberocat.improvedfactions.factions

import io.github.toberocat.improvedfactions.database.storage.GameStateCommands
import io.github.toberocat.improvedfactions.permissions.Permissions
import org.bukkit.configuration.ConfigurationSection

fun defaultFactionRanks(configuredRanks: ConfigurationSection?): List<GameStateCommands.DefaultRankSpec> =
    configuredRanks?.getKeys(false)?.map { rankName ->
        GameStateCommands.DefaultRankSpec(
            rankName,
            configuredRanks.getInt("$rankName.priority"),
            configuredRanks.getStringList("$rankName.default-permissions").toSet()
        )
    } ?: listOf(
        GameStateCommands.DefaultRankSpec(
            "Member",
            1,
            setOf(Permissions.SEND_INVITES, Permissions.VIEW_POWER, Permissions.HOME)
        ),
        GameStateCommands.DefaultRankSpec("Owner", 1000, Permissions.knownPermissions.keys)
    )
