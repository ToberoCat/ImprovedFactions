package io.github.toberocat.improvedfactions.commands

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.translation.localizeUnformatted
import io.github.toberocat.improvedfactions.utils.getMeta
import io.github.toberocat.toberocore.command.Command
import io.github.toberocat.toberocore.command.CommandExecutor
import io.github.toberocat.toberocore.command.SubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.command.CommandSender
import java.util.*

data class CommandIndexEntry(
    val qualifiedName: String,
    val permission: String,
    val commandUsage: String,
    val args: Map<String, String>,
    val englishDescription: String,
)
typealias CategoryIndex = MutableMap<String, MutableList<CommandIndexEntry>>

// ToDo: Maybe make this a kotlin code generator instead of a command
class GenerateWikiSourcesCommand(
    private val plugin: ImprovedFactionsPlugin,
    private val executor: CommandExecutor
) : SubCommand("generateWikiSources") {

    private val categoryIndex: CategoryIndex = mutableMapOf()

    override fun options() = Options.getFromConfig(plugin, label)

    override fun arguments(): Array<Argument<*>> = emptyArray()

    private fun indexCommand(command: Command): Unit = command.children.forEach { (label, subCommand) ->
        val meta = subCommand.getMeta()
        if (meta == null) {
            plugin.logger.warning(
                "Command $label sub command of ${command.label} has a no command meta attached to it. The command will still work, but it will be excluded from the wiki pages"
            )
            return@forEach
        }

        val qualifiedName = subCommand.permission.replace(".", " ")
        val args = subCommand.args.joinToString(" ") { it.usage() }
        val category = Locale.ENGLISH.localizeUnformatted(meta.category, emptyMap())
        val commandIndex = categoryIndex[category] ?: mutableListOf()
        commandIndex.add(CommandIndexEntry(
            qualifiedName = qualifiedName,
            permission = subCommand.permission,
            englishDescription = Locale.ENGLISH.localizeUnformatted(meta.description, emptyMap()),
            commandUsage = "/$qualifiedName $args",
            args = subCommand.args.associate {
                it.usage() to Locale.ENGLISH.localizeUnformatted(
                    it.descriptionKey(),
                    emptyMap()
                )
            }
        ))

        categoryIndex[category] = commandIndex
        indexCommand(subCommand)
    }

    private fun generateWikiSources() {
        indexCommand(executor)
        categoryIndex.forEach { (category, commands) ->
            val categoryFileName = category.replace(" ", "_")
            val categoryFile = plugin.dataFolder.resolve("wiki/commands/$categoryFileName-commands.md")
            categoryFile.parentFile.mkdirs()
            categoryFile.writeText("# $category\n\n")

            commands.forEach { command ->
                categoryFile.appendText(
                    """
                    ## ${command.qualifiedName}
                    
                    Permission: ${command.permission}
                    Usage: `${command.commandUsage}`
                    Description: ${command.englishDescription}
                    
                    | Argument | Description | Required |
                    | --- | --- | --- |
                """.trimIndent()
                )

                command.args.forEach { (arg, description) ->
                    categoryFile.appendText(
                        "| ${
                            arg.replace(
                                "[^A-Za-z]".toRegex(),
                                ""
                            )
                        } | $description | ${arg.startsWith("<")} |\n"
                    )
                }
            }
        }

        val indexFile = plugin.dataFolder.resolve("wiki/commands/index.md")
        indexFile.parentFile.mkdirs()
        indexFile.writeText(
            """
            # Command Index
            
            ## Categories:
            
            
        """.trimIndent()
        )
        categoryIndex.forEach { (category, _) ->
            val categoryFileName = category.replace(" ", "_")
            indexFile.appendText("- [${category}category]($categoryFileName-commands.md)\n")
        }

        indexFile.appendText("""
            ## Commands:
            
            | Command | Description |
            | --- | --- |
            
        """.trimIndent())

        categoryIndex.forEach { (category, commands) ->
            val categoryFileName = category.replace(" ", "_")
            commands.forEach { command ->
                indexFile.appendText(
                    "| [${command.qualifiedName}]($categoryFileName-command.md#${command.qualifiedName}) | ${command.englishDescription} |\n"
                )
            }
        }
        categoryIndex.clear()
    }

    override fun handleCommand(sender: CommandSender, args: Array<String>): Boolean {
        if (!sender.isOp) {
            sender.sendMessage("You have to be op for this command")
            return false
        }

        sender.sendMessage("Generating wiki sources...")
        generateWikiSources()
        sender.sendMessage("Done")
        return true
    }

    override fun showInTab(sender: CommandSender, args: Array<out String>) = false
}