package io.github.toberocat.improvedfactions.claims.clustering

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException
import kotlin.test.Test

class ClusterReachabilityCheckerTest {
    @Test
    fun `test get unreachable positions`() {
        val positions = listOf(
            Position(0, 0, 1),
            Position(0, 1, 1),
            Position(1, 0, 1),
            Position(1, 1, 1),
            Position(2, 2, 1)
        )

        val checker = ClusterReachabilityChecker(positions)

        val unreachablePositions = checker.getUnreachablePositions()

        assertEquals(setOf(Position(2, 2, 1)), unreachablePositions)
    }

    @Test
    fun `test get unreachable positions empty list`() {
        val positions = emptyList<Position>()

        val checker = ClusterReachabilityChecker(positions)

        val unreachablePositions = checker.getUnreachablePositions()

        assertEquals(emptySet<Position>(), unreachablePositions)
    }

    @Test
    fun `test get unreachable positions single element`() {
        val positions = listOf(Position(0, 0, 1))

        val checker = ClusterReachabilityChecker(positions)

        val unreachablePositions = checker.getUnreachablePositions()

        assertEquals(emptySet<Position>(), unreachablePositions)
    }


    @Test
    fun `test get unreachable positions all unreachable`() {
        val positions = listOf(
            Position(0, 0, 1),
            Position(1, 1, 1),
            Position(2, 2, 1)
        )

        val checker = ClusterReachabilityChecker(positions)

        val unreachablePositions = checker.getUnreachablePositions()

        assertEquals(
            setOf(
                Position(1, 1, 1),
                Position(2, 2, 1)
            ), unreachablePositions
        )
    }

    @Test
    fun `test get different claim types`() {
        val positions = listOf(
            Position(0, 0, 1),
            Position(0, 1, 1),
            Position(1, 0, 1),
            Position(1, 1, 1),
            Position(4, 4, 2),
            Position(4, 5, 2),
            Position(5, 4, 2),
            Position(5, 5, 2)
        )

        val checker = ClusterReachabilityChecker(positions)
        assertThrows<IllegalArgumentException> { checker.getUnreachablePositions() }
    }

    @Test
    fun `test get unreachable positions large grid`() {
        val gridSize = 100
        val positions = (0 until gridSize).flatMap { x ->
            (0 until gridSize).map { y ->
                Position(x, y, 1)
            }
        }

        val checker = ClusterReachabilityChecker(positions)

        val unreachablePositions = checker.getUnreachablePositions()

        assertEquals(emptySet<Position>(), unreachablePositions)
    }
}