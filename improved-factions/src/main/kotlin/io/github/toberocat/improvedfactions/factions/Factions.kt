package io.github.toberocat.improvedfactions.factions

import dev.s7a.base64.Base64ItemStack
import io.github.toberocat.improvedfactions.ranks.FactionRankHandler
import io.github.toberocat.improvedfactions.translation.LocalizationKey
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * Created: 04.08.2023
 * @author Tobias Madlberger (Tobias)
 */
object Factions : IntIdTable("factions") {
    var maxNameLength = 36
    var maxSpacesInName = 5
    var nameRegex = Regex("[a-zA-Z ]*")
    var maxIconLength = 5000

    val name = varchar("name", maxNameLength)
    val owner = uuid("owner")
    val accumulatedPower = integer("accumulated_power").default(0)
    val maxPower = integer("max_power").default(50)
    val defaultRank = integer("default_rank").default(FactionRankHandler.guestRankId)
    val base64Icon = varchar("icon_base64", maxIconLength)
        .default(Base64ItemStack.encode(ItemStack(Material.WOODEN_SWORD)))
    val factionJoinType = enumeration("join_type", FactionJoinType::class)
        .default(FactionJoinType.INVITE_ONLY)

    fun handleQueues() {
        Faction.all().forEach { FactionHandler.createListenersFor(it) }
    }
}

data class LocalizedMessage(val key: LocalizationKey, val placeholders: Map<String, String>)