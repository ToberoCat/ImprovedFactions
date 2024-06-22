package io.github.toberocat.improvedfactions.unit.claims.clustering

import io.github.toberocat.improvedfactions.claims.clustering.ChunkPosition
import io.github.toberocat.improvedfactions.claims.clustering.ClaimClusterDetector
import io.github.toberocat.improvedfactions.claims.clustering.ClaimQueryProvider
import io.github.toberocat.improvedfactions.claims.clustering.WorldPosition
import io.github.toberocat.improvedfactions.unit.ImprovedFactionsTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ClaimDetectorTest : ImprovedFactionsTest() {

    @Test
    fun `test cluster detection`() {
        val factionPositions = listOf(
            Pair(ChunkPosition(0, 0, ""), 1),
            Pair(ChunkPosition(1, 0, ""), 1),
            Pair(ChunkPosition(0, 1, ""), 1),

            Pair(ChunkPosition(2, 2, ""), 2),
            Pair(ChunkPosition(3, 2, ""), 2),
            Pair(ChunkPosition(3, 3, ""), 2)
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
            Pair(ChunkPosition(0, 0, ""), 1),
            Pair(ChunkPosition(1, 0, ""), 1)
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

        detector.removePosition(ChunkPosition(0, 0, ""))

        assertEquals(1, detector.clusterMap.size)
        assertEquals(1, detector.clusters.size)

        assertEquals(clusterIds[0], detector.getClusterId(ChunkPosition(1, 0, "")))
        assertNull(detector.getClusterId(ChunkPosition(0, 0, "")))
        assertEquals(1, detector.getCluster(ChunkPosition(1, 0, ""))?.getReadOnlyPositions()?.size)
        assertEquals(
            ChunkPosition(1, 0, ""),
            detector.getCluster(ChunkPosition(1, 0, ""))?.getReadOnlyPositions()?.first()
        )
    }

    @Test
    fun `test position removal split clusters`() {
        val positions = listOf(
            ChunkPosition(0, 0, ""),
            ChunkPosition(1, 0, ""),
            ChunkPosition(0, 1, "")
        )

        val factionPositions = positions.map { Pair(it, 1) }

        val detector = ClaimClusterDetector(DummyClaimQueryProvider(factionPositions))
        detector.detectClusters()

        assertEquals(3, detector.clusterMap.size)
        assertEquals(1, detector.clusters.size)

        detector.removePosition(ChunkPosition(0, 0, ""))

        assertEquals(2, detector.clusterMap.size)
        assertEquals(2, detector.clusters.size)
    }

    @Test
    fun `test position removal not split clusters`() {
        val positions = listOf(
            ChunkPosition(0, 0, ""),
            ChunkPosition(1, 0, ""),
            ChunkPosition(0, 1, ""),
            ChunkPosition(1, 1, "")
        )

        val factionPositions = positions.map { Pair(it, 1) }

        val detector = ClaimClusterDetector(DummyClaimQueryProvider(factionPositions))
        detector.detectClusters()

        assertEquals(4, detector.clusterMap.size)
        assertEquals(1, detector.clusters.size)

        detector.removePosition(ChunkPosition(0, 0, ""))

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
        detector.insertFactionPosition(ChunkPosition(1, 1, ""), 1)

        assertEquals(clusterId, detector.getClusterId(ChunkPosition(1, 1, "")))
    }

    @Test
    fun `test insert position connecting two clusters`() {
        val factionPositions = listOf(
            Pair(ChunkPosition(1, 1, ""), 1),
            Pair(ChunkPosition(5, 5, ""), 2),
            Pair(ChunkPosition(2, 1, ""), 1)
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
        assertEquals(clusterIds[0], detector.getClusterId(ChunkPosition(1, 1, "")))
        assertEquals(clusterIds[0], detector.getClusterId(ChunkPosition(2, 1, "")))
        assertEquals(clusterIds[1], detector.getClusterId(ChunkPosition(5, 5, "")))
    }

    @Test
    fun `test insert position connecting multiple clusters`() {
        val factionPositions = listOf(
            Pair(ChunkPosition(1, 1, ""), 1),
            Pair(ChunkPosition(5, 5, ""), 2),
            Pair(ChunkPosition(2, 1, ""), 1),
            Pair(ChunkPosition(10, 10, ""), 3),
            Pair(ChunkPosition(10, 9, ""), 3)
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
        assertEquals(clusterIds[0], detector.getClusterId(ChunkPosition(1, 1, "")))
        assertEquals(clusterIds[0], detector.getClusterId(ChunkPosition(2, 1, "")))
        assertEquals(clusterIds[1], detector.getClusterId(ChunkPosition(5, 5, "")))
        assertEquals(clusterIds[2], detector.getClusterId(ChunkPosition(10, 9, "")))
        assertEquals(clusterIds[2], detector.getClusterId(ChunkPosition(10, 10, "")))
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
        detector.insertFactionPosition(ChunkPosition(1, 1, ""), 1)
        detector.insertFactionPosition(ChunkPosition(2, 2, ""), 2)

        // Assert your expectations
        assertEquals(clusterIds[0], detector.getClusterId(ChunkPosition(1, 1, "")))
        assertEquals(clusterIds[1], detector.getClusterId(ChunkPosition(2, 2, "")))
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
        detector.insertFactionPosition(ChunkPosition(0, 0, ""), 1)
        detector.insertFactionPosition(ChunkPosition(1, 0, ""), 2)

        // Assert your expectations
        assertEquals(clusterIds[0], detector.getClusterId(ChunkPosition(0, 0, "")))
        assertEquals(clusterIds[1], detector.getClusterId(ChunkPosition(1, 0, "")))
    }

    @Test
    fun `test diagonals not being counted as neighbours`() {
        val positions = listOf(
            ChunkPosition(0, 0, ""),
            ChunkPosition(1, 0, ""),
            ChunkPosition(0, 1, ""),
            ChunkPosition(2, 2, ""),
            ChunkPosition(3, 2, ""),
            ChunkPosition(3, 3, "")
        )
        val factionPositions = positions.map { Pair(it, 1) }

        val detector = ClaimClusterDetector(DummyClaimQueryProvider(factionPositions))
        detector.detectClusters()

        assertEquals(2, detector.clusters.size)
    }

    @Test
    fun `test removing clusters`() {
        val factionPositions = listOf(
            Pair(ChunkPosition(0, 0, ""), 1),
            Pair(ChunkPosition(1, 0, ""), 1),
            Pair(ChunkPosition(0, 1, ""), 1),
            Pair(ChunkPosition(2, 2, ""), 2),
            Pair(ChunkPosition(3, 2, ""), 2),
            Pair(ChunkPosition(3, 3, ""), 2)
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

        detector.insertFactionPosition(ChunkPosition(4, 4, ""), 1)
        assertEquals(2, detector.clusters.size)
    }

    @Test
    fun `test detectOuterNodes`() {
        val factionPositions = listOf(
            Pair(ChunkPosition(0, 0, ""), 1),
            Pair(ChunkPosition(1, 0, ""), 1),
            Pair(ChunkPosition(2, 0, ""), 1),
            Pair(ChunkPosition(0, 1, ""), 1),
            Pair(ChunkPosition(2, 1, ""), 1),
            Pair(ChunkPosition(0, 2, ""), 1),
            Pair(ChunkPosition(1, 2, ""), 1),
            Pair(ChunkPosition(2, 2, ""), 1)
        )

        val detector = ClaimClusterDetector(DummyClaimQueryProvider(factionPositions))
        detector.detectClusters()
        val cluster = detector.clusters.values.first()
        val outerNodes = cluster.getOuterNodes()

        kotlin.test.assertEquals(2, outerNodes.size)
        kotlin.test.assertEquals(5, outerNodes[0].size)
        kotlin.test.assertEquals(5, outerNodes[1].size)
        kotlin.test.assertEquals(
            listOf(
                WorldPosition(world = "", x = 0, y = 0),
                WorldPosition(world = "", x = 48, y = 0),
                WorldPosition(world = "", x = 48, y = 48),
                WorldPosition(world = "", x = 0, y = 48),
                WorldPosition(world = "", x = 0, y = 0)
            ), outerNodes[0]
        )
        kotlin.test.assertEquals(
            listOf(
                WorldPosition(world = "", x = 16, y = 16),
                WorldPosition(world = "", x = 32, y = 16),
                WorldPosition(world = "", x = 32, y = 32),
                WorldPosition(world = "", x = 16, y = 32),
                WorldPosition(world = "", x = 16, y = 16)
            ), outerNodes[1]
        )

        detector.insertFactionPosition(ChunkPosition(1, 1, ""), 1)
        val updatedOuterNodes = detector.clusters.values.first().getOuterNodes()

        kotlin.test.assertEquals(1, updatedOuterNodes.size)
        kotlin.test.assertEquals(5, updatedOuterNodes[0].size)
        kotlin.test.assertEquals(
            listOf(
                WorldPosition(world = "", x = 0, y = 0),
                WorldPosition(world = "", x = 48, y = 0),
                WorldPosition(world = "", x = 48, y = 48),
                WorldPosition(world = "", x = 0, y = 48),
                WorldPosition(world = "", x = 0, y = 0)
            ), updatedOuterNodes[0]
        )
    }

    //
    @Test
    fun `test detectOuterNodes lshape`() {
        val factionPositions = listOf(
            Pair(ChunkPosition(0, 0, ""), 1),
            Pair(ChunkPosition(1, 0, ""), 1),
            Pair(ChunkPosition(1, 1, ""), 1),
            Pair(ChunkPosition(1, 2, ""), 1),
            Pair(ChunkPosition(2, 1, ""), 1)
        )

        val detector = ClaimClusterDetector(DummyClaimQueryProvider(factionPositions))
        detector.detectClusters()
        val cluster = detector.clusters.values.first()
        val outerNodes = cluster.getOuterNodes()

        outerNodes[0].forEach { println(it) }
        kotlin.test.assertEquals(1, outerNodes.size)
        kotlin.test.assertEquals(11, outerNodes[0].size)
        kotlin.test.assertEquals(
            listOf(
                WorldPosition(world = "", x = 0, y = 0),
                WorldPosition(world = "", x = 0, y = 16),
                WorldPosition(world = "", x = 16, y = 16),
                WorldPosition(world = "", x = 16, y = 48),
                WorldPosition(world = "", x = 32, y = 48),
                WorldPosition(world = "", x = 32, y = 32),
                WorldPosition(world = "", x = 48, y = 32),
                WorldPosition(world = "", x = 48, y = 16),
                WorldPosition(world = "", x = 32, y = 16),
                WorldPosition(world = "", x = 32, y = 0),
                WorldPosition(world = "", x = 0, y = 0)
            ), outerNodes[0]
        )
    }

    private fun predeterminedIdGenerator(ids: List<UUID>): () -> UUID {
        var index = 0
        return { ids[index++] }
    }
}

class DummyClaimQueryProvider(private val positions: List<Pair<ChunkPosition, Int>>) : ClaimQueryProvider {
    private val positionsOnly = positions.map { it.first }

    override fun queryNeighbours(position: ChunkPosition) = listOf(
        ChunkPosition(position.x + 1, position.y, position.world),
        ChunkPosition(position.x - 1, position.y, position.world),
        ChunkPosition(position.x, position.y + 1, position.world),
        ChunkPosition(position.x, position.y - 1, position.world)
    ).filter { it in positionsOnly }

    override fun allFactionPositions() = positions

    override fun allZonePositions(): List<Pair<ChunkPosition, String>> = emptyList()
}