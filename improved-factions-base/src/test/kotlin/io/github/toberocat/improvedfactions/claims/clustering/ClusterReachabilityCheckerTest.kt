package io.github.toberocat.improvedfactions.claims.clustering

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class ClusterReachabilityCheckerTest {
    @Test
    fun `test get unreachable positions`() {
        val positions = setOf(
            Position(0, 0, ""),
            Position(0, 1,  ""),
            Position(1, 0,  ""),
            Position(1, 1,  ""),
            Position(2, 2,  "")
        )

        val checker = ClusterReachabilityChecker(positions)

        val unreachablePositions = checker.getUnreachablePositions()

        assertEquals(setOf(Position(2, 2,  "")), unreachablePositions)
    }

    @Test
    fun `test get unreachable positions empty list`() {
        val positions = emptySet<Position>()

        val checker = ClusterReachabilityChecker(positions)

        val unreachablePositions = checker.getUnreachablePositions()

        assertEquals(emptySet<Position>(), unreachablePositions)
    }

    @Test
    fun `test get unreachable positions single element`() {
        val positions = setOf(Position(0, 0, ""))

        val checker = ClusterReachabilityChecker(positions)

        val unreachablePositions = checker.getUnreachablePositions()

        assertEquals(emptySet<Position>(), unreachablePositions)
    }


    @Test
    fun `test get unreachable positions all unreachable`() {
        val positions = setOf(
            Position(0, 0,  ""),
            Position(1, 1,  ""),
            Position(2, 2,  "")
        )

        val checker = ClusterReachabilityChecker(positions)

        val unreachablePositions = checker.getUnreachablePositions()

        assertEquals(
            setOf(
                Position(1, 1,  ""),
                Position(2, 2,  "")
            ), unreachablePositions
        )
    }

    @Test
    fun `test get unreachable positions large grid`() {
        val gridSize = 100
        val positions = (0 until gridSize).flatMap { x ->
            (0 until gridSize).map { y ->
                Position(x, y,  "")
            }
        }.toSet()

        val checker = ClusterReachabilityChecker(positions)

        val unreachablePositions = checker.getUnreachablePositions()

        assertEquals(emptySet<Position>(), unreachablePositions)
    }
}