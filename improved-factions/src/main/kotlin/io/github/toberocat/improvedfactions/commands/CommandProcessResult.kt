package io.github.toberocat.improvedfactions.commands

import io.github.toberocat.improvedfactions.annotations.command.LocalizationKey
import io.github.toberocat.improvedfactions.translation.sendLocalized
import org.bukkit.command.CommandSender

data class CommandProcessResult(
    val responseLocalizationKey: LocalizationKey,
    val args: Map<String, String>,
)

fun CommandSender.sendCommandResult(result: CommandProcessResult) {
    sendLocalized(result.responseLocalizationKey, result.args)
}