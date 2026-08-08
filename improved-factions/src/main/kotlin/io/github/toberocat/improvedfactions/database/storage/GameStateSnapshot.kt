package io.github.toberocat.improvedfactions.database.storage

import java.util.UUID

data class ClaimKey(val world: String, val chunkX: Int, val chunkZ: Int)

data class ClaimSnapshot(
    val key: ClaimKey,
    val factionId: Int,
    val zoneType: String,
    val clusterId: UUID?,
    val isRaidable: Boolean,
    val id: Int = 0
)

data class FactionSnapshot(
    val id: Int,
    val name: String,
    val owner: UUID,
    val joinType: String,
    val accumulatedPower: Int,
    val maxPower: Int,
    val claimCount: Int,
    val defaultRankId: Int = 0,
    val iconBase64: String? = null
)

data class UserSnapshot(
    val uniqueId: UUID,
    val factionId: Int,
    val rankName: String,
    val id: Int = 0,
    val rankId: Int = 0,
    val rankPriority: Int = -1,
    val permissions: Set<String> = emptySet()
)

data class RankSnapshot(
    val id: Int,
    val factionId: Int,
    val name: String,
    val priority: Int,
    val permissions: Set<String>
)

data class InviteSnapshot(
    val id: Int,
    val inviterUserId: Int,
    val invitedUserId: Int,
    val factionId: Int,
    val rankId: Int,
    val expiresAtEpochMillis: Long
)

data class BanSnapshot(val id: Int, val factionId: Int, val userId: Int)

data class RelationSnapshot(val sourceFactionId: Int, val targetFactionId: Int, val type: String)

data class AllyInviteSnapshot(val id: Int, val sourceFactionId: Int, val targetFactionId: Int, val expiresAtEpochMillis: Long)

data class HomeSnapshot(val factionId: Int, val world: String, val x: Double, val y: Double, val z: Double)

data class UsageLimitKey(val registry: String, val playerId: UUID)

data class GameStateSnapshot(
    val claims: Map<ClaimKey, ClaimSnapshot>,
    val factions: Map<Int, FactionSnapshot>,
    val users: Map<UUID, UserSnapshot>,
    val playerNames: Map<UUID, String>,
    val ranks: Map<Int, RankSnapshot> = emptyMap(),
    val invites: Map<Int, InviteSnapshot> = emptyMap(),
    val bans: Map<Int, BanSnapshot> = emptyMap(),
    val relations: Set<RelationSnapshot> = emptySet(),
    val allyInvites: Map<Int, AllyInviteSnapshot> = emptyMap(),
    val homes: Map<Int, HomeSnapshot> = emptyMap(),
    val usageLimits: Map<UsageLimitKey, Int> = emptyMap()
) {
    init {
        require(claims.all { (key, claim) -> key == claim.key })
        require(factions.all { (id, faction) -> id == faction.id })
        require(users.all { (id, user) -> id == user.uniqueId })
    }

    fun immutableCopy() = copy(
        claims = claims.toMap(),
        factions = factions.toMap(),
        users = users.mapValues { (_, user) -> user.copy(permissions = user.permissions.toSet()) },
        playerNames = playerNames.toMap(),
        ranks = ranks.mapValues { (_, rank) -> rank.copy(permissions = rank.permissions.toSet()) },
        invites = invites.toMap(),
        bans = bans.toMap(),
        relations = relations.toSet(),
        allyInvites = allyInvites.toMap(),
        homes = homes.toMap(),
        usageLimits = usageLimits.toMap()
    )
}
