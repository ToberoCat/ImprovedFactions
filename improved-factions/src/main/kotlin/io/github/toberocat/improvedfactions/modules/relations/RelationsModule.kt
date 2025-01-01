package io.github.toberocat.improvedfactions.modules.relations

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.commands.processor.relationsCommandProcessors
import io.github.toberocat.improvedfactions.database.DatabaseManager
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.modules.Module
import io.github.toberocat.improvedfactions.modules.relations.commands.*
import io.github.toberocat.improvedfactions.modules.relations.database.FactionAllyInvites
import io.github.toberocat.improvedfactions.modules.relations.database.FactionRelations
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

    override fun onLoadDatabase(plugin: ImprovedFactionsPlugin) {
        loggedTransaction {
            DatabaseManager.createTables(
                FactionRelations,
                FactionAllyInvites
            )
            FactionAllyInvites.scheduleInviteExpirations()
        }
    }

    fun deleteFactionRelations(factionId: Int) {
        relationsModuleHandle.deleteFactionRelations(factionId)
    }

    fun relationsModulePair() = moduleName to this

    fun Faction.isAllied(targetFactionId: Int) = relationsModuleHandle.isAllied(id.value, targetFactionId)
    fun Faction.isEnemy(targetFactionId: Int) = relationsModuleHandle.isEnemy(id.value, targetFactionId)
    fun Faction.allies() = relationsModuleHandle.getAlliedFactions(id.value)
    fun Faction.enemies() = relationsModuleHandle.getEnemyFactions(id.value)
    fun Faction.inviteToAlliance(targetFaction: Faction) =
        relationsModuleHandle.inviteToAlliance(id.value, targetFaction.id.value)

    fun Faction.acceptAllyInvite(targetFaction: Faction) =
        relationsModuleHandle.acceptAlliance(id.value, targetFaction.id.value)

    fun Faction.declareWar(targetFaction: Faction) = relationsModuleHandle.declareWar(id.value, targetFaction.id.value)
}