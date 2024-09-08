package io.github.toberocat.improvedfactions.claims.clustering.cluster

interface AdditionalClusterType {
    fun scheduleUpdate()
    fun getColor(): Int?
    fun delete()
}