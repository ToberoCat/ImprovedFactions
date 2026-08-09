package io.github.toberocat.improvedfactions.claims.overclaim

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.clustering.position.ChunkPosition
import io.github.toberocat.improvedfactions.database.storage.ClaimKey
import io.github.toberocat.improvedfactions.database.storage.ClaimSnapshot
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.database.storage.GameStateCommands
import io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule.powerRaidModule
import io.github.toberocat.improvedfactions.translation.getLocaleEnum
import io.github.toberocat.improvedfactions.translation.localize
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.modules.home.HomeModule
import io.github.toberocat.improvedfactions.utils.toAudience
import io.github.toberocat.toberocore.util.MathUtils
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import java.util.*

class ClaimSiegeManager(private val claimKey: ClaimKey, private val claimedFactionId: Int) {
    private val config = powerRaidModule().config
    private val translatedBossBars = mutableMapOf<Locale, BossBar>()
    private val players = mutableSetOf<UUID>()
    private var task: BukkitTask? = null
    private var siegeProgressSpeed: Double = 0.0
    private var claimIntegrity: Double = 100.0

    companion object {
        private val siegeManagers = mutableMapOf<ChunkPosition, ClaimSiegeManager>()

        fun getManager(claim: ClaimSnapshot): ClaimSiegeManager {
            val position = ChunkPosition(claim.key.chunkX, claim.key.chunkZ, claim.key.world)
            return siegeManagers.compute(position) { _, existing ->
                existing?.takeIf { it.claimedFactionId == claim.factionId }
                    ?: ClaimSiegeManager(claim.key, claim.factionId)
            }
                ?: error("Unable to create siege manager")
        }

        fun remove(key: ClaimKey) = siegeManagers.remove(ChunkPosition(key.chunkX, key.chunkZ, key.world))

        private fun broadcastToFactions(
            factionIds: List<Int>,
            key: String,
            placeholders: Map<String, String>
        ) {
            factionIds.flatMap(StorageManager.cache::factionMembers)
                .distinct()
                .mapNotNull(Bukkit::getPlayer)
                .forEach { it.sendLocalized(key, placeholders) }
        }
    }

    fun enterClaimCombat(player: Player) {
        if (player.uniqueId in players)
            return
        if (players.isEmpty() && playerFactionId(player.uniqueId) == claimedFactionId)
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
        if (players.isEmpty() && playerFactionId(player.uniqueId) == claimedFactionId)
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

        if (playerFactionId(player.uniqueId) != claimedFactionId &&
            players.count { playerFactionId(it) != claimedFactionId } == 1) {
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
                    "x" to claimKey.chunkX.toString(),
                    "z" to claimKey.chunkZ.toString(),
                    "world" to claimKey.world
                )
            )
        )
        bossBar.progress((claimIntegrity / 100.0).toFloat())
        bossBar.color(if (siegeProgressSpeed > 0) BossBar.Color.RED else BossBar.Color.GREEN)
    }

    private fun isIntruder(player: Player) = playerFactionId(player.uniqueId) != claimedFactionId

    private fun playerFactionId(playerId: UUID) = StorageManager.cache.user(playerId)?.factionId ?: -1

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
        remove(claimKey)
    }

    private fun getAssociatedFactionIds(): List<Int> =
        players
            .map(::playerFactionId)
            .toMutableList()
            .also { it.add(claimedFactionId) }
            .distinct()

    private fun associatedOnlinePlayers(): List<Player> = getAssociatedFactionIds()
        .flatMap(StorageManager.cache::factionMembers)
        .distinct()
        .mapNotNull(Bukkit::getPlayer)

    fun resetState() {
        hideAllBossBars()
        translatedBossBars.clear()
        players.clear()
    }

    private fun hideAllBossBars() = associatedOnlinePlayers()
        .forEach { it.toAudience().hideBossBar(getBossBar(it)) }

    // Events
    private fun handleSiegeVictory() {
        stopSiegeTask()
        val factionIds = getAssociatedFactionIds()
        val placeholders = mapOf(
                "x" to claimKey.chunkX.toString(),
                "z" to claimKey.chunkZ.toString(),
                "world" to claimKey.world
            )
        StorageManager.continueOnMain(GameStateCommands.unclaim(claimKey), {
            HomeModule.warnIfHomeWasUnclaimed(claimedFactionId, listOf(claimKey))
            broadcastToFactions(factionIds, "power.siege.unclaimed", placeholders)
        }) { failure ->
            ImprovedFactionsPlugin.instance.logger.warning("Unable to persist siege victory: ${failure.message}")
        }
    }

    private fun handleSiegeFailure() {
        stopSiegeTask()
        broadcastToAssociatedPlayers(
            "power.siege.defended", mapOf(
                "x" to claimKey.chunkX.toString(),
                "z" to claimKey.chunkZ.toString(),
                "world" to claimKey.world
            )
        )
    }

    private fun handleSiegeStart() {
        broadcastToAssociatedPlayers(
            "power.siege.started", mapOf(
                "x" to claimKey.chunkX.toString(),
                "z" to claimKey.chunkZ.toString(),
                "world" to claimKey.world
            )
        )
    }

    private fun broadcastToAssociatedPlayers(key: String, placeholders: Map<String, String>) =
        associatedOnlinePlayers().forEach { it.sendLocalized(key, placeholders) }

    private fun addPlayer(player: Player) {
        players.add(player.uniqueId)
        siegeProgressSpeed += if (isIntruder(player)) config.siegeBreachProgress else -config.siegeResistanceProgress
        player.toAudience().showBossBar(getBossBar(player))

        associatedOnlinePlayers().forEach { it.toAudience().showBossBar(getBossBar(it)) }

        if (players.size == 1)
            startSiegeTask()
    }
}
