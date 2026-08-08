package io.github.toberocat.improvedfactions.modules.relations.handles

import java.util.concurrent.CompletionStage

interface RelationsModuleHandle {
    fun getAlliedFactions(factionId: Int): Set<Int>
    fun getEnemyFactions(factionId: Int): Set<Int>
    fun isAllied(factionId: Int, targetFactionId: Int): Boolean
    fun isEnemy(factionId: Int, targetFactionId: Int): Boolean

    fun inviteToAlliance(factionId: Int, targetFactionId: Int): CompletionStage<Unit>
    fun acceptAlliance(factionId: Int, targetFactionId: Int): CompletionStage<Unit>
    fun declareWar(factionId: Int, targetFactionId: Int): CompletionStage<Unit>
    fun breakAlliance(factionId: Int, targetFactionId: Int): CompletionStage<Unit>
    fun makePeace(factionId: Int, targetFactionId: Int): CompletionStage<Unit>
    fun deleteFactionRelations(factionId: Int): CompletionStage<Unit>
}
