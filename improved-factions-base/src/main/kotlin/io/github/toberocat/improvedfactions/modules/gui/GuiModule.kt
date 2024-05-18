package io.github.toberocat.improvedfactions.modules.gui

import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.function.FunctionProcessor
import io.github.toberocat.guiengine.utils.logger.PluginLogger
import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.commands.rank.CreateRankCommand
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.modules.gui.actions.GuiEngineCommandMapperAction
import io.github.toberocat.improvedfactions.modules.gui.commands.core.GuiInfoCommand
import io.github.toberocat.improvedfactions.modules.gui.commands.core.GuiListInvitesCommand
import io.github.toberocat.improvedfactions.modules.gui.commands.core.GuiMembersCommand
import io.github.toberocat.improvedfactions.modules.gui.commands.core.GuiRankCommandRoute
import io.github.toberocat.improvedfactions.modules.gui.commands.power.GuiPowerCommand
import io.github.toberocat.improvedfactions.modules.gui.components.icon.FactionIconComponent
import io.github.toberocat.improvedfactions.modules.gui.components.icon.FactionIconComponentBuilder
import io.github.toberocat.improvedfactions.modules.gui.components.permission.FactionPermissionComponent
import io.github.toberocat.improvedfactions.modules.gui.components.permission.FactionPermissionComponentBuilder
import io.github.toberocat.improvedfactions.modules.gui.components.permission.TYPE
import io.github.toberocat.improvedfactions.modules.gui.components.rank.FactionRankComponent
import io.github.toberocat.improvedfactions.modules.gui.components.rank.FactionRankComponentBuilder
import io.github.toberocat.improvedfactions.modules.gui.components.rankselector.FactionRankSelectorComponent
import io.github.toberocat.improvedfactions.modules.gui.components.rankselector.FactionRankSelectorComponentBuilder
import io.github.toberocat.improvedfactions.modules.gui.functions.FactionPermissionFunction
import io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule
import io.github.toberocat.improvedfactions.modules.power.impl.FactionPowerRaidModuleHandleImpl
import io.github.toberocat.toberocore.command.CommandExecutor
import org.bukkit.Bukkit

class GuiModule : BaseModule {
    override val moduleName = MODULE_NAME

    private lateinit var guiEngineApi: GuiEngineApi

    override fun reloadConfig(plugin: ImprovedFactionsPlugin) {
    }

    override fun addCommands(plugin: ImprovedFactionsPlugin, executor: CommandExecutor) {
        executor.overrideCoreCommands(plugin)

        (PowerRaidsModule.powerRaidModule().factionModuleHandle as? FactionPowerRaidModuleHandleImpl)?.let {
            executor.overridePowerRaidModuleCommands(plugin, it)
        }
    }


    override fun onEnable(plugin: ImprovedFactionsPlugin) {
        guiEngineApi = GuiEngineApi(plugin)
        registerComponents()
        registerFunctions()
        guiEngineApi.reload(PluginLogger(plugin.logger))
    }

    override fun shouldEnable(plugin: ImprovedFactionsPlugin): Boolean {
        val shouldEnable = super.shouldEnable(plugin)
        if (!shouldEnable) {
            return false
        }

        if (Bukkit.getPluginManager().isPluginEnabled("GuiEngine")) {
            return true
        }

        warn("Gui module is enabled but GuiEngine is not installed. Disabling Gui module.")
        return false
    }

    private fun registerComponents() {
        guiEngineApi.registerFactory(
            "faction-icon", FactionIconComponent::class.java, FactionIconComponentBuilder::class.java
        )

        guiEngineApi.registerFactory(
            "faction-rank", FactionRankComponent::class.java, FactionRankComponentBuilder::class.java
        )

        guiEngineApi.registerFactory(
            TYPE, FactionPermissionComponent::class.java, FactionPermissionComponentBuilder::class.java
        )

        guiEngineApi.registerFactory(
            FactionRankSelectorComponent.TYPE,
            FactionRankSelectorComponent::class.java,
            FactionRankSelectorComponentBuilder::class.java
        )
    }

    private fun registerFunctions() {
        FunctionProcessor.registerComputeFunction(FactionPermissionFunction())
    }

    private fun CommandExecutor.overrideCoreCommands(
        plugin: ImprovedFactionsPlugin,
    ) {
        val rankRoute = GuiRankCommandRoute(guiEngineApi, plugin)

        addChild(rankRoute)
        addChild(GuiInfoCommand(guiEngineApi, plugin))
        addChild(GuiMembersCommand(guiEngineApi, plugin))
        addChild(GuiListInvitesCommand(guiEngineApi, plugin))

        (rankRoute.getChild("create") as? CreateRankCommand)
            ?.let { GuiEngineCommandMapperAction(it.permission, it) }
    }

    private fun CommandExecutor.overridePowerRaidModuleCommands(
        plugin: ImprovedFactionsPlugin,
        powerRaidModuleHandle: FactionPowerRaidModuleHandleImpl
    ) {
        addChild(GuiPowerCommand(guiEngineApi, plugin, powerRaidModuleHandle))
    }

    companion object {
        const val MODULE_NAME = "gui"

        fun guiPair() = MODULE_NAME to GuiModule()
    }
}