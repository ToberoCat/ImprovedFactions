package io.github.toberocat.improvedfactions.database.storage

import java.util.concurrent.CompletionStage
import java.util.concurrent.atomic.AtomicLong

class ClaimRepository(
    private val dispatcher: StorageDispatcher,
    private val cache: ClaimStateCache,
    private val loadSnapshot: () -> GameStateSnapshot
) {
    private val requestedGeneration = AtomicLong()
    private val publishedGeneration = AtomicLong()
    private val publishLock = Any()

    fun refresh(): CompletionStage<GameStateSnapshot> {
        val generation = requestedGeneration.incrementAndGet()
        return dispatcher.submit(loadSnapshot).thenApply { snapshot ->
            synchronized(publishLock) {
                if (generation > publishedGeneration.get()) {
                    cache.publish(snapshot)
                    publishedGeneration.set(generation)
                }
            }
            snapshot
        }
    }
}
