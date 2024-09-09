package io.github.toberocat.improvedfactions.modules.relations.handles

interface RelationsModuleHandle {
    fun getAlliedFactions(factionId: Int): Set<Int>
    fun getEnemyFactions(factionId: Int): Set<Int>
    fun isAllied(factionId: Int, targetFactionId: Int): Boolean
    fun isEnemy(factionId: Int, targetFactionId: Int): Boolean

    fun inviteToAlliance(factionId: Int, targetFactionId: Int)
    fun acceptAlliance(factionId: Int, targetFactionId: Int)
    fun declareWar(factionId: Int, targetFactionId: Int)
    fun breakAlliance(factionId: Int, targetFactionId: Int)
    fun makePeace(factionId: Int, targetFactionId: Int)
    fun deleteFactionRelations(factionId: Int)
}