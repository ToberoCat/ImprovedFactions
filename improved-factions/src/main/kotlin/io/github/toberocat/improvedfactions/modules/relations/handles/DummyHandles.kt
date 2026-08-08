package io.github.toberocat.improvedfactions.modules.relations.handles

import java.util.concurrent.CompletableFuture

class DummyRelationsModuleHandle : RelationsModuleHandle {
    override fun getAlliedFactions(factionId: Int) = emptySet<Int>()

    override fun getEnemyFactions(factionId: Int) = emptySet<Int>()

    override fun isAllied(factionId: Int, targetFactionId: Int) = false

    override fun isEnemy(factionId: Int, targetFactionId: Int) = false

    override fun inviteToAlliance(factionId: Int, targetFactionId: Int) = CompletableFuture.completedFuture(Unit)

    override fun acceptAlliance(factionId: Int, targetFactionId: Int) = CompletableFuture.completedFuture(Unit)

    override fun declareWar(factionId: Int, targetFactionId: Int) = CompletableFuture.completedFuture(Unit)

    override fun breakAlliance(factionId: Int, targetFactionId: Int) = CompletableFuture.completedFuture(Unit)

    override fun makePeace(factionId: Int, targetFactionId: Int) = CompletableFuture.completedFuture(Unit)
    override fun deleteFactionRelations(factionId: Int) = CompletableFuture.completedFuture(Unit)
}
