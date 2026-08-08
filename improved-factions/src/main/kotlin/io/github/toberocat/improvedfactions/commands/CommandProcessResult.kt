package io.github.toberocat.improvedfactions.commands

import io.github.toberocat.improvedfactions.annotations.command.LocalizationKey
import io.github.toberocat.improvedfactions.translation.sendLocalized
import org.bukkit.command.CommandSender
import org.bukkit.Bukkit
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import java.util.UUID
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.translation.LocalizedException
import java.util.concurrent.CompletionStage

data class CommandProcessResult(
    val responseLocalizationKey: LocalizationKey,
    val args: Map<String, String>,
    val deferred: Boolean = false,
)

fun cancelledCommandResult() = CommandProcessResult("", emptyMap(), deferred = true)

fun CommandSender.sendCommandResult(result: CommandProcessResult) {
    sendLocalized(result.responseLocalizationKey, result.args)
}

fun <T> CommandSender.respondAfter(
    stage: CompletionStage<T>,
    response: (T) -> CommandProcessResult
): CommandProcessResult? {
    val senderRef = CommandSenderRef.capture(this)
    StorageManager.continueOnMain(stage, { value ->
        senderRef.resolve()?.sendCommandResult(response(value))
    }) { failure ->
        val sender = senderRef.resolve() ?: return@continueOnMain
        if (failure is LocalizedException) sender.sendLocalized(failure.key, failure.placeholders)
        else {
            io.github.toberocat.improvedfactions.modules.base.BaseModule.logger.warning(failure.message ?: failure.javaClass.name)
            sender.sendMessage(failure.message ?: "Storage operation failed")
        }
    }
    return CommandProcessResult("", emptyMap(), deferred = true)
}

private sealed interface CommandSenderRef {
    fun resolve(): CommandSender?

    data class PlayerRef(val uniqueId: UUID) : CommandSenderRef {
        override fun resolve() = Bukkit.getPlayer(uniqueId)
    }

    data object ConsoleRef : CommandSenderRef {
        override fun resolve() = Bukkit.getConsoleSender()
    }

    data class BlockRef(val world: String, val x: Int, val y: Int, val z: Int) : CommandSenderRef {
        override fun resolve() = Bukkit.getWorld(world)?.getBlockAt(x, y, z)?.state as? CommandSender
    }

    companion object {
        fun capture(sender: CommandSender): CommandSenderRef = when (sender) {
            is Player -> PlayerRef(sender.uniqueId)
            is ConsoleCommandSender -> ConsoleRef
            is BlockCommandSender -> BlockRef(
                sender.block.world.name,
                sender.block.x,
                sender.block.y,
                sender.block.z
            )
            else -> ConsoleRef
        }
    }
}
