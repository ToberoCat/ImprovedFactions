package io.github.toberocat.improvedfactions.utils

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.annotations.command.CommandMeta
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessor
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.utils.offline.KnownOfflinePlayer
import io.github.toberocat.improvedfactions.utils.offline.KnownOfflinePlayers
import io.github.toberocat.toberocore.command.SubCommand
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.TextComponent
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*
import kotlin.reflect.full.findAnnotations

inline fun <T, R> T.compute(computeBlock: (T) -> R) = computeBlock(this)

fun Player.toAudience(): Audience = BaseModule.adventure.player(this)

fun UUID.toOfflinePlayer(): OfflinePlayer = Bukkit.getOfflinePlayer(this)

fun SubCommand.getMeta(): CommandMeta? = this::class.findAnnotations(CommandMeta::class).firstOrNull()

fun CommandProcessor.getMeta(): GeneratedCommandMeta? = this::class.findAnnotations(GeneratedCommandMeta::class).firstOrNull()


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

fun TextComponent.appendIf(condition: Boolean, component: () -> TextComponent): TextComponent =
    if (condition) append(component) else this

fun String.camlCaseToSnakeCase(separator: String = "_") =
    replace(Regex("([a-z])([A-Z]+)"), "$1${separator}$2").lowercase()