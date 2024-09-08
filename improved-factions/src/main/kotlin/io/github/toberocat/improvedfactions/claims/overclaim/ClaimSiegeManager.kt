package io.github.toberocat.improvedfactions.claims.overclaim

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.FactionClaim
import io.github.toberocat.improvedfactions.claims.clustering.position.ChunkPosition
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule.Companion.powerRaidModule
import io.github.toberocat.improvedfactions.translation.getLocaleEnum
import io.github.toberocat.improvedfactions.translation.localize
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.toAudience
import io.github.toberocat.toberocore.util.MathUtils
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import java.util.*

class ClaimSiegeManager(private val claim: FactionClaim) {
    private val config = powerRaidModule().config
    private val translatedBossBars = mutableMapOf<Locale, BossBar>()
    private val players = mutableSetOf<UUID>()
    private var task: BukkitTask? = null
    private var siegeProgressSpeed: Double = 0.0
    private var claimIntegrity: Double = 100.0

    companion object {
        private val siegeManagers = mutableMapOf<ChunkPosition, ClaimSiegeManager>()

        fun getManager(claim: FactionClaim): ClaimSiegeManager {
            return siegeManagers.computeIfAbsent(ChunkPosition(claim.chunkX, claim.chunkZ, claim.world)) {
                ClaimSiegeManager(
                    claim
                )
            }
        }

        fun remove(claim: FactionClaim) = siegeManagers.remove(ChunkPosition(claim.chunkX, claim.chunkZ, claim.world))
    }

    fun enterClaimCombat(player: Player) {
        if (player.uniqueId in players)
            return
        if (players.isEmpty() && player.factionUser().factionId == claim.factionId)
            return
        if (players.isEmpty()) {
            player.sendLocalized("power.siege.start")
            return
        }

        addPlayer(player)
    }

    fun startSiege(player: Player) {
        if (player.uniqueId in players)
            return
        if (players.isEmpty() && player.factionUser().factionId == claim.factionId)
            return
        if (players.isNotEmpty()) {
            player.sendLocalized("power.siege.already-started")
            return
        }

        addPlayer(player)
    }

    fun leaveClaimCombat(player: Player) {
        if (player.uniqueId !in players)
            return

        siegeProgressSpeed -= if (isIntruder(player)) config.siegeBreachProgress else -config.siegeResistanceProgress

        if (player.factionUser().factionId != claim.factionId &&
            players.filter { it.factionUser().factionId != claim.factionId }.size == 1) {
            siegeProgressSpeed = -config.siegeClaimRecoverySpeed
            hideAllBossBars()
        }
        players.remove(player.uniqueId)
    }

    private fun getBossBar(player: Player) = translatedBossBars.computeIfAbsent(player.getLocaleEnum()) {
        val bar = BossBar.bossBar(Component.empty(), 1f, BossBar.Color.RED, BossBar.Overlay.NOTCHED_10)
        updateBossBar(it, bar)
        return@computeIfAbsent bar
    }

    private fun updateBossBars() = translatedBossBars.forEach { updateBossBar(it.key, it.value) }

    private fun updateBossBar(locale: Locale, bossBar: BossBar) {
        bossBar.name(
            locale.localize(
                "base.boss-bars.siege", mapOf(
                    "participants" to players.size.toString(),
                    "x" to claim.chunkX.toString(),
                    "z" to claim.chunkZ.toString(),
                    "world" to claim.world
                )
            )
        )
        bossBar.progress((claimIntegrity / 100.0).toFloat())
        bossBar.color(if (siegeProgressSpeed > 0) BossBar.Color.RED else BossBar.Color.GREEN)
    }

    private fun isIntruder(player: Player) = player.factionUser().factionId != claim.factionId

    private fun startSiegeTask() {
        if (task != null)
            return

        println("Scheduling new task")
        task = SiegeTask(this).runTaskTimer(ImprovedFactionsPlugin.instance, 0, 20)
        handleSiegeStart()
    }

    fun tickSiege() {
        setIntegrity(claimIntegrity - siegeProgressSpeed)
        updateBossBars()
        if (claimIntegrity <= 0.001)
            handleSiegeVictory()
         else if (claimIntegrity >= 99.999)
             handleSiegeFailure()

    }

    private fun setIntegrity(value: Double) {
        claimIntegrity = MathUtils.clamp(value, 0.0, 100.0)
    }

    private fun stopSiegeTask() {
        println("stop siege task")
        task?.cancel()
        task = null
        remove(claim)
    }

    private fun getAssociatedFactions() = loggedTransaction {
        players
            .map { it.factionUser().factionId }
            .toMutableList()
            .also { it.add(claim.factionId) }
            .distinct()
            .mapNotNull { Faction.findById(it) }
    }

    fun resetState() {
        hideAllBossBars()
        translatedBossBars.clear()
        players.clear()
    }

    private fun hideAllBossBars() = loggedTransaction {
        getAssociatedFactions()
            .flatMap { it.members().mapNotNull { user -> user.player() } }
            .forEach { it.toAudience().hideBossBar(getBossBar(it)) }
    }

    // Events
    private fun handleSiegeVictory() {
        stopSiegeTask()
        getAssociatedFactions()
            .forEach {
                it.broadcast(
                    "power.siege.unclaimed", mapOf(
                        "x" to claim.chunkX.toString(),
                        "z" to claim.chunkZ.toString(),
                        "world" to claim.world
                    )
                )
            }
        loggedTransaction { claim.chunk()?.let { claim.faction()?.unclaim(it) } }
    }

    private fun handleSiegeFailure() {
        stopSiegeTask()
        getAssociatedFactions()
            .forEach {
                it.broadcast(
                    "power.siege.defended", mapOf(
                        "x" to claim.chunkX.toString(),
                        "z" to claim.chunkZ.toString(),
                        "world" to claim.world
                    )
                )
            }
    }

    private fun handleSiegeStart() {
        getAssociatedFactions()
            .forEach {
                it.broadcast(
                    "power.siege.started", mapOf(
                        "x" to claim.chunkX.toString(),
                        "z" to claim.chunkZ.toString(),
                        "world" to claim.world
                    )
                )
            }
    }

    private fun addPlayer(player: Player) {
        players.add(player.uniqueId)
        siegeProgressSpeed += if (isIntruder(player)) config.siegeBreachProgress else -config.siegeResistanceProgress
        player.toAudience().showBossBar(getBossBar(player))

        getAssociatedFactions()
            .flatMap { it.members().mapNotNull { user -> user.player() } }
            .forEach { it.toAudience().showBossBar(getBossBar(it)) }

        if (players.size == 1)
            startSiegeTask()
    }
}