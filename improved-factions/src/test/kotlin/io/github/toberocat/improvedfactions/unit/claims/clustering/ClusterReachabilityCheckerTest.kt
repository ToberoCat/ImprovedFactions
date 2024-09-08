package io.github.toberocat.improvedfactions.unit.claims.clustering

import io.github.toberocat.improvedfactions.claims.clustering.position.ChunkPosition
import io.github.toberocat.improvedfactions.claims.clustering.detector.ClusterReachabilityChecker
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class ClusterReachabilityCheckerTest {
    @Test
    fun `test get unreachable positions`() {
        val positions = setOf(
            ChunkPosition(0, 0, ""),
            ChunkPosition(0, 1,  ""),
            ChunkPosition(1, 0,  ""),
            ChunkPosition(1, 1,  ""),
            ChunkPosition(2, 2,  "")
        )

        val checker = ClusterReachabilityChecker(positions)

        val unreachablePositions = checker.getUnreachablePositions()

        assertEquals(setOf(ChunkPosition(2, 2,  "")), unreachablePositions)
    }

    @Test
    fun `test get unreachable positions empty list`() {
        val positions = emptySet<ChunkPosition>()

        val checker = ClusterReachabilityChecker(positions)

        val unreachablePositions = checker.getUnreachablePositions()

        assertEquals(emptySet<ChunkPosition>(), unreachablePositions)
    }

    @Test
    fun `test get unreachable positions single element`() {
        val positions = setOf(ChunkPosition(0, 0, ""))

        val checker = ClusterReachabilityChecker(positions)

        val unreachablePositions = checker.getUnreachablePositions()

        assertEquals(emptySet<ChunkPosition>(), unreachablePositions)
    }


    @Test
    fun `test get unreachable positions all unreachable`() {
        val positions = setOf(
            ChunkPosition(0, 0,  ""),
            ChunkPosition(1, 1,  ""),
            ChunkPosition(2, 2,  "")
        )

        val checker = ClusterReachabilityChecker(positions)

        val unreachablePositions = checker.getUnreachablePositions()

        assertEquals(
            setOf(
                ChunkPosition(1, 1,  ""),
                ChunkPosition(2, 2,  "")
            ), unreachablePositions
        )
    }

    @Test
    fun `test get unreachable positions large grid`() {
        val gridSize = 100
        val positions = (0 until gridSize).flatMap { x ->
            (0 until gridSize).map { y ->
                ChunkPosition(x, y,  "")
            }
        }.toSet()

        val checker = ClusterReachabilityChecker(positions)

        val unreachablePositions = checker.getUnreachablePositions()

        assertEquals(emptySet<ChunkPosition>(), unreachablePositions)
    }
}