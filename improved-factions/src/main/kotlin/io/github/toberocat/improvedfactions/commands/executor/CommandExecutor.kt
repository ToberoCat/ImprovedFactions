package io.github.toberocat.improvedfactions.commands.executor

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.annotations.localization.Localization
import io.github.toberocat.improvedfactions.commands.CommandProcessor
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.bukkit.OfflinePlayerArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.bukkit.PlayerArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.bukkit.WorldArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.faction.FactionArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.faction.FactionInviteArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.faction.FactionRankArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.faction.ZoneArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.primitives.BoolArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.primitives.IntArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.primitives.StringArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.primitives.enums.JoinTypeEnumArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.primitives.enums.PowerTypeArgumentParser
import io.github.toberocat.improvedfactions.commands.sendCommandResult
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.factions.FactionJoinType
import io.github.toberocat.improvedfactions.invites.FactionInvite
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.modules.power.PowerType
import io.github.toberocat.improvedfactions.ranks.FactionRank
import io.github.toberocat.improvedfactions.translation.LocalizedException
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.zone.Zone
import org.bukkit.OfflinePlayer
import org.bukkit.World
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

val DEFAULT_PARSERS = mapOf<Class<*>, ArgumentParser>(
    String::class.java to StringArgumentParser(),
    Int::class.java to IntArgumentParser(),
    Boolean::class.java to BoolArgumentParser(),
    Player::class.java to PlayerArgumentParser(),
    OfflinePlayer::class.java to OfflinePlayerArgumentParser(),
    Faction::class.java to FactionArgumentParser(),
    FactionJoinType::class.java to JoinTypeEnumArgumentParser(),
    Zone::class.java to ZoneArgumentParser(),
    World::class.java to WorldArgumentParser(),
    FactionInvite::class.java to FactionInviteArgumentParser(),
    FactionRank::class.java to FactionRankArgumentParser(),
    PowerType::class.java to PowerTypeArgumentParser()
)

open class CommandExecutor(private val plugin: ImprovedFactionsPlugin) : TabExecutor {

    private val commandProcessors = mutableMapOf<String, CommandProcessor>()
    private val tabCompletionTree = CompletionLevelNode()

    fun bindToPluginCommand(command: String) {
        val pluginCommand =
            plugin.getCommand(command) ?: throw IllegalArgumentException("Command $command not found")
        pluginCommand.tabCompleter = this
        pluginCommand.setExecutor(this)
    }


    fun registerCommandProcessor(processor: CommandProcessor) {
        if (commandProcessors.containsKey(processor.label)) {
            throw IllegalArgumentException("Command processor with label ${processor.label} already registered")
        }

        tabCompletionTree.insert(processor)
        commandProcessors[processor.label] = processor
        commandProcessors.entries.sortedByDescending { it.key.split(" ").size }
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        originalArgs: Array<String>,
    ): List<String> {
        if (BaseModule.config.maxCommandSuggestions == 0) {
            return emptyList()
        }

        val joinedCommand = createJoinedCommand(originalArgs)
        val processor = findProcessor(joinedCommand)
        if (processor == null) {
            return tabCompleteCommands(sender, originalArgs)
        }

        if (!processor.canExecute(sender, originalArgs)) {
            return emptyList()
        }

        val leftArgs = joinedCommand.replace(processor.label, "").trimStart()
        val argsArray = leftArgs.split(" ").toTypedArray()

        val rawResults = processor.tabComplete(sender, argsArray)
        val filteredResults = rawResults.filter { it.startsWith(originalArgs.last(), ignoreCase = true) }
        val limitedResults = filteredResults.take(BaseModule.config.maxCommandSuggestions)
        return limitedResults
    }

    @Localization("base.commands.unknown-command")
    @Localization("base.commands.cant-execute")
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        originalArgs: Array<String>,
    ): Boolean {
        val joinedCommand = createJoinedCommand(originalArgs)
        val processor = findProcessor(joinedCommand)
        if (processor == null) {
            sender.sendLocalized("base.commands.unknown-command")
            return false
        }

        if (!processor.canExecute(sender, originalArgs)) {
            sender.sendLocalized("base.commands.cant-execute")
            return false
        }

        val leftArgs = joinedCommand.replace(processor.label, "").trim()
        val args = leftArgs.split(" ").filter { it.isNotBlank() }.toTypedArray()

        val result = runCatching {
            processor.execute(sender, args)
        }.onFailure {
            if (it is LocalizedException) {
                sender.sendLocalized(it.key, it.placeholders)
            } else {
                plugin.logger.warning(
                    "The command ${processor.label} used a non localized exception - This is the outdated way of handling exceptions." +
                            " Exception Class: ${it.javaClass.name}. In the future, please use LocalizedException. In the meantime, this exception will be printed to the console."
                )
                it.printStackTrace()
                sender.sendMessage(it.message ?: "An error occurred")
            }
        }
            .getOrNull() ?: return false

        sender.sendCommandResult(result)
        return true
    }

    private fun tabCompleteCommands(sender: CommandSender, args: Array<String>): List<String> {
        return tabCompletionTree.getCompletions(args.toList())
    }

    private fun findProcessor(joinedCommand: String) =
        commandProcessors.entries.firstOrNull { joinedCommand.contains(it.key) }?.value

    private fun createJoinedCommand(args: Array<String>): String = args.joinToString(" ")
}