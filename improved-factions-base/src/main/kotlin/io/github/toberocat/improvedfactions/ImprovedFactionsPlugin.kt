package io.github.toberocat.improvedfactions

import com.jeff_media.updatechecker.UpdateCheckSource
import com.jeff_media.updatechecker.UpdateChecker
import com.jeff_media.updatechecker.UserAgentBuilder
import io.github.toberocat.improvedfactions.claims.FactionClaims
import io.github.toberocat.improvedfactions.claims.clustering.ClaimClusterDetector
import io.github.toberocat.improvedfactions.claims.clustering.DatabaseClaimQueryProvider
import io.github.toberocat.improvedfactions.commands.FactionCommandExecutor
import io.github.toberocat.improvedfactions.commands.claim.FactionMap
import io.github.toberocat.improvedfactions.config.ImprovedFactionsConfig
import io.github.toberocat.improvedfactions.database.DatabaseConnector
import io.github.toberocat.improvedfactions.factions.Factions
import io.github.toberocat.improvedfactions.invites.FactionInvites
import io.github.toberocat.improvedfactions.listeners.move.MoveListener
import io.github.toberocat.improvedfactions.modules.dynmap.DynmapModule
import io.github.toberocat.improvedfactions.modules.gui.GuiModule
import io.github.toberocat.improvedfactions.modules.home.HomeModule
import io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule
import io.github.toberocat.improvedfactions.modules.wilderness.WildernessModule
import io.github.toberocat.improvedfactions.papi.PapiExpansion
import io.github.toberocat.improvedfactions.ranks.FactionRankHandler
import io.github.toberocat.improvedfactions.ranks.FactionRanks
import io.github.toberocat.improvedfactions.translation.updateLanguages
import io.github.toberocat.improvedfactions.utils.BStatsCollector
import io.github.toberocat.improvedfactions.utils.FileUtils
import io.github.toberocat.improvedfactions.utils.arguments.ClaimRadiusArgument
import io.github.toberocat.improvedfactions.utils.particles.ParticleAnimation
import io.github.toberocat.improvedfactions.utils.threadPool
import io.github.toberocat.improvedfactions.zone.ZoneHandler
import io.github.toberocat.toberocore.command.CommandExecutor
import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.OfflinePlayer
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.exposed.sql.Database


/**
 * Created: 04.08.2023
 * @author Tobias Madlberger (Tobias)
 */
const val SPIGOT_RESOURCE_ID = 95617

class ImprovedFactionsPlugin : JavaPlugin() {

    private lateinit var database: Database
    lateinit var adventure: BukkitAudiences
    lateinit var improvedFactionsConfig: ImprovedFactionsConfig
    lateinit var claimChunkClusters: ClaimClusterDetector
    lateinit var papiTransformer: (player: OfflinePlayer, input: String) -> String

    companion object {
        lateinit var instance: ImprovedFactionsPlugin
            private set
        val modules = mutableMapOf(
            PowerRaidsModule.powerRaidsPair(),
            DynmapModule.dynmapPair(),
            WildernessModule.wildernessPair(),
            HomeModule.homePair(),
            GuiModule.guiPair()
        )

        fun getActiveModules() = modules.filter { it.value.shouldEnable(instance) }
    }

    fun addModuleCommands(executor: CommandExecutor) {
        modules.filter { it.value.shouldEnable(this) }
            .forEach { (_, module) -> module.addCommands(this, executor) }
    }

    override fun onEnable() {
        saveDefaultConfig()
        instance = this
        adventure = BukkitAudiences.create(this)
        claimChunkClusters = ClaimClusterDetector(DatabaseClaimQueryProvider())

        BStatsCollector(this)
        checkForUpdate()

        copyFolders()
        loadConfig()

        database = DatabaseConnector(this).createDatabase()

        registerModules()
        registerListeners()
        registerCommands()
        registerPapi()

        updateLanguages(this)

        claimChunkClusters.detectClusters()
    }

    override fun onDisable() {
        adventure.close()
        threadPool.shutdown()
    }

    private fun checkForUpdate() {
        if (!config.getBoolean("update-checker")) return

        logger.info("Checking for updates...")
        UpdateChecker(this, UpdateCheckSource.SPIGOT, SPIGOT_RESOURCE_ID.toString())
            .setDownloadLink(SPIGOT_RESOURCE_ID)
            .setDonationLink("https://www.paypal.com/donate/?hosted_button_id=BGB6QWR886Q6Y")
            .setChangelogLink(SPIGOT_RESOURCE_ID)
            .setNotifyOpsOnJoin(true)
            .setColoredConsoleOutput(true)
            .setSupportLink("https://discord.com/invite/yJYyNRfk39")
            .setNotifyByPermissionOnJoin("factions.updatechecker")
            .setUserAgent(
                UserAgentBuilder()
                    .addServerVersion()
                    .addBukkitVersion()
                    .addPluginNameAndVersion()
            )
            .checkEveryXHours(24.0)
            .checkNow()
    }

    private fun copyFolders() {
        FileUtils.copyAll(this, "languages")
    }

    private fun registerModules() = getActiveModules()
        .forEach { (name, module) ->
            module.onEnable(this)
            logger.info("Loaded module $name")
            module.reloadConfig(this)
        }

    private fun registerPapi() {
        if (server.pluginManager.isPluginEnabled("PlaceholderAPI")) {
            PapiExpansion(improvedFactionsConfig).register()
            papiTransformer = { player, input -> PlaceholderAPI.setPlaceholders(player, input) }
            logger.info("Loaded improved factions papi extension")
            return
        }

        papiTransformer = { _, input -> input }
        logger.info("Papi not found. Skipping Papi registration")
    }


    fun loadConfig() {
        improvedFactionsConfig = ImprovedFactionsConfig.createConfig(this)
        Factions.maxNameLength = config.getInt("factions.unsafe.max-name-length", 36)
        Factions.maxIconLength = config.getInt("factions.unsafe.max-icon-length", 5000)
        Factions.maxSpacesInName = config.getInt("factions.max-spaces-in-name", 5)
        Factions.nameRegex = Regex(config.getString("factions.name-regex") ?: "[a-zA-Z ]*")
        FactionInvites.inviteExpiresInMinutes = config.getInt("factions.invites-expire-in", 5)
        FactionRanks.maxRankNameLength = config.getInt("factions.max-rank-name-length", 50)
        FactionClaims.blockedWorlds = config.getStringList("blacklisted-worlds").toSet()
        FactionRankHandler.guestRankName =
            config.getString("factions.unsafe.guest-rank-name") ?: FactionRankHandler.guestRankName
        FactionRanks.rankNameRegex = Regex(config.getString("factions.rank-name-regex") ?: "[a-zA-Z ]*")

        ParticleAnimation.hideDecorativeParticles = config.getBoolean("performance.decorative-particles.hidden", false)
        ParticleAnimation.tickSpeed = config.getLong("performance.decorative-particles.tick-speed", 1)

        ClaimRadiusArgument.MAX_RADIUS = config.getInt("factions.max-claim-radius", 10)
        FactionMap.MAP_WIDTH = config.getInt("factions.map-width", FactionMap.MAP_WIDTH)
        FactionMap.MAP_HEIGHT = config.getInt("factions.map-height", FactionMap.MAP_HEIGHT)

        config.getConfigurationSection("zones")?.getKeys(false)?.let {
            it.forEach { zone ->
                val section = config.getConfigurationSection("zones.$zone")
                if (section == null) {
                    logger.warning("Invalid formatted zone $zone found in config")
                    return@forEach
                }
                ZoneHandler.createZone(this, zone, section)
            }
        }

        ZoneHandler.defaultZoneCheck(this)

        modules.values.forEach { it.reloadConfig(this) }
    }

    private fun registerCommands() {
        FactionCommandExecutor(this)
    }

    private fun registerListeners() {
        server.pluginManager.registerEvents(MoveListener(this), this)
    }
}