package io.github.toberocat.improvedfactions.utils

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.annotations.CommandMeta
import io.github.toberocat.improvedfactions.utils.offline.KnownOfflinePlayer
import io.github.toberocat.improvedfactions.utils.offline.KnownOfflinePlayers
import io.github.toberocat.toberocore.command.SubCommand
import net.kyori.adventure.audience.Audience
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*
import java.nio.charset.StandardCharsets;
import kotlin.reflect.full.findAnnotations

inline fun <T, R> T.compute(computeBlock: (T) -> R) = computeBlock(this)

fun Player.toAudience(): Audience = ImprovedFactionsPlugin.instance.adventure.player(this)

fun UUID.toOfflinePlayer(): OfflinePlayer = Bukkit.getOfflinePlayer(this)

fun SubCommand.getMeta(): CommandMeta? = this::class.findAnnotations(CommandMeta::class).firstOrNull()

fun String.hasOfflinePlayerByName() = loggedTransaction {
    KnownOfflinePlayer.count(KnownOfflinePlayers.name eq this@hasOfflinePlayerByName) > 0
}

fun String.getOfflinePlayerByName() = loggedTransaction {
    val knownPlayerUUID = KnownOfflinePlayer.find { KnownOfflinePlayers.name eq this@getOfflinePlayerByName }
        .firstOrNull()?.id?.value;
    return@loggedTransaction if (knownPlayerUUID?.let { Bukkit.getOfflinePlayer(it).name } != null) {
        ImprovedFactionsPlugin.instance.logger.fine("[OfflinePlayers] KnownPlayerUUID found.")
        Bukkit.getOfflinePlayer(knownPlayerUUID);
    } else { /* Fallback to Bukkit API if provided UUID is incorrect thus returning null. */
        ImprovedFactionsPlugin.instance.logger.fine("[OfflinePlayers] KnownPlayerUUID null, fell back to Bukkit " + knownPlayerUUID.toString())
        Bukkit.getOfflinePlayers().find { it.name == this@getOfflinePlayerByName };
    }
}
