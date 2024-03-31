package io.github.toberocat.improvedfactions.claims

import io.github.toberocat.improvedfactions.claims.clustering.Position
import io.github.toberocat.improvedfactions.claims.overclaim.ClaimSiegeManager
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.modules.dynmap.DynmapModule
import io.github.toberocat.improvedfactions.user.noFactionId
import io.github.toberocat.improvedfactions.zone.Zone
import io.github.toberocat.improvedfactions.zone.ZoneHandler
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class FactionClaim(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FactionClaim>(FactionClaims)

    val siegeManager
        get() = ClaimSiegeManager.getManager(this)

    var world by FactionClaims.world
    var chunkX by FactionClaims.chunkX
    var chunkZ by FactionClaims.chunkZ
    var factionId by FactionClaims.factionId
    var zoneType by FactionClaims.zoneType

    fun faction(): Faction? = Faction.findById(factionId)

    fun zone(): Zone? = ZoneHandler.getZone(zoneType)

    fun canClaim(): Boolean {
        return when {
            FactionClaims.blockedWorlds.contains(world) -> false
            factionId != noFactionId -> false
            zone()?.allowClaiming == false -> false
            else -> true
        }
    }

    fun chunk() = Bukkit.getWorld(world)?.getChunkAt(chunkX, chunkZ)

}