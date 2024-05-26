package io.github.toberocat.improvedfactions.unit.claims.clustering

import io.github.toberocat.improvedfactions.claims.clustering.Position
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class PositionTest {
    @Test
    fun `test equals`() {
        val position1 = Position(1, 2,  "",3)
        val position2 = Position(1, 2,  "",3)
        val position3 = Position(4, 5,  "",6)

        assertEquals(position1, position2)
        assertNotEquals(position1, position3)
    }

    @Test
    fun `test hash code`() {
        val position1 = Position(1, 2,  "",3)
        val position2 = Position(1, 2,  "",3)
        val position3 = Position(4, 5,  "",6)

        assertEquals(position1.hashCode(), position2.hashCode())
        assertNotEquals(position1.hashCode(), position3.hashCode())
    }
}