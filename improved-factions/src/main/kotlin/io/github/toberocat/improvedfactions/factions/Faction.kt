package io.github.toberocat.improvedfactions.factions

import dev.s7a.base64.Base64ItemStack
import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.annotations.localization.Localization
import io.github.toberocat.improvedfactions.claims.*
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.exceptions.*
import io.github.toberocat.improvedfactions.factions.ban.FactionBan
import io.github.toberocat.improvedfactions.factions.ban.FactionBans
import io.github.toberocat.improvedfactions.invites.FactionInvite
import io.github.toberocat.improvedfactions.messages.MessageBroker
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.modules.chat.ChatModule.resetChatMode
import io.github.toberocat.improvedfactions.modules.dynmap.DynmapModule
import io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule.powerRaidModule
import io.github.toberocat.improvedfactions.modules.relations.RelationsModule
import io.github.toberocat.improvedfactions.ranks.FactionRank
import io.github.toberocat.improvedfactions.ranks.FactionRankHandler
import io.github.toberocat.improvedfactions.ranks.listRanks
import io.github.toberocat.improvedfactions.translation.LocalizationKey
import io.github.toberocat.improvedfactions.translation.LocalizedException
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.FactionUser
import io.github.toberocat.improvedfactions.user.FactionUsers
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.user.noFactionId
import io.github.toberocat.improvedfactions.utils.toOfflinePlayer
import io.github.toberocat.toberocore.command.exceptions.CommandException
import io.github.toberocat.toberocore.util.ItemUtils
import io.github.toberocat.toberocore.util.MathUtils
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import java.util.*
import kotlin.math.max

/**
 * Created: 04.08.2023
 * @author Tobias Madlberger (Tobias)
 */
class Faction(id: EntityID<Int>) : IntEntity(id) {
    companion object : EntityClass<Int, Faction>(Factions)

    var owner by Factions.owner
    var defaultRank by Factions.defaultRank
    internal var localName by Factions.name

    private var base64Icon by Factions.base64Icon
    private var localAccumulatedPower by Factions.accumulatedPower
    private var localMaxPower by Factions.maxPower
    private var localFactionJoinType by Factions.factionJoinType

    val maxPower
        get() = localMaxPower

    val accumulatedPower
        get() = localAccumulatedPower

    var name
        get() = localName
        set(value) {
            broadcast(
                "base.faction.renamed", mapOf(
                    "old" to localName, "new" to value
                )
            )
            localName = value
        }

    var factionJoinType
        get() = localFactionJoinType
        set(value) {
            broadcast(
                "base.faction.join-type-changed", mapOf(
                    "mode" to value.name.lowercase()
                )
            )
            localFactionJoinType = value
        }

    var icon: ItemStack
        get() {
            val item = Base64ItemStack.decode(base64Icon) ?: ItemStack(Material.GRASS_BLOCK)
            ItemUtils.editMeta(item) { it.setDisplayName("§e${localName}") }
            return item
        }
        set(value) {
            val base64 = Base64ItemStack.encode(value)
            if (base64.length > BaseModule.config.maxFactionIconLength) throw CommandException(
                "base.exceptions.icon.invalid-icon", emptyMap()
            )
            base64Icon = base64
        }

    override fun delete() {
        BaseModule.claimChunkClusters.removeFactionClusters(this)
        DynmapModule.dynmapModule().dynmapModuleHandle.removeHome(this)
        listRanks().forEach { it.delete() }
        claims().forEach {
            it.factionId = noFactionId
        }
        members().forEach { unsetUserData(it) }
        RelationsModule.deleteFactionRelations(id.value)

        super.delete()
    }

    fun generateColor() = FactionHandler.generateColor(id.value)


    fun setMaxPower(newMaxPower: Int) {
        val actualNewMaxPower = max(newMaxPower, 0)
        if (actualNewMaxPower == localMaxPower)
            return

        if (accumulatedPower > actualNewMaxPower)
            setAccumulatedPower(actualNewMaxPower, PowerAccumulationChangeReason.MAX_CHANGED)
        broadcast(
            "power.faction.max-power-changed",
            mapOf(
                "old" to localAccumulatedPower.toString(),
                "new" to actualNewMaxPower.toString()
            )
        )
        localMaxPower = actualNewMaxPower
    }

    fun setAccumulatedPower(newAccumulatedPower: Int, reason: PowerAccumulationChangeReason) {
        val actualNewAccumulatedPower = MathUtils.clamp(newAccumulatedPower, -localMaxPower, localMaxPower)
        if (actualNewAccumulatedPower == localAccumulatedPower)
            return

        broadcast(
            "power.faction.accumulated-power-changed.$reason",
            mapOf(
                "old" to localAccumulatedPower.toString(),
                "new" to actualNewAccumulatedPower.toString(),
                "max" to localMaxPower.toString()
            )
        )

        BaseModule.claimChunkClusters.markFactionClusterForUpdate(this)
        localAccumulatedPower = actualNewAccumulatedPower
    }

    fun countActiveMembers(ticksIntoPast: Long): Int {
        val minStamp = System.currentTimeMillis() - ticksIntoPast / 20 * 1000
        return FactionUser.find { FactionUsers.factionId eq id.value }
            .count { user -> user.offlinePlayer().let { it.isOnline || it.lastPlayed > minStamp } }
    }

    fun countInactiveMembers(millisecondsIntoPast: Long): Int {
        val minStamp = System.currentTimeMillis() - millisecondsIntoPast
        return FactionUser.find { FactionUsers.factionId eq id.value }
            .count { user -> user.offlinePlayer().let { !it.isOnline && it.lastPlayed <= minStamp } }
    }

    fun isInactive(toMillis: Long): Boolean {
        val minStamp = System.currentTimeMillis() - toMillis
        return FactionUser.find { FactionUsers.factionId eq id.value }
            .none { user -> user.offlinePlayer().let { it.isOnline || it.lastPlayed > minStamp } }
    }

    fun members(): SizedIterable<FactionUser> = FactionUser.find { FactionUsers.factionId eq id.value }

    fun claims(): SizedIterable<FactionClaim> = FactionClaim.find { FactionClaims.factionId eq id.value }

    @Throws(CommandException::class)
    fun join(uuid: UUID, rankId: Int) {
        val user = uuid.factionUser()
        if (isBanned(user))
            throw CommandException("base.exceptions.you-are-banned", emptyMap())

        user.factionId = id.value
        user.assignedRank = rankId

        val player = Bukkit.getPlayer(uuid) ?: return
        val rank = loggedTransaction { user.rank() }
        broadcast(
            "base.faction.player-joined", mapOf(
                "player" to player.displayName, "rank" to rank.name
            )
        )

        powerRaidModule().powerModuleHandle.memberJoin(this)
    }

    fun invite(inviter: UUID, invited: UUID, rankId: Int): FactionInvite {
        val inviterUserId = inviter.factionUser().id.value
        val invitedUserId = invited.factionUser().id.value
        if (inviterUserId == invitedUserId) throw CantInviteYourselfException()

        val factId = id.value
        val invite = FactionInvite.new {
            inviterId = inviterUserId
            invitedId = invitedUserId
            factionId = factId
            this.rankId = rankId
            expirationDate = Clock.System.now()
                .plus(5, DateTimeUnit.MINUTE)
                .toLocalDateTime(TimeZone.UTC)
        }
        Bukkit.getScheduler().runTaskLater(
            ImprovedFactionsPlugin.instance,
            Runnable { loggedTransaction { invite.delete() } },
            BaseModule.config.inviteExpiresInMinutes * 60 * 20L
        )

        broadcast(
            "base.faction.player-invited", mapOf(
                "inviter" to (Bukkit.getPlayer(inviter)?.displayName ?: "§cNot found"),
                "invited" to (Bukkit.getPlayer(invited)?.displayName ?: "§cNot found")
            )
        )

        return invite
    }

    fun claimSquare(
        centerChunk: Chunk,
        squareRadius: Int,
        handleError: (e: LocalizedException) -> Unit,
    ): ClaimStatistics =
        squareClaimAction(centerChunk, squareRadius, { claim(it, announce = false) }, handleError)

    fun claim(chunk: Chunk, announce: Boolean = true): FactionClaim {
        val claim = chunk.getFactionClaim()
        if (!canClaimInWorld(chunk.world.name) || claim?.canClaim() == false) throw CantClaimThisChunkException(chunk)
        powerRaidModule().powerModuleHandle.claimChunk(chunk, this)

        val factionId = id.value
        val factionClaim = claim ?: FactionClaim.new {
            chunkX = chunk.x
            chunkZ = chunk.z
            this.world = chunk.world.name
            this.factionId = factionId
        }

        factionClaim.factionId = factionId
        BaseModule.claimChunkClusters.insertFactionPosition(
            factionClaim,
            id.value
        )

        if (announce) {
            broadcast(
                "base.faction.chunk-claimed", mapOf(
                    "x" to chunk.x.toString(), "z" to chunk.z.toString(), "world" to chunk.world.name
                )
            )
        }
        return factionClaim
    }

    fun bans(): SizedIterable<FactionBan> = FactionBan.find { FactionBans.faction eq id }

    fun isBanned(user: FactionUser): Boolean =
        FactionBan.count(FactionBans.user eq user.id and (FactionBans.faction eq id)) != 0L

    fun unclaimSquare(chunk: Chunk, squareRadius: Int, handleError: (e: LocalizedException) -> Unit) =
        squareClaimAction(chunk, squareRadius, { unclaim(it, announce = false) }, handleError)

    @Throws(FactionDoesntHaveThisClaimException::class)
    fun unclaim(chunk: Chunk, announce: Boolean = true) {
        val claim = chunk.getFactionClaim()
        if (claim == null || claim.factionId != id.value) throw FactionDoesntHaveThisClaimException()
        claim.factionId = noFactionId
        BaseModule.claimChunkClusters.removePosition(claim)
        if (announce) {
            broadcast(
                "base.faction.chunk-unclaimed", mapOf(
                    "x" to chunk.x.toString(), "z" to chunk.z.toString(), "world" to chunk.world.name
                )
            )
        }
    }

    @Throws(PlayerIsOwnerLeaveException::class)
    fun kick(player: UUID) {
        if (owner == player) throw PlayerIsOwnerLeaveException()

        unsetUserData(player.factionUser())
        broadcast("base.faction.player-kicked", emptyMap())
    }

    @Throws(CommandException::class)
    fun ban(user: FactionUser) {
        if (owner == user.uniqueId) throw PlayerIsOwnerLeaveException()
        if (isBanned(user))
            throw CommandException("base.exceptions.already-banned", emptyMap())

        if (user.factionId == id.value) {
            unsetUserData(user)
        }

        val f = this
        FactionBan.new {
            faction = f
            this.user = user
        }

        broadcast("base.faction.player-banned", emptyMap())
    }

    @Throws(PlayerIsOwnerLeaveException::class)
    fun leave(player: UUID) {
        if (owner == player) throw PlayerIsOwnerLeaveException()

        unsetUserData(player.factionUser())
        broadcast(
            "base.faction.player-left", mapOf(
                "player" to (Bukkit.getOfflinePlayer(player).name ?: "unknown")
            )
        )
    }

    fun transferOwnership(newOwner: UUID) {
        val user = newOwner.factionUser()
        if (user.factionId != id.value)
            throw CommandException("base.exceptions.player-not-in-faction", emptyMap())
        if (owner == newOwner)
            throw CommandException("base.exceptions.player-already-owner", emptyMap())
        owner = user.uniqueId

        val previousOwner = owner.factionUser()
        user.assignedRank = previousOwner.assignedRank
        previousOwner.assignedRank = defaultRank

        owner.toOfflinePlayer().sendLocalized("base.faction.transferowner.owner-transfered")
        user.player()?.sendLocalized("base.faction.transferowner.youre-the-owner-now")
    }

    fun broadcast(key: LocalizationKey, placeholders: Map<String, String>) =
        MessageBroker.send(id.value, LocalizedMessage(key, placeholders))

    private fun unsetUserData(user: FactionUser) {
        user.factionId = noFactionId
        user.assignedRank = FactionRankHandler.guestRankId
        user.player()?.resetChatMode()
        powerRaidModule().powerModuleHandle.memberLeave(this)
    }

    @Localization("base.exceptions.rank-not-found")
    fun getDefaultRank() = FactionRank.findById(defaultRank) ?: throw LocalizedException(
        "base.exceptions.rank-not-found", emptyMap()
    )
}