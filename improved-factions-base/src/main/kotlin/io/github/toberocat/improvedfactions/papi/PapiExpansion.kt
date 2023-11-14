package io.github.toberocat.improvedfactions.papi

import io.github.toberocat.improvedfactions.user.factionUser
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player


/**
 * Created: 20.07.2023
 * @author Tobias Madlberger (Tobias)
 */

class PapiExpansion : PlaceholderExpansion() {
    private val placeholders = HashMap<String, (player: Player) -> String?>()

    init {
        placeholders["owner"] = { it.factionUser().faction()?.owner?.let { uuid ->
            Bukkit.getOfflinePlayer(uuid) }?.name }
        placeholders["name"] = { it.factionUser().faction()?.name }
        placeholders["rank"] = { it.factionUser().rank().name }
        placeholders["power"] = { it.factionUser().faction()?.accumulatedPower?.toString() }
        placeholders["maxPower"] = { it.factionUser().faction()?.maxPower?.toString() }
    }

    override fun getAuthor(): String = "Tobero"

    override fun getIdentifier(): String = "faction"

    override fun getVersion(): String = "1.0.0"

    override fun getRequiredPlugin(): String = "ImprovedFaction"

    override fun persist(): Boolean = true

    override fun onRequest(player: OfflinePlayer?, params: String) =
        player?.player?.let { placeholders[params]?.invoke(it) }
}
