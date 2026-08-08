package io.github.toberocat.improvedfactions.modules.relations

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.commands.processor.relationsCommandProcessors
import io.github.toberocat.improvedfactions.database.storage.GameStateCommands
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.modules.Module
import io.github.toberocat.improvedfactions.modules.relations.commands.*
import io.github.toberocat.improvedfactions.modules.relations.handles.DummyRelationsModuleHandle
import io.github.toberocat.improvedfactions.modules.relations.handles.RelationsModuleHandle
import io.github.toberocat.improvedfactions.modules.relations.impl.RelationsModuleHandleImpl
import io.github.toberocat.toberocore.command.CommandExecutor

object RelationsModule : Module {
    const val MODULE_NAME = "relations"
    override val moduleName = MODULE_NAME
    override var isEnabled = false

    private var relationsModuleHandle: RelationsModuleHandle = DummyRelationsModuleHandle()


    override fun onEnable(plugin: ImprovedFactionsPlugin) {
        relationsModuleHandle = RelationsModuleHandleImpl()
    }

    override fun getCommandProcessors(plugin: ImprovedFactionsPlugin) =
        relationsCommandProcessors(plugin)

    fun deleteFactionRelations(factionId: Int) = GameStateCommands.deleteFactionRelations(factionId)

    fun relationsModulePair() = moduleName to this

}
