package io.github.toberocat.improvedfactions.unit.claims.clustering

import io.github.toberocat.improvedfactions.claims.clustering.ClaimClusterDetector
import io.github.toberocat.improvedfactions.claims.clustering.ClaimQueryProvider
import io.github.toberocat.improvedfactions.claims.clustering.Position
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ClaimDetectorTest {

    @Test
    fun `test cluster detection`() {
        val positions = listOf(
            Position(0, 0, "", 1),
            Position(1, 0, "", 1),
            Position(0, 1, "", 1),
            Position(2, 2, "", 2),
            Position(3, 2, "", 2),
            Position(3, 3, "", 2)
        )
        val detector = ClaimClusterDetector(DummyClaimQueryProvider(positions))
        detector.detectClusters()


        assertEquals(2, detector.clusters.size)
        assertEquals(2, detector.clusterMap.values.distinct().count())

        assertNotNull(detector.getClusterId(positions[0]))
        assertTrue(
            detector.getClusterId(positions[0]) == detector.getClusterId(positions[1]) && detector.getClusterId(
                positions[1]
            ) == detector.getClusterId(positions[2])
        )

        assertNotNull(detector.getClusterId(positions[3]))
        assertTrue(
            detector.getClusterId(positions[3]) == detector.getClusterId(positions[4]) && detector.getClusterId(
                positions[4]
            ) == detector.getClusterId(positions[5])
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
        val positions = listOf(
            Position(0, 0, "", 1),
            Position(1, 0, "", 1),
        )
        val clusterIds = listOf(
            UUID.randomUUID(),
            UUID.randomUUID()
        )

        val detector = ClaimClusterDetector(
            DummyClaimQueryProvider(positions),
            predeterminedIdGenerator(clusterIds)
        )
        detector.detectClusters()

        assertEquals(2, detector.clusterMap.size)
        assertEquals(1, detector.clusters.size)

        detector.removePosition(Position(0, 0, "", 1))

        assertEquals(1, detector.clusterMap.size)
        assertEquals(1, detector.clusters.size)

        assertEquals(clusterIds[0], detector.getClusterId(Position(1, 0, "", 1)))
        assertNull(detector.getClusterId(Position(0, 0, "", 1)))
        assertEquals(1, detector.getCluster(Position(1, 0, "", 1))?.getReadOnlyPositions()?.size)
        assertEquals(Position(1, 0, "", 1), detector.getCluster(Position(1, 0, "", 1))?.getReadOnlyPositions()?.first())
    }

    @Test
    fun `test position removal split clusters`() {
        val positions = listOf(
            Position(0, 0, "", 1),
            Position(1, 0, "", 1),
            Position(0, 1, "", 1)
        )

        val detector = ClaimClusterDetector(DummyClaimQueryProvider(positions))
        detector.detectClusters()

        assertEquals(3, detector.clusterMap.size)
        assertEquals(1, detector.clusters.size)

        detector.removePosition(Position(0, 0, "", 1))

        assertEquals(2, detector.clusterMap.size)
        assertEquals(2, detector.clusters.size)

    }

    @Test
    fun `test position removal not split clusters`() {
        val positions = listOf(
            Position(0, 0, "", 1),
            Position(1, 0, "", 1),
            Position(0, 1, "", 1),
            Position(1, 1, "", 1)
        )

        val detector = ClaimClusterDetector(DummyClaimQueryProvider(positions))
        detector.detectClusters()

        assertEquals(4, detector.clusterMap.size)
        assertEquals(1, detector.clusters.size)

        detector.removePosition(Position(0, 0, "", 1))

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
        detector.insertPosition(Position(1, 1, "", 1))

        assertEquals(clusterId, detector.getClusterId(Position(1, 1, "", 1)))
    }

    @Test
    fun `test insert position connecting two clusters`() {
        val positions = listOf(
            Position(1, 1, "", 1),
            Position(5, 5, "", 2),
            Position(2, 1, "", 1)
        )
        val clusterIds = listOf(
            UUID.randomUUID(),
            UUID.randomUUID()
        )

        val detector = ClaimClusterDetector(
            DummyClaimQueryProvider(positions),
            predeterminedIdGenerator(clusterIds)
        )
        positions.forEach { detector.insertPosition(it) }

        // Assert your expectations
        assertEquals(clusterIds[0], detector.getClusterId(Position(1, 1, "", 1)))
        assertEquals(clusterIds[0], detector.getClusterId(Position(2, 1, "", 1)))
        assertEquals(clusterIds[1], detector.getClusterId(Position(5, 5, "", 2)))
    }

    @Test
    fun `test insert position connecting multiple clusters`() {
        val positions = listOf(
            Position(1, 1, "", 1),
            Position(5, 5, "", 2),
            Position(2, 1, "", 1),
            Position(10, 10, "", 3),
            Position(10, 9, "", 3)
        )
        val clusterIds = listOf(
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID()
        )
        val detector = ClaimClusterDetector(
            DummyClaimQueryProvider(positions),
            predeterminedIdGenerator(clusterIds)
        )


        positions.forEach { detector.insertPosition(it) }

        // Assert your expectations
        assertEquals(clusterIds[0], detector.getClusterId(Position(1, 1, "", 1)))
        assertEquals(clusterIds[0], detector.getClusterId(Position(2, 1, "", 1)))
        assertEquals(clusterIds[1], detector.getClusterId(Position(5, 5, "", 2)))
        assertEquals(clusterIds[2], detector.getClusterId(Position(10, 9, "", 3)))
        assertEquals(clusterIds[2], detector.getClusterId(Position(10, 10, "", 3)))
    }

    @Test
    fun `test insert positionW with same XY different ID`() {
        val clusterIds = listOf(
            UUID.randomUUID(),
        )
        val detector = ClaimClusterDetector(
            DummyClaimQueryProvider(emptyList()),
            predeterminedIdGenerator(clusterIds)
        )
        // Insert positions with same x and y but different id
        detector.insertPosition(Position(1, 1, "", 1))
        assertThrows<IllegalArgumentException> { detector.insertPosition(Position(1, 1, "", 2)) }

        // Assert your expectations
        assertEquals(clusterIds[0], detector.getClusterId(Position(1, 1, "", 1)))
        assertEquals(clusterIds[0], detector.getClusterId(Position(1, 1, "", 2)))
    }

    @Test
    fun `test insert position with same ID different XY`() {
        val clusterId = listOf(
            UUID.randomUUID(),
            UUID.randomUUID()
        )
        val detector = ClaimClusterDetector(
            DummyClaimQueryProvider(emptyList()),
            predeterminedIdGenerator(clusterId)
        )
        // Insert positions with same id but different x and y
        detector.insertPosition(Position(1, 1, "", 1))
        detector.insertPosition(Position(2, 2, "", 1))

        // Assert your expectations
        assertEquals(clusterId[0], detector.getClusterId(Position(1, 1, "", 1)))
        assertEquals(clusterId[1], detector.getClusterId(Position(2, 2, "", 1)))
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
        detector.insertPosition(Position(0, 0, "", 1))
        detector.insertPosition(Position(1, 0, "", 2))

        // Assert your expectations
        assertEquals(clusterIds[0], detector.getClusterId(Position(0, 0, "", 1)))
        assertEquals(clusterIds[1], detector.getClusterId(Position(1, 0, "", 2)))
    }

    @Test
    fun `test get cluster id return same cluster when faction id is not same`() {
        val positions = listOf(
            Position(0, 0, "", 1),
            Position(1, 0, "", 1),
            Position(0, 1, "", 1),
            Position(2, 2, "", 2),
            Position(3, 2, "", 2),
            Position(3, 3, "", 2)
        )
        val detector = ClaimClusterDetector(DummyClaimQueryProvider(positions))
        detector.detectClusters()

        assertNull(detector.getCluster(Position(2, 2, "", 1)))
    }

    @Test
    fun `test get cluster return null cluster when faction id is not same`() {
        val positions = listOf(
            Position(0, 0, "", 1),
            Position(1, 0, "", 1),
            Position(0, 1, "", 1),
            Position(2, 2, "", 2),
            Position(3, 2, "", 2),
            Position(3, 3, "", 2)
        )
        val detector = ClaimClusterDetector(DummyClaimQueryProvider(positions))
        detector.detectClusters()

        assertNull(detector.getCluster(Position(2, 2, "", 1)))
    }

    @Test
    fun `test diagonals not being counted as neighbours`() {
        val positions = listOf(
            Position(0, 0, "", 1),
            Position(1, 0, "", 1),
            Position(0, 1, "", 1),
            Position(2, 2, "", 1),
            Position(3, 2, "", 1),
            Position(3, 3, "", 1)
        )
        val detector = ClaimClusterDetector(DummyClaimQueryProvider(positions))
        detector.detectClusters()

        assertEquals(2, detector.clusters.size)
    }

    @Test
    fun `test removing clusters`() {
        val positions = listOf(
            Position(0, 0, "", 1),
            Position(1, 0, "", 1),
            Position(0, 1, "", 1),
            Position(2, 2, "", 2),
            Position(3, 2, "", 2),
            Position(3, 3, "", 2)
        )
        val clusterIds = listOf(
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID()
        )

        val detector = ClaimClusterDetector(
            DummyClaimQueryProvider(positions),
            predeterminedIdGenerator(clusterIds)
        )
        detector.detectClusters()

        assertEquals(2, detector.clusters.size)

        detector.removeCluster(clusterIds[0])
        assertEquals(1, detector.clusters.size)

        detector.insertPosition(Position(4, 4, "", 1))
        assertEquals(2, detector.clusters.size)
    }

    private fun predeterminedIdGenerator(ids: List<UUID>): () -> UUID {
        var index = 0
        return { ids[index++] }
    }
}

class DummyClaimQueryProvider(private val positions: List<Position>) : ClaimQueryProvider {
    override fun queryNeighbours(position: Position) = listOf(
        Position(position.x + 1, position.y, position.world, position.factionId),
        Position(position.x - 1, position.y, position.world, position.factionId),
        Position(position.x, position.y + 1, position.world, position.factionId),
        Position(position.x, position.y - 1, position.world, position.factionId),
    ).filter { it in positions }

    override fun all() = positions

}