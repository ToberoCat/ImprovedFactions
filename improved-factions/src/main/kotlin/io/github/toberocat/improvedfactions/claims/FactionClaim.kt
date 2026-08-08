package io.github.toberocat.improvedfactions.claims

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.clustering.cluster.Cluster
import io.github.toberocat.improvedfactions.claims.clustering.position.ChunkPosition
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.database.storage.ClaimKey
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.user.noFactionId
import io.github.toberocat.improvedfactions.zone.Zone
import io.github.toberocat.improvedfactions.zone.ZoneHandler
import io.github.toberocat.improvedfactions.database.DatabaseManager.refreshStorageCacheAfterCommit
import org.bukkit.Bukkit
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

internal class FactionClaim(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FactionClaim>(FactionClaims)

    var world by FactionClaims.world
    var chunkX by FactionClaims.chunkX
    var chunkZ by FactionClaims.chunkZ
    private var storedFactionId by FactionClaims.factionId
    var factionId
        get() = storedFactionId
        set(value) {
            storedFactionId = value
            refreshStorageCacheAfterCommit()
        }
    private var storedZoneType by FactionClaims.zoneType
    var zoneType
        get() = storedZoneType
        set(value) {
            storedZoneType = value
            refreshStorageCacheAfterCommit()
        }
    private var storedClaimCluster by Cluster optionalReferencedOn FactionClaims.clusterId
    var claimCluster
        get() = storedClaimCluster
        set(value) {
            storedClaimCluster = value
            refreshStorageCacheAfterCommit()
        }

    fun faction(): Faction? = Faction.findById(factionId)

    fun zone(): Zone? = ZoneHandler.getZone(zoneType)

    fun canClaim(): Boolean {
        return when {
            !canClaimInWorld(world) -> false
            factionId != noFactionId -> false
            zone()?.allowClaiming == false -> false
            else -> true
        }
    }

    fun isClaimed() = factionId != noFactionId

    fun chunk() = Bukkit.getWorld(world)?.getChunkAt(chunkX, chunkZ)
    fun toPosition() = ChunkPosition(chunkX, chunkZ, world)

    fun isRaidable() = StorageManager.cache.claim(ClaimKey(world, chunkX, chunkZ))?.isRaidable == true
    override fun toString(): String {
        return "FactionClaim(world='$world', chunkX=$chunkX, chunkZ=$chunkZ, factionId=$factionId, zoneType='$zoneType', claimCluster=${claimCluster?.id?.value})"
    }

}
