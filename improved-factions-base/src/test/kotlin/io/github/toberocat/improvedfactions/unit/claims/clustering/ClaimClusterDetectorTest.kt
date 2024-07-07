package io.github.toberocat.improvedfactions.unit.claims.clustering

import io.github.toberocat.improvedfactions.claims.FactionClaim
import io.github.toberocat.improvedfactions.claims.clustering.detector.ClaimClusterDetector
import io.github.toberocat.improvedfactions.claims.clustering.position.ChunkPosition
import io.github.toberocat.improvedfactions.claims.clustering.position.WorldPosition
import io.github.toberocat.improvedfactions.claims.clustering.query.ClaimQueryProvider
import io.github.toberocat.improvedfactions.claims.getFactionClaim
import io.github.toberocat.improvedfactions.unit.ImprovedFactionsTest
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*
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
        val detector = ClaimClusterDetector(DummyClaimQueryProvider(factionPositions))
        detector.detectClusters()

        assertEquals(2, detector.clusters.size)

        assertNotNull(detector.getClusterId(factionClaim(factionPositions[0])))
        assertTrue(
            detector.getClusterId(factionClaim(factionPositions[0])) == detector.getClusterId(
                factionClaim(
                    factionPositions[1]
                )
            ) &&
                    detector.getClusterId(factionClaim(factionPositions[1])) == detector.getClusterId(
                factionClaim(
                    factionPositions[2]
                )
            )
        )

        assertNotNull(detector.getClusterId(factionClaim(factionPositions[3])))
        assertTrue(
            detector.getClusterId(factionClaim(factionPositions[3])) == detector.getClusterId(
                factionClaim(
                    factionPositions[4]
                )
            ) &&
                    detector.getClusterId(factionClaim(factionPositions[4])) == detector.getClusterId(
                factionClaim(
                    factionPositions[5]
                )
            )
        )

        assertTrue(
            transaction {
                detector.getCluster(factionClaim(factionPositions[0]))?.getClaims()?.containsAll(
                    listOf(
                        factionClaim(factionPositions[0]),
                        factionClaim(factionPositions[1]),
                        factionClaim(factionPositions[2])
                    )
                ) == true
            }
        )
        assertTrue(
            transaction {
                detector.getCluster(factionClaim(factionPositions[3]))?.getClaims()?.containsAll(
                    listOf(
                        factionClaim(factionPositions[3]),
                        factionClaim(factionPositions[4]),
                        factionClaim(factionPositions[5])
                    )
                ) == true
            }
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

        assertEquals(2, detector.getAffectedClaims())
        assertEquals(1, detector.clusters.size)

        detector.removePosition(factionClaim(0, 0, "", 1))

        assertEquals(1, detector.getAffectedClaims())
        assertEquals(1, detector.clusters.size)

        assertEquals(clusterIds[0], detector.getClusterId(factionClaim(1, 0, "", 1)))
        assertNull(detector.getClusterId(factionClaim(0, 0, "", 1)))
        assertEquals(1, transaction { detector.getCluster(factionClaim(1, 0, "", 1))?.getClaims()?.size })
        transaction {
            assertEquals(
                factionClaim(1, 0, "", 1),
                detector.getCluster(factionClaim(1, 0, "", 1))?.getClaims()?.first()
            )
        }
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

        assertEquals(3, detector.getAffectedClaims())
        assertEquals(1, detector.clusters.size)

        detector.removePosition(factionClaim(0, 0, "", 1))

        assertEquals(2, detector.getAffectedClaims())
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

        assertEquals(4, detector.getAffectedClaims())
        assertEquals(1, detector.clusters.size)

        detector.removePosition(factionClaim(0, 0, "", 1))

        assertEquals(3, detector.getAffectedClaims())
        assertEquals(1, detector.clusters.size)
    }

    @Test
    fun `test insert position into empty detector`() {
        val clusterId = UUID.randomUUID()
        val detector = ClaimClusterDetector(
            DummyClaimQueryProvider(emptyList()),
            predeterminedIdGenerator(listOf(clusterId))
        )
        detector.insertFactionPosition(factionClaim(1, 1, "", 1), 1)

        assertEquals(clusterId, detector.getClusterId(factionClaim(1, 1, "", 1)))
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
        factionPositions.forEach { (c, id) -> detector.insertFactionPosition(factionClaim(c.x, c.y, c.world, id), id) }

        // Assert your expectations
        assertEquals(clusterIds[0], detector.getClusterId(factionClaim(1, 1, "", 1)))
        assertEquals(clusterIds[0], detector.getClusterId(factionClaim(2, 1, "", 1)))
        assertEquals(clusterIds[1], detector.getClusterId(factionClaim(5, 5, "", 2)))
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

        factionPositions.forEach { (c, id) -> detector.insertFactionPosition(factionClaim(c.x, c.y, c.world, id), id) }

        // Assert your expectations
        assertEquals(clusterIds[0], detector.getClusterId(factionClaim(1, 1, "", 1)))
        assertEquals(clusterIds[0], detector.getClusterId(factionClaim(2, 1, "", 1)))
        assertEquals(clusterIds[1], detector.getClusterId(factionClaim(5, 5, "", 2)))
        assertEquals(clusterIds[2], detector.getClusterId(factionClaim(10, 9, "", 3)))
        assertEquals(clusterIds[2], detector.getClusterId(factionClaim(10, 10, "", 3)))
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
        detector.insertFactionPosition(factionClaim(1, 1, "", 1), 1)
        detector.insertFactionPosition(factionClaim(2, 2, "", 2), 2)

        // Assert your expectations
        assertEquals(clusterIds[0], detector.getClusterId(factionClaim(1, 1, "", 1)))
        assertEquals(clusterIds[1], detector.getClusterId(factionClaim(2, 2, "", 2)))
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
        detector.insertFactionPosition(factionClaim(0, 0, "", 1), 1)
        detector.insertFactionPosition(factionClaim(1, 0, "", 2), 2)

        // Assert your expectations
        assertEquals(clusterIds[0], detector.getClusterId(factionClaim(0, 0, "", 1)))
        assertEquals(clusterIds[1], detector.getClusterId(factionClaim(1, 0, "", 2)))
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

        detector.deleteCluster(clusterIds[0])
        assertEquals(1, detector.clusters.size)

        detector.insertFactionPosition(factionClaim(4, 4, "", 1), 1)
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
        val cluster = detector.clusters.first()
        val outerNodes = transaction { cluster.getOuterNodes() }

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

        detector.insertFactionPosition(factionClaim(1, 1, "", 1), 1)
        val updatedOuterNodes = transaction { detector.clusters.first().getOuterNodes() }

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
        val cluster = detector.clusters.first()
        val outerNodes = transaction { cluster.getOuterNodes() }

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

    private fun factionClaim(x: Int, y: Int, world: String, id: Int): FactionClaim {
        return transaction {
            testFaction(id = id)
            getFactionClaim(x, y, world) ?: FactionClaim.new {
                chunkX = x
                chunkZ = y
                this.world = world
                factionId = id
            }
        }
    }

    private fun factionClaim(pair: Pair<ChunkPosition, Int>): FactionClaim {
        val (c, id) = pair
        return transaction { factionClaim(c.x, c.y, c.world, id) }
    }

    inner class DummyClaimQueryProvider(positions: List<Pair<ChunkPosition, Int>>) : ClaimQueryProvider {
        private val positionsOnly = positions.map { it.first }
        private val factionClaims = transaction {
            positions.map { (chunk, id) -> factionClaim(chunk.x, chunk.y, chunk.world, id) to id }
        }

        override fun queryNeighbours(claim: FactionClaim) = listOf(
            ChunkPosition(claim.chunkX + 1, claim.chunkZ, claim.world),
            ChunkPosition(claim.chunkX - 1, claim.chunkZ, claim.world),
            ChunkPosition(claim.chunkX, claim.chunkZ + 1, claim.world),
            ChunkPosition(claim.chunkX, claim.chunkZ - 1, claim.world)
        ).filter { it in positionsOnly }.mapNotNull { it.getFactionClaim() }

        override fun allFactionPositions() = factionClaims

        override fun allZonePositions(): List<Pair<FactionClaim, String>> = emptyList()
    }
}