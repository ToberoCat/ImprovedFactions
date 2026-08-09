package io.github.toberocat.improvedfactions.modules.relations.impl

import io.github.toberocat.improvedfactions.database.storage.GameStateCommands
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.modules.relations.RelationType
import io.github.toberocat.improvedfactions.modules.relations.handles.RelationsModuleHandle
import io.github.toberocat.improvedfactions.translation.LocalizedException
import java.time.Instant

class RelationsModuleHandleImpl : RelationsModuleHandle {
    override fun getAlliedFactions(factionId: Int) = StorageManager.cache.relations(factionId, RelationType.ALLY.name)

    override fun getEnemyFactions(factionId: Int) = StorageManager.cache.relations(factionId, RelationType.ENEMY.name)

    override fun isAllied(factionId: Int, targetFactionId: Int) = targetFactionId in getAlliedFactions(factionId)

    override fun isEnemy(factionId: Int, targetFactionId: Int) = targetFactionId in getEnemyFactions(factionId)

    override fun inviteToAlliance(factionId: Int, targetFactionId: Int): java.util.concurrent.CompletionStage<Unit> {
        validateDistinct(factionId, targetFactionId, "relations.exceptions.cant-ally-yourself")
        if (isAllied(factionId, targetFactionId)) fail("relations.exceptions.already-allied")
        if (isEnemy(factionId, targetFactionId)) fail("relations.exceptions.already-enemy")
        if (StorageManager.cache.allyInvite(factionId, targetFactionId) != null) fail("relations.exceptions.already-invited")
        return GameStateCommands.createAllyInvite(factionId, targetFactionId, Instant.now().plusSeconds(300))
    }

    override fun acceptAlliance(factionId: Int, targetFactionId: Int): java.util.concurrent.CompletionStage<Unit> {
        validateDistinct(factionId, targetFactionId, "relations.exceptions.cant-ally-yourself")
        if (isAllied(factionId, targetFactionId)) fail("relations.exceptions.already-allied")
        if (isEnemy(factionId, targetFactionId)) fail("relations.exceptions.already-enemy")
        if (StorageManager.cache.allyInvite(factionId, targetFactionId) == null) fail("relations.exceptions.no-invite")
        return GameStateCommands.acceptAllyInvite(factionId, targetFactionId)
    }

    override fun declareWar(factionId: Int, targetFactionId: Int): java.util.concurrent.CompletionStage<Unit> {
        validateDistinct(factionId, targetFactionId, "relations.exceptions.cant-declare-war-on-yourself")
        if (isAllied(factionId, targetFactionId)) fail("relations.exceptions.already-allied")
        if (isEnemy(factionId, targetFactionId)) fail("relations.exceptions.already-enemy")
        return GameStateCommands.createRelation(factionId, targetFactionId, RelationType.ENEMY.ordinal)
    }

    override fun breakAlliance(factionId: Int, targetFactionId: Int): java.util.concurrent.CompletionStage<Unit> {
        validateDistinct(factionId, targetFactionId, "relations.exceptions.cant-break-alliance-with-yourself")
        if (!isAllied(factionId, targetFactionId)) fail("relations.exceptions.not-allied")
        return GameStateCommands.deleteRelation(factionId, targetFactionId, RelationType.ALLY.ordinal)
    }

    override fun makePeace(factionId: Int, targetFactionId: Int): java.util.concurrent.CompletionStage<Unit> {
        validateDistinct(factionId, targetFactionId, "relations.exceptions.cant-make-peace-with-yourself")
        if (!isEnemy(factionId, targetFactionId)) fail("relations.exceptions.not-enemy")
        return GameStateCommands.deleteRelation(factionId, targetFactionId, RelationType.ENEMY.ordinal)
    }

    override fun deleteFactionRelations(factionId: Int) = GameStateCommands.deleteFactionRelations(factionId)

    private fun validateDistinct(source: Int, target: Int, key: String) {
        if (source == target) fail(key)
    }

    private fun fail(key: String): Nothing = throw LocalizedException(key)
}
