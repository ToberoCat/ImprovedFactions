package io.github.toberocat.improvedfactions.commands.general

import BuildConfig
import io.github.toberocat.improvedfactions.annotations.CommandCategory
import io.github.toberocat.improvedfactions.annotations.CommandResponse
import io.github.toberocat.improvedfactions.annotations.GeneratedCommandMeta
import org.bukkit.command.CommandSender

@GeneratedCommandMeta(
    label = "version",
    module = "base",
    responses = [
        CommandResponse("pluginVersion"),
    ],
    category = CommandCategory.GENERAL_CATEGORY
)
abstract class VersionCommand : VersionCommandContext() {
    fun process(sender: CommandSender) = pluginVersion("version" to BuildConfig.VERSION)
}