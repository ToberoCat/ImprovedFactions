package io.github.toberocat.improvedfactions.claims.clustering

import io.github.toberocat.improvedfactions.claims.clustering.position.ChunkPosition
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import kotlin.test.Test

class ChunkPositionTest {
    @Test
    fun `test equals`() {
        val position1 = ChunkPosition(1, 2,  "")
        val position2 = ChunkPosition(1, 2,  "")
        val position3 = ChunkPosition(4, 5,  "")

        assertEquals(position1, position2)
        assertNotEquals(position1, position3)
    }

    @Test
    fun `test hash code`() {
        val position1 = ChunkPosition(1, 2,  "")
        val position2 = ChunkPosition(1, 2,  "")
        val position3 = ChunkPosition(4, 5,  "")

        assertEquals(position1.hashCode(), position2.hashCode())
        assertNotEquals(position1.hashCode(), position3.hashCode())
    }
}