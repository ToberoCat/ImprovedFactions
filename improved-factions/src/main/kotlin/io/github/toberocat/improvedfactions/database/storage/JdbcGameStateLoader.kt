package io.github.toberocat.improvedfactions.database.storage

import io.github.toberocat.improvedfactions.factions.FactionJoinType
import io.github.toberocat.improvedfactions.modules.relations.RelationType
import io.github.toberocat.improvedfactions.user.noFactionId
import java.nio.ByteBuffer
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID
import javax.sql.DataSource
import kotlin.math.sqrt

data class RaidSnapshotConfig(
    val allowOverclaim: Boolean,
    val claimPowerKeep: Double,
    val guestRankName: String
)

class JdbcGameStateLoader(
    private val dataSource: DataSource,
    private val config: RaidSnapshotConfig
) {
    fun load(): GameStateSnapshot = dataSource.connection.use { connection ->
        connection.autoCommit = false
        try {
            val factions = loadFactions(connection)
            val ranks = loadRanks(connection)
            val users = loadUsers(connection, ranks)
            val names = loadPlayerNames(connection)
            val rawClaims = loadClaims(connection)
            val claims = calculateRaidability(rawClaims, factions)
            val invites = loadInvites(connection)
            val bans = loadBans(connection)
            val relations = loadRelations(connection)
            val allyInvites = loadAllyInvites(connection)
            val homes = loadHomes(connection)
            val usageLimits = loadUsageLimits(connection)
            connection.commit()
            GameStateSnapshot(
                claims.associateBy { it.key }, factions, users, names, ranks, invites,
                bans, relations, allyInvites, homes, usageLimits
            )
        } catch (failure: Throwable) {
            runCatching { connection.rollback() }
            throw failure
        }
    }

    private fun loadFactions(connection: Connection): Map<Int, FactionSnapshot> {
        val claimCounts = connection.createStatement().use { statement ->
            statement.executeQuery(
                "SELECT faction_id, COUNT(*) AS claim_count FROM faction_claims " +
                    "WHERE faction_id <> $noFactionId GROUP BY faction_id"
            ).use { result ->
                buildMap {
                    while (result.next()) put(result.getInt("faction_id"), result.getInt("claim_count"))
                }
            }
        }
        return connection.createStatement().use { statement ->
            statement.executeQuery(
                "SELECT id, name, owner, join_type, accumulated_power, max_power, default_rank, icon_base64 " +
                    "FROM factions WHERE id <> $noFactionId"
            ).use { result ->
                buildMap {
                    while (result.next()) {
                        val id = result.getInt("id")
                        val joinType = FactionJoinType.entries
                            .getOrNull(result.getInt("join_type"))
                            ?.toString()
                            ?: FactionJoinType.INVITE_ONLY.toString()
                        put(
                            id,
                            FactionSnapshot(
                                id,
                                result.getString("name"),
                                result.uuid("owner"),
                                joinType,
                                result.getInt("accumulated_power"),
                                result.getInt("max_power"),
                                claimCounts[id] ?: 0,
                                result.getInt("default_rank"),
                                result.getString("icon_base64")
                            )
                        )
                    }
                }
            }
        }
    }

    private fun loadRanks(connection: Connection): Map<Int, RankSnapshot> {
        val permissions = connection.createStatement().use { statement ->
            statement.executeQuery("SELECT rank_id, permission, allowed FROM faction_permissions").use { result ->
                buildMap<Int, MutableSet<String>> {
                    while (result.next()) {
                        if (result.getBoolean("allowed")) {
                            getOrPut(result.getInt("rank_id"), ::mutableSetOf).add(result.getString("permission"))
                        }
                    }
                }
            }
        }
        return connection.createStatement().use { statement ->
            statement.executeQuery("SELECT id, faction_id, rank_name, priority FROM faction_ranks").use { result ->
                buildMap {
                    while (result.next()) {
                        val id = result.getInt("id")
                        put(id, RankSnapshot(id, result.getInt("faction_id"), result.getString("rank_name"),
                            result.getInt("priority"), permissions[id].orEmpty().toSet()))
                    }
                }
            }
        }
    }

    private fun loadUsers(connection: Connection, ranks: Map<Int, RankSnapshot>): Map<UUID, UserSnapshot> =
        connection.createStatement().use { statement ->
            statement.executeQuery(
                "SELECT u.id, u.uniqueId, u.faction_id, u.rank_id, r.rank_name, r.priority " +
                    "FROM faction_users u LEFT JOIN faction_ranks r ON r.id = u.rank_id"
            ).use { result ->
                buildMap {
                    while (result.next()) {
                        val uniqueId = result.uuid("uniqueId")
                        put(
                            uniqueId,
                            UserSnapshot(
                                uniqueId,
                                result.getInt("faction_id"),
                                result.getString("rank_name") ?: config.guestRankName,
                                result.getInt("id"),
                                result.getInt("rank_id"),
                                result.getInt("priority"),
                                ranks[result.getInt("rank_id")]?.permissions.orEmpty()
                            )
                        )
                    }
                }
            }
        }

    private fun loadPlayerNames(connection: Connection): Map<UUID, String> =
        connection.createStatement().use { statement ->
            statement.executeQuery("SELECT id, name FROM known_offline_players").use { result ->
                buildMap {
                    while (result.next()) put(result.uuid("id"), result.getString("name"))
                }
            }
        }

    private fun loadClaims(connection: Connection): List<ClaimSnapshot> =
        connection.createStatement().use { statement ->
            statement.executeQuery(
                "SELECT id, chunk_x, chunk_z, world, faction_id, zone_type, cluster_id FROM faction_claims"
            ).use { result ->
                buildList {
                    while (result.next()) {
                        val key = ClaimKey(
                            result.getString("world"),
                            result.getInt("chunk_x"),
                            result.getInt("chunk_z")
                        )
                        add(
                            ClaimSnapshot(
                                key,
                                result.getInt("faction_id"),
                                result.getString("zone_type"),
                                result.nullableUuid("cluster_id"),
                                isRaidable = false,
                                id = result.getInt("id")
                            )
                        )
                    }
                }
            }
        }

    private fun loadInvites(connection: Connection): Map<Int, InviteSnapshot> =
        connection.createStatement().use { statement ->
            statement.executeQuery(
                "SELECT id, inviter_id, invited_id, faction_id, rank_id, expiration_date FROM faction_invites"
            ).use { result ->
                buildMap {
                    while (result.next()) {
                        val id = result.getInt("id")
                        val expires = result.epochMillis("expiration_date")
                        put(id, InviteSnapshot(id, result.getInt("inviter_id"), result.getInt("invited_id"),
                            result.getInt("faction_id"), result.getInt("rank_id"), expires))
                    }
                }
            }
        }

    private fun loadBans(connection: Connection): Map<Int, BanSnapshot> =
        connection.createStatement().use { statement ->
            statement.executeQuery("SELECT id, faction, user FROM faction_bans").use { result ->
                buildMap {
                    while (result.next()) {
                        val id = result.getInt("id")
                        put(id, BanSnapshot(id, result.getInt("faction"), result.getInt("user")))
                    }
                }
            }
        }

    private fun loadRelations(connection: Connection): Set<RelationSnapshot> =
        connection.createStatement().use { statement ->
            statement.executeQuery("SELECT source_faction_id, target_faction_id, relation_type FROM faction_relations").use { result ->
                buildSet {
                    while (result.next()) add(
                        RelationSnapshot(
                            result.getInt("source_faction_id"),
                            result.getInt("target_faction_id"),
                            RelationType.entries.getOrNull(result.getInt("relation_type"))?.name ?: RelationType.ENEMY.name
                        )
                    )
                }
            }
        }

    private fun loadHomes(connection: Connection): Map<Int, HomeSnapshot> =
        connection.createStatement().use { statement ->
            statement.executeQuery("SELECT id, world, x, y, z FROM faction_homes").use { result ->
                buildMap {
                    while (result.next()) {
                        val id = result.getInt("id")
                        put(id, HomeSnapshot(id, result.getString("world"), result.getDouble("x"),
                            result.getDouble("y"), result.getDouble("z")))
                    }
                }
            }
        }

    private fun loadAllyInvites(connection: Connection): Map<Int, AllyInviteSnapshot> =
        connection.createStatement().use { statement ->
            statement.executeQuery(
                "SELECT id, source_faction_id, target_faction_id, expiration_date FROM faction_ally_invites"
            ).use { result ->
                buildMap {
                    while (result.next()) {
                        val id = result.getInt("id")
                        put(id, AllyInviteSnapshot(id, result.getInt("source_faction_id"),
                            result.getInt("target_faction_id"), result.epochMillis("expiration_date")))
                    }
                }
            }
        }

    private fun loadUsageLimits(connection: Connection): Map<UsageLimitKey, Int> =
        connection.createStatement().use { statement ->
            statement.executeQuery("SELECT registry, player_id, used FROM player_usage_limits").use { result ->
                buildMap {
                    while (result.next()) put(
                        UsageLimitKey(result.getString("registry"), result.uuid("player_id")),
                        result.getInt("used")
                    )
                }
            }
        }

    private fun calculateRaidability(
        claims: List<ClaimSnapshot>,
        factions: Map<Int, FactionSnapshot>
    ): List<ClaimSnapshot> {
        if (!config.allowOverclaim) return claims
        val raidable = mutableSetOf<ClaimKey>()
        connectedFactionClaims(claims).forEach { clusterClaims ->
                val faction = factions[clusterClaims.first().factionId] ?: return@forEach
                val totalClaims = faction.claimCount
                if (totalClaims <= 0) return@forEach
                val maintenanceCost = totalClaims * config.claimPowerKeep
                val clusterRatio = clusterClaims.size.toDouble() / totalClaims
                val clusterPowerCost = maintenanceCost * clusterRatio
                val centerX = clusterClaims.map { it.key.chunkX }.average()
                val centerZ = clusterClaims.map { it.key.chunkZ }.average()
                val distances = clusterClaims.map {
                    val deltaX = it.key.chunkX - centerX
                    val deltaZ = it.key.chunkZ - centerZ
                    deltaX * deltaX + deltaZ * deltaZ
                }
                val biggestDistance = distances.maxOrNull() ?: return@forEach
                if (biggestDistance == 0.0) return@forEach
                val distancePercentages = distances.map { it / biggestDistance }
                val distanceSum = distancePercentages.sum()
                if (distanceSum == 0.0) return@forEach
                val claimPowerCost = clusterPowerCost / distanceSum
                val availablePower = (faction.accumulatedPower - maintenanceCost)
                    .coerceIn(-faction.maxPower.toDouble(), faction.maxPower.toDouble())
                val threshold = sqrt((faction.maxPower + availablePower) * clusterRatio)
                distancePercentages.forEachIndexed { index, distance ->
                    if (distance * claimPowerCost >= threshold) raidable += clusterClaims[index].key
                }
            }
        return claims.map { claim -> claim.copy(isRaidable = claim.key in raidable) }
    }

    private fun connectedFactionClaims(claims: List<ClaimSnapshot>): List<List<ClaimSnapshot>> {
        val remaining = claims.filter { it.factionId != noFactionId }.associateBy { it.key }.toMutableMap()
        val components = mutableListOf<List<ClaimSnapshot>>()
        while (remaining.isNotEmpty()) {
            val first = remaining.entries.first()
            remaining.remove(first.key)
            val component = mutableListOf(first.value)
            val queue = ArrayDeque<ClaimSnapshot>().apply { add(first.value) }
            while (queue.isNotEmpty()) {
                val current = queue.removeFirst()
                listOf(
                    current.key.copy(chunkX = current.key.chunkX + 1),
                    current.key.copy(chunkX = current.key.chunkX - 1),
                    current.key.copy(chunkZ = current.key.chunkZ + 1),
                    current.key.copy(chunkZ = current.key.chunkZ - 1)
                ).forEach { neighbourKey ->
                    val neighbour = remaining[neighbourKey]
                    if (neighbour != null && neighbour.factionId == current.factionId) {
                        remaining.remove(neighbourKey)
                        component += neighbour
                        queue += neighbour
                    }
                }
            }
            components += component
        }
        return components
    }

    private fun ResultSet.uuid(column: String): UUID = requireNotNull(nullableUuid(column)) {
        "Expected UUID in column $column"
    }

    private fun ResultSet.nullableUuid(column: String): UUID? {
        val value = getObject(column) ?: return null
        return when (value) {
            is UUID -> value
            is ByteArray -> value.toUuid()
            is String -> UUID.fromString(value)
            else -> getBytes(column)?.toUuid()
                ?: throw IllegalArgumentException("Unsupported UUID value ${value.javaClass.name}")
        }
    }

    private fun ResultSet.epochMillis(column: String): Long {
        val value = getObject(column) ?: return 0L
        return when (value) {
            is Timestamp -> value.time
            is java.util.Date -> value.time
            is Instant -> value.toEpochMilli()
            is LocalDateTime -> value.toInstant(ZoneOffset.UTC).toEpochMilli()
            is Number -> value.toLong()
            is String -> parseTimestamp(value)
            else -> parseTimestamp(value.toString())
        }
    }

    private fun parseTimestamp(value: String): Long = value.toLongOrNull()
        ?: runCatching { Instant.parse(value).toEpochMilli() }
            .recoverCatching { LocalDateTime.parse(value).toInstant(ZoneOffset.UTC).toEpochMilli() }
            .recoverCatching { Timestamp.valueOf(value).time }
            .getOrElse { throw IllegalArgumentException("Unsupported timestamp value '$value'", it) }

    private fun ByteArray.toUuid(): UUID {
        require(size == 16) { "UUID binary value must contain 16 bytes" }
        val buffer = ByteBuffer.wrap(this)
        return UUID(buffer.long, buffer.long)
    }
}
