package io.github.toberocat.improvedfactions.api

import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.api.events.FactionCreateEvent
import io.github.toberocat.improvedfactions.factions.defaultFactionRanks
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import java.util.concurrent.CompletionStage
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CancellationException
import java.util.UUID
import org.bukkit.Bukkit

/**
 * Entry point for accessing basic Improved Factions functionality from other plugins.
 */
object ImprovedFactionsAPI {
    /**
     * Get all factions registered on the server.
     */
    fun getFactions(): List<FactionSnapshot> = StorageManager.cache.factions()

    /**
     * Get a faction by its id.
     */
    fun getFaction(id: Int): FactionSnapshot? = StorageManager.cache.faction(id)

    /**
     * Get a faction by its name.
     */
    fun getFaction(name: String): FactionSnapshot? = StorageManager.cache.factions()
        .firstOrNull { it.name.equals(name, ignoreCase = true) }

    /**
     * Create a new faction.
     */
    fun createFaction(ownerId: UUID, factionName: String): CompletionStage<FactionSnapshot> {
        check(Bukkit.isPrimaryThread()) { "Faction creation must be initiated on the Bukkit main thread" }
        val event = FactionCreateEvent(ownerId, factionName)
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) return CompletableFuture<FactionSnapshot>().also {
            it.completeExceptionally(CancellationException("Faction creation was cancelled"))
        }
        val ranks = defaultFactionRanks(
            BaseModule.plugin.config.getConfigurationSection("factions.default-faction-ranks")
        )
        return GameStateCommands.createFaction(ownerId, factionName, 50, ranks, Permissions.knownPermissions.keys)
            .thenApply { id -> checkNotNull(StorageManager.cache.faction(id)) }
    }
}
