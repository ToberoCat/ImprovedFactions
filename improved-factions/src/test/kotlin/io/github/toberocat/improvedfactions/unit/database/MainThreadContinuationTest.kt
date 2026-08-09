package io.github.toberocat.improvedfactions.unit.database

import io.github.toberocat.improvedfactions.database.storage.MainThreadContinuation
import org.junit.jupiter.api.Test
import java.util.concurrent.CompletableFuture
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@io.github.toberocat.improvedfactions.testing.UnitTest
class MainThreadContinuationTest {
    @Test
    fun `completion only touches gameplay state through the scheduled continuation`() {
        val scheduled = mutableListOf<Runnable>()
        val continuation = MainThreadContinuation { scheduled += it }
        var observed: String? = null
        val stage = CompletableFuture.completedFuture("committed")

        continuation.resume(stage, { observed = it }, { error(it) })

        assertFalse(observed != null)
        assertEquals(1, scheduled.size)
        scheduled.single().run()
        assertEquals("committed", observed)
    }

    @Test
    fun `plain main thread action is scheduled and not run inline`() {
        val scheduled = mutableListOf<Runnable>()
        val continuation = MainThreadContinuation { scheduled += it }
        var called = false

        continuation.execute { called = true }

        assertFalse(called)
        scheduled.single().run()
        assertEquals(true, called)
    }
}
