package io.github.toberocat.improvedfactions.commands.admin

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.annotations.command.PermissionConfig
import io.github.toberocat.improvedfactions.annotations.permission.PermissionConfigurations
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import java.util.ResourceBundle
import org.bukkit.command.CommandSender

@PermissionConfig(config = PermissionConfigurations.OP_ONLY)
@GeneratedCommandMeta(
    label = "admin reload",
    category = CommandCategory.ADMIN_CATEGORY,
    module = "base",
    responses = [
        CommandResponse("reloadCompleted"),
        CommandResponse("reloadFailed")
    ]
)
abstract class ReloadCommand(private val plugin: ImprovedFactionsPlugin) : ReloadCommandContext() {

    fun process(sender: CommandSender): CommandProcessResult {
        return try {
            plugin.reloadConfig()
            plugin.moduleManager.reloadModuleConfigs()
            ResourceBundle.clearCache()

            reloadCompleted()
        } catch (e: Exception) {
            reloadFailed("error" to (e.message ?: "Unknown error"))
        }
    }
}