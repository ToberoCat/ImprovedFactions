package io.github.toberocat.improvedfactions.modules.base

import com.jeff_media.updatechecker.UpdateCheckSource
import com.jeff_media.updatechecker.UpdateChecker
import com.jeff_media.updatechecker.UserAgentBuilder
import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.SPIGOT_RESOURCE_ID
import io.github.toberocat.improvedfactions.annotations.papi.PapiPlaceholder
import io.github.toberocat.improvedfactions.annotations.permission.Permission
import io.github.toberocat.improvedfactions.annotations.permission.PermissionConfigurations
import io.github.toberocat.improvedfactions.claims.clustering.detector.ClaimClusterDetector
import io.github.toberocat.improvedfactions.claims.clustering.query.DatabaseClaimQueryProvider
import io.github.toberocat.improvedfactions.commands.processor.baseCommandProcessors
import io.github.toberocat.improvedfactions.config.ImprovedFactionsConfig
import io.github.toberocat.improvedfactions.database.DatabaseConnector
import io.github.toberocat.improvedfactions.integrations.Integrations
import io.github.toberocat.improvedfactions.listeners.PlayerJoinListener
import io.github.toberocat.improvedfactions.listeners.move.MoveListener
import io.github.toberocat.improvedfactions.modules.Module
import io.github.toberocat.improvedfactions.translation.updateLanguages
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.BStatsCollector
import io.github.toberocat.improvedfactions.utils.FileUtils
import io.github.toberocat.improvedfactions.utils.toOfflinePlayer
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.OfflinePlayer
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
    lateinit var integrations: Integrations

    override fun onEnable(plugin: ImprovedFactionsPlugin) {
        this.plugin = plugin
        logger = plugin.logger

        adventure = BukkitAudiences.create(plugin)
        claimChunkClusters = ClaimClusterDetector(DatabaseClaimQueryProvider())
        config = ImprovedFactionsConfig.createConfig(plugin)

        BStatsCollector(plugin)
        checkForUpdate()

        copyFolders()

        plugin.registerListeners(
            MoveListener(),
            PlayerJoinListener()
        )

        integrations = Integrations(plugin)
        updateLanguages(plugin)
    }

    override fun shouldEnable(plugin: ImprovedFactionsPlugin) = true

    override fun onEverythingEnabled(plugin: ImprovedFactionsPlugin) {
        claimChunkClusters.detectClusters()
        integrations.loadIntegrations()
    }

    override fun onLoadDatabase(plugin: ImprovedFactionsPlugin) {
        database = DatabaseConnector(plugin).createDatabase()
    }

    override fun onDisable(plugin: ImprovedFactionsPlugin) {
        adventure.close()
    }

    @PapiPlaceholder("owner", MODULE_NAME, "The owner of the faction")
    @PapiPlaceholder("name", MODULE_NAME, "The name of the faction")
    @PapiPlaceholder("rank", MODULE_NAME, "The rank of the player in the faction")
    @PapiPlaceholder("join_mode", MODULE_NAME, "The join mode of the faction")
    override fun onPlaceholder(placeholders: HashMap<String, (player: OfflinePlayer) -> String?>) {
        placeholders["owner"] = { it.factionUser().faction()?.owner?.toOfflinePlayer()?.name }
        placeholders["name"] = { it.factionUser().faction()?.name }
        placeholders["rank"] = { it.factionUser().rank().name }
        placeholders["join_mode"] = { it.factionUser().faction()?.factionJoinType?.toString() }
    }

    @Permission("factions.updatechecker", config = PermissionConfigurations.OP_ONLY)
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

    override fun reloadConfig(plugin: ImprovedFactionsPlugin) {
        config.reload(plugin, plugin.config)
    }

    override fun getCommandProcessors(plugin: ImprovedFactionsPlugin) =
        baseCommandProcessors(plugin)

    fun basePair() = MODULE_NAME to this
}