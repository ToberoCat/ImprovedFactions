package io.github.toberocat.improvedfactions.claims.clustering

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ClaimDetectorTest {

    @Test
    fun `test cluster detection`() {
        val factionPositions = listOf(
            Pair(Position(0, 0, ""), 1),
            Pair(Position(1, 0, ""), 1),
            Pair(Position(0, 1, ""), 1),

            Pair(Position(2, 2, ""), 2),
            Pair(Position(3, 2, ""), 2),
            Pair(Position(3, 3, ""), 2)
        )
        val positions = factionPositions.map { it.first }
        val detector = ClaimClusterDetector(DummyClaimQueryProvider(factionPositions))
        detector.detectClusters()

        assertEquals(2, detector.clusters.size)
        assertEquals(2, detector.clusterMap.values.distinct().count())

        assertNotNull(detector.getClusterId(positions[0]))
        assertTrue(
            detector.getClusterId(positions[0]) == detector.getClusterId(positions[1]) &&
                    detector.getClusterId(positions[1]) == detector.getClusterId(positions[2])
        )

        assertNotNull(detector.getClusterId(positions[3]))
        assertTrue(
            detector.getClusterId(positions[3]) == detector.getClusterId(positions[4]) &&
                    detector.getClusterId(positions[4]) == detector.getClusterId(positions[5])
        )

        assertTrue(
            detector.getCluster(positions[0])?.getReadOnlyPositions()?.containsAll(
                listOf(
                    positions[0],
                    positions[1],
                    positions[2]
                )
            ) == true
        )
        assertTrue(
            detector.getCluster(positions[3])?.getReadOnlyPositions()?.containsAll(
                listOf(
                    positions[3],
                    positions[4],
                    positions[5]
                )
            ) == true
        )
    }

    @Test
    fun `test position removal`() {
        val factionPositions = listOf(
            Pair(Position(0, 0, ""), 1),
            Pair(Position(1, 0, ""), 1)
        )

        val clusterIds = listOf(
            UUID.randomUUID(),
            UUID.randomUUID()
        )

        val detector = ClaimClusterDetector(
            DummyClaimQueryProvider(factionPositions),
            predeterminedIdGenerator(clusterIds)
        )
        detector.detectClusters()

        assertEquals(2, detector.clusterMap.size)
        assertEquals(1, detector.clusters.size)

        detector.removePosition(Position(0, 0, ""))

        assertEquals(1, detector.clusterMap.size)
        assertEquals(1, detector.clusters.size)

        assertEquals(clusterIds[0], detector.getClusterId(Position(1, 0, "")))
        assertNull(detector.getClusterId(Position(0, 0, "")))
        assertEquals(1, detector.getCluster(Position(1, 0, ""))?.getReadOnlyPositions()?.size)
        assertEquals(Position(1, 0, ""), detector.getCluster(Position(1, 0, ""))?.getReadOnlyPositions()?.first())
    }

    @Test
    fun `test position removal split clusters`() {
        val positions = listOf(
            Position(0, 0, ""),
            Position(1, 0, ""),
            Position(0, 1, "")
        )

        val factionPositions = positions.map { Pair(it, 1) }

        val detector = ClaimClusterDetector(DummyClaimQueryProvider(factionPositions))
        detector.detectClusters()

        assertEquals(3, detector.clusterMap.size)
        assertEquals(1, detector.clusters.size)

        detector.removePosition(Position(0, 0, ""))

        assertEquals(2, detector.clusterMap.size)
        assertEquals(2, detector.clusters.size)
    }

    @Test
    fun `test position removal not split clusters`() {
        val positions = listOf(
            Position(0, 0, ""),
            Position(1, 0, ""),
            Position(0, 1, ""),
            Position(1, 1, "")
        )

        val factionPositions = positions.map { Pair(it, 1) }

        val detector = ClaimClusterDetector(DummyClaimQueryProvider(factionPositions))
        detector.detectClusters()

        assertEquals(4, detector.clusterMap.size)
        assertEquals(1, detector.clusters.size)

        detector.removePosition(Position(0, 0, ""))

        assertEquals(3, detector.clusterMap.size)
        assertEquals(1, detector.clusters.size)
    }

    @Test
    fun `test insert position into empty detector`() {
        val clusterId = UUID.randomUUID()
        val detector = ClaimClusterDetector(
            DummyClaimQueryProvider(emptyList()),
            predeterminedIdGenerator(listOf(clusterId))
        )
        detector.insertFactionPosition(Position(1, 1, ""), 1)

        assertEquals(clusterId, detector.getClusterId(Position(1, 1, "")))
    }

    @Test
    fun `test insert position connecting two clusters`() {
        val factionPositions = listOf(
            Pair(Position(1, 1, ""), 1),
            Pair(Position(5, 5, ""), 2),
            Pair(Position(2, 1, ""), 1)
        )

        val clusterIds = listOf(
            UUID.randomUUID(),
            UUID.randomUUID()
        )

        val detector = ClaimClusterDetector(
            DummyClaimQueryProvider(factionPositions),
            predeterminedIdGenerator(clusterIds)
        )
        factionPositions.forEach { detector.insertFactionPosition(it.first, it.second) }

        // Assert your expectations
        assertEquals(clusterIds[0], detector.getClusterId(Position(1, 1, "")))
        assertEquals(clusterIds[0], detector.getClusterId(Position(2, 1, "")))
        assertEquals(clusterIds[1], detector.getClusterId(Position(5, 5, "")))
    }

    @Test
    fun `test insert position connecting multiple clusters`() {
        val factionPositions = listOf(
            Pair(Position(1, 1, ""), 1),
            Pair(Position(5, 5, ""), 2),
            Pair(Position(2, 1, ""), 1),
            Pair(Position(10, 10, ""), 3),
            Pair(Position(10, 9, ""), 3)
        )

        val clusterIds = listOf(
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID()
        )
        val detector = ClaimClusterDetector(
            DummyClaimQueryProvider(factionPositions),
            predeterminedIdGenerator(clusterIds)
        )

        factionPositions.forEach { detector.insertFactionPosition(it.first, it.second) }

        // Assert your expectations
        assertEquals(clusterIds[0], detector.getClusterId(Position(1, 1, "")))
        assertEquals(clusterIds[0], detector.getClusterId(Position(2, 1, "")))
        assertEquals(clusterIds[1], detector.getClusterId(Position(5, 5, "")))
        assertEquals(clusterIds[2], detector.getClusterId(Position(10, 9, "")))
        assertEquals(clusterIds[2], detector.getClusterId(Position(10, 10, "")))
    }


    @Test
    fun `test insert position with same ID different XY`() {
        val clusterIds = listOf(
            UUID.randomUUID(),
            UUID.randomUUID()
        )
        val detector = ClaimClusterDetector(
            DummyClaimQueryProvider(emptyList()),
            predeterminedIdGenerator(clusterIds)
        )
        // Insert positions with same id but different x and y
        detector.insertFactionPosition(Position(1, 1, ""), 1)
        detector.insertFactionPosition(Position(2, 2, ""), 2)

        // Assert your expectations
        assertEquals(clusterIds[0], detector.getClusterId(Position(1, 1, "")))
        assertEquals(clusterIds[1], detector.getClusterId(Position(2, 2, "")))
    }

    @Test
    fun `test positions next to each other with different ids`() {
        val clusterIds = listOf(
            UUID.randomUUID(),
            UUID.randomUUID()
        )

        val detector = ClaimClusterDetector(
            DummyClaimQueryProvider(emptyList()),
            predeterminedIdGenerator(clusterIds)
        )
        // Insert positions with different x, y, and id
        detector.insertFactionPosition(Position(0, 0, ""), 1)
        detector.insertFactionPosition(Position(1, 0, ""), 2)

        // Assert your expectations
        assertEquals(clusterIds[0], detector.getClusterId(Position(0, 0, "")))
        assertEquals(clusterIds[1], detector.getClusterId(Position(1, 0, "")))
    }

    @Test
    fun `test diagonals not being counted as neighbours`() {
        val positions = listOf(
            Position(0, 0, ""),
            Position(1, 0, ""),
            Position(0, 1, ""),
            Position(2, 2, ""),
            Position(3, 2, ""),
            Position(3, 3, "")
        )
        val factionPositions = positions.map { Pair(it, 1) }

        val detector = ClaimClusterDetector(DummyClaimQueryProvider(factionPositions))
        detector.detectClusters()

        assertEquals(2, detector.clusters.size)
    }

    @Test
    fun `test removing clusters`() {
        val factionPositions = listOf(
            Pair(Position(0, 0, ""), 1),
            Pair(Position(1, 0, ""), 1),
            Pair(Position(0, 1, ""), 1),
            Pair(Position(2, 2, ""), 2),
            Pair(Position(3, 2, ""), 2),
            Pair(Position(3, 3, ""), 2)
        )
        val clusterIds = listOf(
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID()
        )

        val detector = ClaimClusterDetector(
            DummyClaimQueryProvider(factionPositions),
            predeterminedIdGenerator(clusterIds)
        )
        detector.detectClusters()

        assertEquals(2, detector.clusters.size)

        detector.removeCluster(clusterIds[0])
        assertEquals(1, detector.clusters.size)

        detector.insertFactionPosition(Position(4, 4, ""), 1)
        assertEquals(2, detector.clusters.size)
    }

    private fun predeterminedIdGenerator(ids: List<UUID>): () -> UUID {
        var index = 0
        return { ids[index++] }
    }
}

class DummyClaimQueryProvider(private val positions: List<Pair<Position, Int>>) : ClaimQueryProvider {
    private val positionsOnly = positions.map { it.first }

    override fun queryNeighbours(position: Position) = listOf(
        Position(position.x + 1, position.y, position.world),
        Position(position.x - 1, position.y, position.world),
        Position(position.x, position.y + 1, position.world),
        Position(position.x, position.y - 1, position.world)
    ).filter { it in positionsOnly }

    override fun allFactionPositions() = positions

    override fun allZonePositions(): List<Pair<Position, String>> = emptyList()
}