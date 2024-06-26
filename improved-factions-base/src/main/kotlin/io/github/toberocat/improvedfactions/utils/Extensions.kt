package io.github.toberocat.improvedfactions.utils

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.toberocore.command.SubCommand
import net.kyori.adventure.audience.Audience
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.full.findAnnotations

fun <T, R> T.compute(computeBlock: (T) -> R): R {
    return computeBlock(this)
}

fun Player.toAudience(): Audience = ImprovedFactionsPlugin.instance.adventure.player(this)

fun UUID.toOfflinePlayer(): OfflinePlayer = Bukkit.getOfflinePlayer(this)

fun SubCommand.getMeta(): CommandMeta? = this::class.findAnnotations(CommandMeta::class).firstOrNull()