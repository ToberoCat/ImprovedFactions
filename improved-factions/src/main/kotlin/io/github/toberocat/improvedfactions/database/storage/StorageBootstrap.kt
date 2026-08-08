package io.github.toberocat.improvedfactions.database.storage

import java.util.concurrent.CompletionStage

/** Starts the initial snapshot load on the storage dispatcher. */
class StorageBootstrap(private val repository: ClaimRepository) {
    fun loadAsync(): CompletionStage<GameStateSnapshot> = repository.refresh()
}
