package io.github.toberocat.improvedfactions.database.storage

import java.util.UUID
import java.util.concurrent.atomic.AtomicReference

class ClaimStateCache {
    private val state = AtomicReference<GameStateSnapshot?>()

    fun isReady(): Boolean = state.get() != null

    fun clear() {
        state.set(null)
    }

    fun snapshot(): GameStateSnapshot? = state.get()

    fun publish(snapshot: GameStateSnapshot) {
        state.set(snapshot.immutableCopy())
    }

    fun claim(key: ClaimKey): ClaimSnapshot? = state.get()?.claims?.get(key)

    fun claim(world: String, chunkX: Int, chunkZ: Int): ClaimSnapshot? =
        claim(ClaimKey(world, chunkX, chunkZ))

    fun faction(id: Int): FactionSnapshot? = state.get()?.factions?.get(id)

    fun factions(): List<FactionSnapshot> = state.get()?.factions.orEmpty().values.toList()

    fun user(uniqueId: UUID): UserSnapshot? = state.get()?.users?.get(uniqueId)

    fun userById(id: Int): UserSnapshot? = state.get()?.users.orEmpty().values.firstOrNull { it.id == id }

    fun rank(id: Int): RankSnapshot? = state.get()?.ranks?.get(id)

    fun ranks(factionId: Int): List<RankSnapshot> = state.get()?.ranks.orEmpty().values
        .filter { it.factionId == factionId }

    fun invites(uniqueId: UUID): List<InviteSnapshot> {
        val userId = user(uniqueId)?.id ?: return emptyList()
        val now = System.currentTimeMillis()
        return state.get()?.invites.orEmpty().values.filter {
            it.invitedUserId == userId && it.expiresAtEpochMillis > now
        }
    }

    fun bans(factionId: Int): List<BanSnapshot> = state.get()?.bans.orEmpty().values
        .filter { it.factionId == factionId }

    fun relations(factionId: Int, type: String): Set<Int> = state.get()?.relations.orEmpty()
        .asSequence()
        .filter { it.type == type && (it.sourceFactionId == factionId || it.targetFactionId == factionId) }
        .map { if (it.sourceFactionId == factionId) it.targetFactionId else it.sourceFactionId }
        .toSet()

    fun allyInvite(firstFactionId: Int, secondFactionId: Int): AllyInviteSnapshot? =
        state.get()?.allyInvites.orEmpty().values.firstOrNull {
            (it.sourceFactionId == firstFactionId && it.targetFactionId == secondFactionId) ||
                (it.sourceFactionId == secondFactionId && it.targetFactionId == firstFactionId)
        }?.takeIf { it.expiresAtEpochMillis > System.currentTimeMillis() }

    fun home(factionId: Int): HomeSnapshot? = state.get()?.homes?.get(factionId)

    fun usage(registry: String, playerId: UUID): Int? = state.get()?.usageLimits?.get(UsageLimitKey(registry, playerId))

    fun playerName(uniqueId: UUID): String? = state.get()?.playerNames?.get(uniqueId)

    fun factionMembers(factionId: Int): List<UUID> = state.get()?.users.orEmpty().values
        .asSequence()
        .filter { it.factionId == factionId }
        .map { it.uniqueId }
        .toList()

    fun claimsNear(world: String, centerX: Int, centerZ: Int, radius: Int): List<ClaimSnapshot> =
        state.get()?.claims.orEmpty().let { claims ->
            buildList {
                for (x in centerX - radius..centerX + radius) {
                    for (z in centerZ - radius..centerZ + radius) {
                        claims[ClaimKey(world, x, z)]?.let(::add)
                    }
                }
            }
        }
}
