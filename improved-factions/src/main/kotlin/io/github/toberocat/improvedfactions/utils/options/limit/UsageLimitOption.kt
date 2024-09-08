package io.github.toberocat.improvedfactions.utils.options.limit

import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.toberocore.command.exceptions.CommandException
import io.github.toberocat.toberocore.command.options.Option
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.and

class UsageLimitOption(
    private val limit: Int,
    private val registry: String,
    private val byPassPermissions: String = "factions.bypass.usage-limits.$registry"
) : Option {
    override fun execute(sender: CommandSender, args: Array<String>) = loggedTransaction {
        return@loggedTransaction when {
            limit < 0 -> args
            sender.hasPermission(byPassPermissions) -> args
            sender !is Player -> throw CommandException("base.exceptions.sender-must-be-player", emptyMap())
            else -> {
                val entry = PlayerUsageLimits.getUsageLimit(registry, sender.uniqueId)
                if (entry.used >= limit) {
                    throw CommandException(
                        "base.exceptions.command-limit-reached",
                        mapOf("limit" to limit.toString())
                    )
                } else {
                    entry.used = entry.used.plus(1)
                    sender.sendLocalized(
                        "base.messages.command-usage-limit",
                        mapOf("left" to (limit - entry.used).toString())
                    )
                    args
                }
            }
        }
    }
}