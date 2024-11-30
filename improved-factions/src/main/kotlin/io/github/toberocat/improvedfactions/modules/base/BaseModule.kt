package io.github.toberocat.improvedfactions.modules.base

import com.jeff_media.updatechecker.UpdateCheckSource
import com.jeff_media.updatechecker.UpdateChecker
import com.jeff_media.updatechecker.UserAgentBuilder
import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.SPIGOT_RESOURCE_ID
import io.github.toberocat.improvedfactions.claims.clustering.detector.ClaimClusterDetector
import io.github.toberocat.improvedfactions.claims.clustering.query.DatabaseClaimQueryProvider
import io.github.toberocat.improvedfactions.commands.executor.GeneratedFactionCommandExecutor
import io.github.toberocat.improvedfactions.config.ImprovedFactionsConfig
import io.github.toberocat.improvedfactions.database.DatabaseConnector
import io.github.toberocat.improvedfactions.listeners.PlayerJoinListener
import io.github.toberocat.improvedfactions.listeners.move.MoveListener
import io.github.toberocat.improvedfactions.modules.Module
import io.github.toberocat.improvedfactions.papi.PapiExpansion
import io.github.toberocat.improvedfactions.translation.updateLanguages
import io.github.toberocat.improvedfactions.utils.BStatsCollector
import io.github.toberocat.improvedfactions.utils.FileUtils
import io.github.toberocat.toberocore.command.CommandExecutor
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.event.Listener
import org.jetbrains.exposed.sql.Database
import java.util.logging.Logger

object BaseModule : Module {
    const val MODULE_NAME = "base"
    override val moduleName = MODULE_NAME
    override var isEnabled = false

    lateinit var adventure: BukkitAudiences
    lateinit var config: ImprovedFactionsConfig
    lateinit var database: Database
    lateinit var claimChunkClusters: ClaimClusterDetector
    lateinit var logger: Logger
    lateinit var plugin: ImprovedFactionsPlugin

    override fun onEnable(plugin: ImprovedFactionsPlugin) {
        this.plugin = plugin
        logger = plugin.logger

        adventure = BukkitAudiences.create(plugin)
        claimChunkClusters = ClaimClusterDetector(DatabaseClaimQueryProvider())

        BStatsCollector(plugin)
        checkForUpdate()

        copyFolders()

        registerListeners(
            MoveListener(plugin),
            PlayerJoinListener()
        )

        registerPapi()

        updateLanguages(plugin)
    }

    override fun onEverythingEnabled(plugin: ImprovedFactionsPlugin) {
        claimChunkClusters.detectClusters()
    }

    override fun onLoadDatabase(plugin: ImprovedFactionsPlugin) {
        database = DatabaseConnector(plugin).createDatabase()
    }

    override fun onDisable(plugin: ImprovedFactionsPlugin) {
        adventure.close()
    }

    private fun checkForUpdate() {
        if (!plugin.config.getBoolean("update-checker")) return

        logger.info("Checking for updates...")
        UpdateChecker(plugin, UpdateCheckSource.SPIGOT, SPIGOT_RESOURCE_ID.toString())
            .setDownloadLink(SPIGOT_RESOURCE_ID)
            .setDonationLink("https://www.paypal.com/donate/?hosted_button_id=BGB6QWR886Q6Y")
            .setChangelogLink(SPIGOT_RESOURCE_ID)
            .setNotifyOpsOnJoin(true)
            .setColoredConsoleOutput(false)
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
        FileUtils.copyAll(plugin, "languages")
    }

    private fun registerPapi() {
        if (plugin.server.pluginManager.isPluginEnabled("PlaceholderAPI")) {
            PapiExpansion(config).register()
            //papiTransformer = { player, input -> PlaceholderAPI.setPlaceholders(player, input) } ToDo
            logger.info("Loaded improved factions papi extension")
            return
        }

        //papiTransformer = { _, input -> input } ToDo
        logger.info("Papi not found. Skipping Papi registration")
    }

    override fun reloadConfig(plugin: ImprovedFactionsPlugin) {
        config = ImprovedFactionsConfig.createConfig(plugin)
        config.reload(plugin, plugin.config)
    }

    override fun addCommands(plugin: ImprovedFactionsPlugin, executor: CommandExecutor) {
        GeneratedFactionCommandExecutor(BaseModule.plugin).bindToPluginCommand("factions")
    }

    fun basePair() = MODULE_NAME to this
}