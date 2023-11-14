package io.github.toberocat.improvedfactions.commands

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.translation.getLocalized
import io.github.toberocat.improvedfactions.translation.getUnformattedLocalized
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.utils.arguments.StringArgument
import io.github.toberocat.improvedfactions.utils.async
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.getMeta
import io.github.toberocat.improvedfactions.utils.toAudience
import io.github.toberocat.toberocore.command.Command
import io.github.toberocat.toberocore.command.CommandExecutor
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.SubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.exceptions.CommandException
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

@CommandMeta(
    description = "base.command.help.description"
)
class HelpCommand(
    private val plugin: ImprovedFactionsPlugin, private val executor: CommandExecutor
) : PlayerSubCommand("help") {

    private val categoryIndex: MutableMap<String, MutableList<String>>
    private val commandMetaIndex: MutableMap<String, SubCommand>
    private val commands: List<String>

    init {
        categoryIndex = HashMap()
        commandMetaIndex = HashMap()
        indexCommand(executor)

        commands = commandMetaIndex.keys.toList()
    }

    private fun indexCommand(command: Command): Unit = command.children.forEach { (label, subCommand) ->
        val meta = subCommand.getMeta()
        if (meta == null) {
            plugin.logger.warning(
                "Command $label sub command of ${command.label} has a no command meta " + "attached to it. The command will still work, but it will be excluded from the help menu"
            )
            return@forEach
        }

        val labels = categoryIndex[meta.category] ?: mutableListOf()
        labels.add(subCommand.permission)
        categoryIndex[meta.category] = labels

        commandMetaIndex[subCommand.permission] = subCommand
        indexCommand(subCommand)
    }

    override fun options(): Options = Options.getFromConfig(plugin, label)

    override fun arguments(): Array<Argument<*>> = arrayOf(
        StringArgument("[command]", "base.command.args.help")
    )

    override fun handle(player: Player, args: Array<out String>): Boolean {
        val command = parseArgs(player, args).get<String>(0)
        if (command == null) {
            printCategoryOverview(player)
            return true
        }

        when {
            command.startsWith("category:") ->
                printCategoryDetails(player, command.replace("category:", ""))
            else -> printCommandDetails(
                player, executor.getChild(command) ?: throw CommandException(
                    "base.command.help.command-not-found", emptyMap()
                )
            )
        }
        return true
    }

    private fun printCategoryOverview(player: Player) {
        val audience = player.toAudience()
        audience.sendMessage(player.getLocalized("base.command.help.header"))
        for (category in categoryIndex.keys) audience.sendMessage(
            player.getLocalized(
                "base.command.help.category-overview", mapOf(
                    "category-name" to player.getUnformattedLocalized(category), "category-id" to category
                )
            )
        )
    }

    private fun printCommandDetails(player: Player, subCommand: SubCommand) {
        async {
            val meta = subCommand.getMeta() ?: return@async
            val baseCommand = subCommand.permission.replace(".", " ")
            val args = subCommand.args.joinToString (" ") {
                "<hover:show_text:'${player.getUnformattedLocalized(it.descriptionKey())}'>" +
                        "<${if (it.usage().startsWith("<")) "aqua" else "gold"}>${it.usage()}</hover>"
            }

            val rawArgs = subCommand.args.joinToString (" ") { it.usage() }

            val usage = "/$baseCommand $args".trim()
            val cmd = "/$baseCommand $rawArgs".trim()
            player.sendLocalized(
                "base.command.help.command-details", mapOf(
                    "usage" to usage, "description" to player.getUnformattedLocalized(meta.description),
                    "cmd" to cmd
                )
            )
        }
    }

    private fun printCategoryDetails(player: Player, category: String) {
        player.sendLocalized("base.command.help.header")
        categoryIndex[category]?.mapNotNull {
            commandMetaIndex[it]
        }?.forEach { printCommandDetails(player, it) }
    }
}