package io.github.toberocat.improvedfactions.factions

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.annotations.localization.Localization
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.messages.MessageBroker
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.ranks.FactionRank
import io.github.toberocat.improvedfactions.ranks.FactionRankHandler
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.toberocore.util.ItemBuilder
import org.bukkit.Bukkit
import org.bukkit.Material
import org.jetbrains.exposed.sql.SizedIterable
import java.util.*

/**
 * Created: 04.08.2023
 * @author Tobias Madlberger (Tobias)
 */
object FactionHandler {
    @Localization("factions.faction-already-exists")
    @Localization("factions.already-in-faction")
    fun createFaction(ownerId: UUID, factionName: String, id: Int? = null): Faction {
        val faction = Faction.new(id) {
            owner = ownerId
            localName = factionName
            icon = ItemBuilder().title("§e$factionName").material(Material.WOODEN_SWORD)
                .create(ImprovedFactionsPlugin.instance)
        }
        createListenersFor(faction)

        val ranks = createDefaultRanks(faction.id.value)

        faction.defaultRank = ranks.firstOrNull()?.id?.value ?: FactionRankHandler.guestRankId
        faction.join(ownerId, ranks.lastOrNull()?.id?.value ?: FactionRankHandler.guestRankId)
        faction.setAccumulatedPower(faction.maxPower, PowerAccumulationChangeReason.PASSIVE_ENERGY_ACCUMULATION)
        return faction
    }

    fun generateColor(id: Int) = Integer.parseInt(
        Integer.toHexString("${id}-${Bukkit.getServer().name}".hashCode())
            .padStart(6, '0')
            .substring(0, 6), 16
    )

    fun getFactions(): SizedIterable<Faction> {
        return loggedTransaction { return@loggedTransaction Faction.all() }
    }

    fun getFaction(id: Int): Faction? {
        return loggedTransaction { return@loggedTransaction Faction.findById(id) }
    }

    fun getFaction(name: String): Faction? {
        return loggedTransaction { return@loggedTransaction Faction.find { Factions.name eq name }.firstOrNull() }
    }

    fun searchFactions(name: String): SizedIterable<Faction> {
        return loggedTransaction { return@loggedTransaction Faction.find { Factions.name like name } }
    }

    fun createListenersFor(faction: Faction) {
        MessageBroker.listenLocalized(faction.id.value) { message ->
            val members = loggedTransaction { faction.members().mapNotNull { Bukkit.getPlayer(it.uniqueId) } }
            members.forEach { it.sendLocalized(message.key, message.placeholders) }
        }
    }

    private fun createDefaultRanks(factionId: Int): List<FactionRank> {
        val section = ImprovedFactionsPlugin.instance.config.getConfigurationSection("factions.default-faction-ranks")
        if (section == null) {
            ImprovedFactionsPlugin.instance.logger.warning("Couldn't read default faction ranks. Fix it immediately")
            return listOf(
                FactionRankHandler.createRank(
                    factionId, "Member", 1,
                    setOf(Permissions.SEND_INVITES)
                ),
                FactionRankHandler.createRank(
                    factionId, "Owner", 1000,
                    Permissions.knownPermissions.keys
                )
            )
        }

        return section.getKeys(false).map {
            FactionRankHandler.createRank(
                factionId, it, section.getInt("$it.priority"),
                section.getStringList("$it.default-permissions")
            )
        }
    }
}