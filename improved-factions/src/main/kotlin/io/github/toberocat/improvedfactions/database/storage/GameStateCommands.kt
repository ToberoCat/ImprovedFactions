package io.github.toberocat.improvedfactions.database.storage

import io.github.toberocat.improvedfactions.factions.FactionJoinType
import io.github.toberocat.improvedfactions.user.noFactionId
import java.nio.ByteBuffer
import java.sql.Connection
import java.sql.Timestamp
import java.time.Instant
import java.util.UUID
import java.util.concurrent.CompletionStage

/** Runtime mutations expressed only in immutable scalar input and executed by [StorageManager]. */
object GameStateCommands {
    data class DefaultRankSpec(val name: String, val priority: Int, val allowedPermissions: Set<String>)

    fun createFaction(
        owner: UUID,
        name: String,
        maximumPower: Int,
        ranks: List<DefaultRankSpec>,
        knownPermissions: Set<String>
    ): CompletionStage<Int> {
        val copiedRanks = ranks.map { it.copy(allowedPermissions = it.allowedPermissions.toSet()) }
        val copiedKnownPermissions = knownPermissions.toSet()
        return StorageManager.write { connection ->
            check(!factionNameExists(connection, name)) { "Faction $name already exists" }
            val factionId = connection.prepareStatement(
                "INSERT INTO factions (name, owner, accumulated_power, max_power, default_rank, join_type) VALUES (?, ?, ?, ?, 0, ?)",
                java.sql.Statement.RETURN_GENERATED_KEYS
            ).use {
                it.setString(1, name); it.setBytes(2, owner.bytes()); it.setInt(3, maximumPower)
                it.setInt(4, maximumPower); it.setInt(5, FactionJoinType.INVITE_ONLY.ordinal); it.executeUpdate()
                it.generatedKeys.use { keys -> check(keys.next()); keys.getInt(1) }
            }
            val rankIds = copiedRanks.map { spec ->
                insertRank(connection, factionId, spec, copiedKnownPermissions)
            }
            val ownerRankId = rankIds.lastOrNull() ?: 0
            val defaultRankId = rankIds.firstOrNull() ?: 0
            connection.prepareStatement("UPDATE factions SET default_rank = ? WHERE id = ?").use {
                it.setInt(1, defaultRankId); it.setInt(2, factionId); it.executeUpdate()
            }
            val userId = ensureUser(connection, owner)
            connection.prepareStatement("UPDATE faction_users SET faction_id = ?, rank_id = ? WHERE id = ?").use {
                it.setInt(1, factionId); it.setInt(2, ownerRankId); it.setInt(3, userId); it.executeUpdate()
            }
            factionId
        }
    }

    fun renameFaction(factionId: Int, name: String) = updateFaction(factionId, "name", name)

    fun setJoinType(factionId: Int, joinType: FactionJoinType) =
        updateFaction(factionId, "join_type", joinType.ordinal)

    fun setIcon(factionId: Int, base64: String?) = updateFaction(factionId, "icon_base64", base64)

    fun setPower(factionId: Int, accumulated: Int? = null, maximum: Int? = null): CompletionStage<Unit> =
        StorageManager.write { connection ->
            when {
                accumulated != null -> connection.prepareStatement(
                    "UPDATE factions SET accumulated_power = CASE " +
                        "WHEN ? > max_power THEN max_power WHEN ? < -max_power THEN -max_power ELSE ? END WHERE id = ?"
                ).use {
                    it.setInt(1, accumulated); it.setInt(2, accumulated); it.setInt(3, accumulated)
                    it.setInt(4, factionId); it.executeUpdate()
                }
                maximum != null -> connection.prepareStatement(
                    "UPDATE factions SET max_power = ?, accumulated_power = CASE " +
                        "WHEN accumulated_power > ? THEN ? WHEN accumulated_power < ? THEN ? " +
                        "ELSE accumulated_power END WHERE id = ?"
                ).use {
                    val boundedMaximum = maximum.coerceAtLeast(0)
                    it.setInt(1, boundedMaximum); it.setInt(2, boundedMaximum); it.setInt(3, boundedMaximum)
                    it.setInt(4, -boundedMaximum); it.setInt(5, -boundedMaximum); it.setInt(6, factionId)
                    it.executeUpdate()
                }
            }
        }

    fun setAccumulatedPower(values: Map<Int, Int>): CompletionStage<Unit> {
        val copiedValues = values.toMap()
        return StorageManager.write { connection ->
            connection.prepareStatement("UPDATE factions SET accumulated_power = ? WHERE id = ?").use { statement ->
                copiedValues.forEach { (factionId, power) ->
                    statement.setInt(1, power); statement.setInt(2, factionId); statement.addBatch()
                }
                statement.executeBatch()
            }
        }
    }

    fun setOwner(factionId: Int, owner: UUID): CompletionStage<Unit> = StorageManager.write { connection ->
        connection.prepareStatement("UPDATE factions SET owner = ? WHERE id = ?").use {
            it.setBytes(1, owner.bytes()); it.setInt(2, factionId); it.executeUpdate()
        }
    }

    fun transferOwnership(
        factionId: Int,
        previousOwner: UUID,
        newOwner: UUID,
        removePreviousOwner: Boolean = false
    ): CompletionStage<Unit> = StorageManager.write { connection ->
        val ownerRank = connection.prepareStatement(
            "SELECT id FROM faction_ranks WHERE faction_id = ? ORDER BY priority DESC LIMIT 1"
        ).use {
            it.setInt(1, factionId)
            it.executeQuery().use { result -> check(result.next()) { "Faction has no owner rank" }; result.getInt(1) }
        }
        val defaultRank = connection.prepareStatement("SELECT default_rank FROM factions WHERE id = ?").use {
            it.setInt(1, factionId)
            it.executeQuery().use { result -> check(result.next()) { "Faction no longer exists" }; result.getInt(1) }
        }
        val newOwnerId = ensureUser(connection, newOwner, factionId, ownerRank)
        val previousOwnerId = ensureUser(connection, previousOwner, factionId, defaultRank)
        connection.prepareStatement("UPDATE factions SET owner = ? WHERE id = ?").use {
            it.setBytes(1, newOwner.bytes()); it.setInt(2, factionId); check(it.executeUpdate() == 1)
        }
        connection.prepareStatement("UPDATE faction_users SET faction_id = ?, rank_id = ? WHERE id = ?").use {
            it.setInt(1, factionId); it.setInt(2, ownerRank); it.setInt(3, newOwnerId); it.executeUpdate()
            it.setInt(1, if (removePreviousOwner) noFactionId else factionId)
            it.setInt(2, if (removePreviousOwner) 0 else defaultRank)
            it.setInt(3, previousOwnerId); it.executeUpdate()
        }
    }

    fun deleteFaction(factionId: Int): CompletionStage<Unit> = StorageManager.write { connection ->
        listOf("faction_ally_invites" to "source_faction_id", "faction_ally_invites" to "target_faction_id",
            "faction_relations" to "source_faction_id", "faction_relations" to "target_faction_id",
            "faction_bans" to "faction").forEach { (table, column) ->
            connection.prepareStatement("DELETE FROM $table WHERE $column = ?").use {
                it.setInt(1, factionId); it.executeUpdate()
            }
        }
        connection.prepareStatement(
            "DELETE FROM faction_permissions WHERE rank_id IN (SELECT id FROM faction_ranks WHERE faction_id = ?)"
        ).use { it.setInt(1, factionId); it.executeUpdate() }
        connection.prepareStatement("DELETE FROM faction_ranks WHERE faction_id = ?").use {
            it.setInt(1, factionId); it.executeUpdate()
        }
        connection.prepareStatement("DELETE FROM faction_homes WHERE id = ?").use {
            it.setInt(1, factionId); it.executeUpdate()
        }
        connection.prepareStatement("DELETE FROM faction_clusters WHERE faction = ?").use {
            it.setInt(1, factionId); it.executeUpdate()
        }
        connection.prepareStatement("UPDATE faction_claims SET faction_id = ?, cluster_id = NULL WHERE faction_id = ?").use {
            it.setInt(1, noFactionId); it.setInt(2, factionId); it.executeUpdate()
        }
        connection.prepareStatement("UPDATE faction_users SET faction_id = ?, rank_id = 0 WHERE faction_id = ?").use {
            it.setInt(1, noFactionId); it.setInt(2, factionId); it.executeUpdate()
        }
        connection.prepareStatement("DELETE FROM factions WHERE id = ?").use {
            it.setInt(1, factionId); it.executeUpdate()
        }
    }

    fun setUserFaction(uniqueId: UUID, factionId: Int, rankId: Int): CompletionStage<Unit> =
        StorageManager.write { connection ->
            val userId = ensureUser(connection, uniqueId, factionId, rankId)
            connection.prepareStatement("UPDATE faction_users SET faction_id = ?, rank_id = ? WHERE id = ?").use {
                it.setInt(1, factionId); it.setInt(2, rankId); it.setInt(3, userId); it.executeUpdate()
            }
        }

    fun claim(key: ClaimKey, factionId: Int, zoneType: String = "default"): CompletionStage<Unit> =
        StorageManager.write { connection ->
            val updated = connection.prepareStatement(
                "UPDATE faction_claims SET faction_id = ? WHERE world = ? AND chunk_x = ? AND chunk_z = ?"
            ).use {
                it.setInt(1, factionId); it.setString(2, key.world); it.setInt(3, key.chunkX); it.setInt(4, key.chunkZ)
                it.executeUpdate()
            }
            if (updated == 0) connection.prepareStatement(
                "INSERT INTO faction_claims (chunk_x, chunk_z, faction_id, world, zone_type) VALUES (?, ?, ?, ?, ?)"
            ).use {
                it.setInt(1, key.chunkX); it.setInt(2, key.chunkZ); it.setInt(3, factionId)
                it.setString(4, key.world); it.setString(5, zoneType); it.executeUpdate()
            }
        }

    fun unclaim(key: ClaimKey): CompletionStage<Unit> = StorageManager.write { connection ->
        connection.prepareStatement(
            "UPDATE faction_claims SET faction_id = ?, cluster_id = NULL WHERE world = ? AND chunk_x = ? AND chunk_z = ?"
        ).use {
            it.setInt(1, noFactionId); it.setString(2, key.world); it.setInt(3, key.chunkX); it.setInt(4, key.chunkZ)
            it.executeUpdate()
        }
    }

    fun claimAll(keys: List<ClaimKey>, factionId: Int, accumulatedPower: Int): CompletionStage<Int> {
        val copiedKeys = keys.toList()
        return StorageManager.write { connection ->
            copiedKeys.forEach { key ->
                val existing = findClaimFaction(connection, key)
                check(existing == null || existing == noFactionId) { "Chunk is already claimed" }
                if (existing == null) connection.prepareStatement(
                    "INSERT INTO faction_claims (chunk_x, chunk_z, faction_id, world, zone_type) VALUES (?, ?, ?, ?, 'default')"
                ).use {
                    it.setInt(1, key.chunkX); it.setInt(2, key.chunkZ); it.setInt(3, factionId); it.setString(4, key.world)
                    it.executeUpdate()
                } else connection.prepareStatement(
                    "UPDATE faction_claims SET faction_id = ? WHERE world = ? AND chunk_x = ? AND chunk_z = ?"
                ).use {
                    it.setInt(1, factionId); it.setString(2, key.world); it.setInt(3, key.chunkX); it.setInt(4, key.chunkZ)
                    it.executeUpdate()
                }
            }
            connection.prepareStatement("UPDATE factions SET accumulated_power = ? WHERE id = ?").use {
                it.setInt(1, accumulatedPower); it.setInt(2, factionId); it.executeUpdate()
            }
            copiedKeys.size
        }
    }

    fun unclaimAll(keys: List<ClaimKey>, factionId: Int): CompletionStage<Int> {
        val copiedKeys = keys.toList()
        return StorageManager.write { connection ->
            var updated = 0
            copiedKeys.forEach { key ->
                connection.prepareStatement(
                    "UPDATE faction_claims SET faction_id = ?, cluster_id = NULL WHERE faction_id = ? AND world = ? AND chunk_x = ? AND chunk_z = ?"
                ).use {
                    it.setInt(1, noFactionId); it.setInt(2, factionId); it.setString(3, key.world)
                    it.setInt(4, key.chunkX); it.setInt(5, key.chunkZ); updated += it.executeUpdate()
                }
            }
            updated
        }
    }

    fun setZone(keys: List<ClaimKey>, zoneType: String): CompletionStage<Int> {
        val copiedKeys = keys.toList()
        return StorageManager.write { connection ->
            var changed = 0
            copiedKeys.forEach { key ->
                val existing = findClaimFaction(connection, key)
                if (existing == null) connection.prepareStatement(
                    "INSERT INTO faction_claims (chunk_x, chunk_z, faction_id, world, zone_type) VALUES (?, ?, ?, ?, ?)"
                ).use {
                    it.setInt(1, key.chunkX); it.setInt(2, key.chunkZ); it.setInt(3, noFactionId)
                    it.setString(4, key.world); it.setString(5, zoneType); changed += it.executeUpdate()
                } else connection.prepareStatement(
                    "UPDATE faction_claims SET zone_type = ?, cluster_id = NULL WHERE world = ? AND chunk_x = ? AND chunk_z = ?"
                ).use {
                    it.setString(1, zoneType); it.setString(2, key.world)
                    it.setInt(3, key.chunkX); it.setInt(4, key.chunkZ); changed += it.executeUpdate()
                }
            }
            changed
        }
    }

    fun setHome(factionId: Int, home: HomeSnapshot): CompletionStage<Unit> = StorageManager.write { connection ->
        val updated = connection.prepareStatement("UPDATE faction_homes SET x = ?, y = ?, z = ?, world = ? WHERE id = ?").use {
            it.setDouble(1, home.x); it.setDouble(2, home.y); it.setDouble(3, home.z); it.setString(4, home.world)
            it.setInt(5, factionId); it.executeUpdate()
        }
        if (updated == 0) connection.prepareStatement(
            "INSERT INTO faction_homes (id, x, y, z, world) VALUES (?, ?, ?, ?, ?)"
        ).use {
            it.setInt(1, factionId); it.setDouble(2, home.x); it.setDouble(3, home.y); it.setDouble(4, home.z)
            it.setString(5, home.world); it.executeUpdate()
        }
    }

    fun deleteInvite(inviteId: Int): CompletionStage<Unit> = deleteById("faction_invites", inviteId)

    fun acceptInvite(inviteId: Int, playerId: UUID, factionId: Int, rankId: Int): CompletionStage<Unit> =
        StorageManager.write { connection ->
            val userId = ensureUser(connection, playerId)
            connection.prepareStatement("DELETE FROM faction_invites WHERE id = ? AND invited_id = ?").use {
                it.setInt(1, inviteId); it.setInt(2, userId)
                check(it.executeUpdate() == 1) { "Faction invite is no longer available" }
            }
            connection.prepareStatement("UPDATE faction_users SET faction_id = ?, rank_id = ? WHERE id = ?").use {
                it.setInt(1, factionId); it.setInt(2, rankId); it.setInt(3, userId); it.executeUpdate()
            }
        }

    fun createInvite(inviter: UUID, invited: UUID, factionId: Int, rankId: Int, expiresAt: Instant): CompletionStage<Unit> =
        StorageManager.write { connection ->
            val inviterId = ensureUser(connection, inviter)
            val invitedId = ensureUser(connection, invited)
            connection.prepareStatement(
                "INSERT INTO faction_invites (inviter_id, invited_id, faction_id, rank_id, expiration_date) VALUES (?, ?, ?, ?, ?)"
            ).use {
                it.setInt(1, inviterId); it.setInt(2, invitedId); it.setInt(3, factionId); it.setInt(4, rankId)
                it.setTimestamp(5, Timestamp.from(expiresAt)); it.executeUpdate()
            }
        }

    fun createBan(factionId: Int, userId: Int): CompletionStage<Unit> = StorageManager.write { connection ->
        connection.prepareStatement("INSERT INTO faction_bans (faction, user) VALUES (?, ?)").use {
            it.setInt(1, factionId); it.setInt(2, userId); it.executeUpdate()
        }
    }

    fun banUser(factionId: Int, uniqueId: UUID): CompletionStage<Unit> = StorageManager.write { connection ->
        val userId = ensureUser(connection, uniqueId)
        connection.prepareStatement("INSERT INTO faction_bans (faction, user) VALUES (?, ?)").use {
            it.setInt(1, factionId); it.setInt(2, userId); it.executeUpdate()
        }
        connection.prepareStatement("UPDATE faction_users SET faction_id = ?, rank_id = 0 WHERE id = ?").use {
            it.setInt(1, noFactionId); it.setInt(2, userId); it.executeUpdate()
        }
    }

    fun deleteBan(banId: Int): CompletionStage<Unit> = deleteById("faction_bans", banId)

    fun createRelation(sourceId: Int, targetId: Int, typeOrdinal: Int): CompletionStage<Unit> =
        StorageManager.write { connection ->
            connection.prepareStatement(
                "INSERT INTO faction_relations (source_faction_id, target_faction_id, relation_type) VALUES (?, ?, ?)"
            ).use { it.setInt(1, sourceId); it.setInt(2, targetId); it.setInt(3, typeOrdinal); it.executeUpdate() }
        }

    fun deleteRelation(sourceId: Int, targetId: Int, typeOrdinal: Int): CompletionStage<Unit> =
        StorageManager.write { connection ->
            connection.prepareStatement(
                "DELETE FROM faction_relations WHERE relation_type = ? AND ((source_faction_id = ? AND target_faction_id = ?) OR (source_faction_id = ? AND target_faction_id = ?))"
            ).use {
                it.setInt(1, typeOrdinal); it.setInt(2, sourceId); it.setInt(3, targetId)
                it.setInt(4, targetId); it.setInt(5, sourceId); it.executeUpdate()
            }
        }

    fun deleteFactionRelations(factionId: Int): CompletionStage<Unit> = StorageManager.write { connection ->
        listOf("faction_ally_invites", "faction_relations").forEach { table ->
            connection.prepareStatement(
                "DELETE FROM $table WHERE source_faction_id = ? OR target_faction_id = ?"
            ).use { it.setInt(1, factionId); it.setInt(2, factionId); it.executeUpdate() }
        }
    }

    fun createAllyInvite(sourceId: Int, targetId: Int, expiresAt: Instant): CompletionStage<Unit> =
        StorageManager.write { connection ->
            connection.prepareStatement(
                "INSERT INTO faction_ally_invites (source_faction_id, target_faction_id, expiration_date) VALUES (?, ?, ?)"
            ).use {
                it.setInt(1, sourceId); it.setInt(2, targetId); it.setTimestamp(3, Timestamp.from(expiresAt)); it.executeUpdate()
            }
        }

    fun acceptAllyInvite(sourceId: Int, targetId: Int): CompletionStage<Unit> = StorageManager.write { connection ->
        connection.prepareStatement(
            "DELETE FROM faction_ally_invites WHERE (source_faction_id = ? AND target_faction_id = ?) OR (source_faction_id = ? AND target_faction_id = ?)"
        ).use { it.setInt(1, sourceId); it.setInt(2, targetId); it.setInt(3, targetId); it.setInt(4, sourceId); it.executeUpdate() }
        connection.prepareStatement(
            "INSERT INTO faction_relations (source_faction_id, target_faction_id, relation_type) VALUES (?, ?, 0)"
        ).use { it.setInt(1, sourceId); it.setInt(2, targetId); it.executeUpdate() }
    }

    fun updateUsage(registry: String, playerId: UUID, used: Int): CompletionStage<Unit> = StorageManager.write { connection ->
        val updated = connection.prepareStatement(
            "UPDATE player_usage_limits SET used = ? WHERE registry = ? AND player_id = ?"
        ).use { it.setInt(1, used); it.setString(2, registry); it.setBytes(3, playerId.bytes()); it.executeUpdate() }
        if (updated == 0) connection.prepareStatement(
            "INSERT INTO player_usage_limits (registry, player_id, used) VALUES (?, ?, ?)"
        ).use { it.setString(1, registry); it.setBytes(2, playerId.bytes()); it.setInt(3, used); it.executeUpdate() }
    }

    fun createRank(
        factionId: Int,
        name: String,
        priority: Int,
        knownPermissions: Set<String>,
        allowedPermissions: Set<String> = emptySet()
    ): CompletionStage<Unit> {
        val copiedKnownPermissions = knownPermissions.toSet()
        val copiedAllowedPermissions = allowedPermissions.toSet()
        return StorageManager.write { connection ->
            val rankId = connection.prepareStatement(
                "INSERT INTO faction_ranks (faction_id, rank_name, priority) VALUES (?, ?, ?)",
                java.sql.Statement.RETURN_GENERATED_KEYS
            ).use {
                it.setInt(1, factionId); it.setString(2, name); it.setInt(3, priority); it.executeUpdate()
                it.generatedKeys.use { keys -> check(keys.next()); keys.getInt(1) }
            }
            connection.prepareStatement(
                "INSERT INTO faction_permissions (rank_id, permission, allowed) VALUES (?, ?, ?)"
            ).use { statement ->
                copiedKnownPermissions.forEach { permission ->
                    statement.setInt(1, rankId); statement.setString(2, permission)
                    statement.setBoolean(3, permission in copiedAllowedPermissions); statement.addBatch()
                }
                statement.executeBatch()
            }
        }
    }

    fun assignRank(uniqueId: UUID, rankId: Int): CompletionStage<Unit> = StorageManager.write { connection ->
        val userId = ensureUser(connection, uniqueId)
        connection.prepareStatement("UPDATE faction_users SET rank_id = ? WHERE id = ?").use {
            it.setInt(1, rankId); it.setInt(2, userId); it.executeUpdate()
        }
    }

    fun setDefaultRank(factionId: Int, rankId: Int): CompletionStage<Unit> =
        updateFaction(factionId, "default_rank", rankId)

    fun deleteRank(rankId: Int, fallbackRankId: Int): CompletionStage<Unit> = StorageManager.write { connection ->
        connection.prepareStatement("UPDATE faction_users SET rank_id = ? WHERE rank_id = ?").use {
            it.setInt(1, fallbackRankId); it.setInt(2, rankId); it.executeUpdate()
        }
        connection.prepareStatement("DELETE FROM faction_permissions WHERE rank_id = ?").use {
            it.setInt(1, rankId); it.executeUpdate()
        }
        connection.prepareStatement("DELETE FROM faction_ranks WHERE id = ?").use {
            it.setInt(1, rankId); it.executeUpdate()
        }
    }

    fun setPermission(rankId: Int, permission: String, allowed: Boolean): CompletionStage<Unit> =
        StorageManager.write { connection ->
            val updated = connection.prepareStatement(
                "UPDATE faction_permissions SET allowed = ? WHERE rank_id = ? AND permission = ?"
            ).use { it.setBoolean(1, allowed); it.setInt(2, rankId); it.setString(3, permission); it.executeUpdate() }
            if (updated == 0) connection.prepareStatement(
                "INSERT INTO faction_permissions (rank_id, permission, allowed) VALUES (?, ?, ?)"
            ).use { it.setInt(1, rankId); it.setString(2, permission); it.setBoolean(3, allowed); it.executeUpdate() }
        }

    private fun updateFaction(factionId: Int, column: String, value: Any?): CompletionStage<Unit> =
        StorageManager.write { connection ->
            connection.prepareStatement("UPDATE factions SET $column = ? WHERE id = ?").use {
                it.setObject(1, value); it.setInt(2, factionId); it.executeUpdate()
            }
        }

    private fun deleteById(table: String, id: Int): CompletionStage<Unit> = StorageManager.write { connection ->
        connection.prepareStatement("DELETE FROM $table WHERE id = ?").use {
            it.setInt(1, id); it.executeUpdate()
        }
    }

    private fun ensureUser(
        connection: Connection,
        uniqueId: UUID,
        initialFactionId: Int = noFactionId,
        initialRankId: Int = 0
    ): Int {
        connection.prepareStatement("SELECT id FROM faction_users WHERE uniqueId = ?").use {
            it.setBytes(1, uniqueId.bytes())
            it.executeQuery().use { result -> if (result.next()) return result.getInt(1) }
        }
        connection.prepareStatement(
            "INSERT INTO faction_users (uniqueId, faction_id, rank_id) VALUES (?, ?, ?)",
            java.sql.Statement.RETURN_GENERATED_KEYS
        ).use {
            it.setBytes(1, uniqueId.bytes()); it.setInt(2, initialFactionId); it.setInt(3, initialRankId)
            it.executeUpdate()
            it.generatedKeys.use { keys -> if (keys.next()) return keys.getInt(1) }
        }
        error("Unable to create faction user $uniqueId")
    }

    private fun insertRank(
        connection: Connection,
        factionId: Int,
        spec: DefaultRankSpec,
        knownPermissions: Set<String>
    ): Int {
        val rankId = connection.prepareStatement(
            "INSERT INTO faction_ranks (faction_id, rank_name, priority) VALUES (?, ?, ?)",
            java.sql.Statement.RETURN_GENERATED_KEYS
        ).use {
            it.setInt(1, factionId); it.setString(2, spec.name); it.setInt(3, spec.priority); it.executeUpdate()
            it.generatedKeys.use { keys -> check(keys.next()); keys.getInt(1) }
        }
        connection.prepareStatement(
            "INSERT INTO faction_permissions (rank_id, permission, allowed) VALUES (?, ?, ?)"
        ).use { statement ->
            knownPermissions.forEach { permission ->
                statement.setInt(1, rankId); statement.setString(2, permission)
                statement.setBoolean(3, permission in spec.allowedPermissions); statement.addBatch()
            }
            statement.executeBatch()
        }
        return rankId
    }

    private fun factionNameExists(connection: Connection, name: String): Boolean =
        connection.prepareStatement("SELECT 1 FROM factions WHERE LOWER(name) = LOWER(?)").use {
            it.setString(1, name); it.executeQuery().use { result -> result.next() }
        }

    private fun findClaimFaction(connection: Connection, key: ClaimKey): Int? =
        connection.prepareStatement(
            "SELECT faction_id FROM faction_claims WHERE world = ? AND chunk_x = ? AND chunk_z = ?"
        ).use {
            it.setString(1, key.world); it.setInt(2, key.chunkX); it.setInt(3, key.chunkZ)
            it.executeQuery().use { result -> if (result.next()) result.getInt(1) else null }
        }

    private fun UUID.bytes(): ByteArray = ByteBuffer.allocate(16)
        .putLong(mostSignificantBits).putLong(leastSignificantBits).array()
}
