package io.github.toberocat.improvedfactions.spigot.command;

import io.github.toberocat.improvedFactions.core.utils.command.Command;
import io.github.toberocat.improvedFactions.core.utils.command.SubCommand;
import io.github.toberocat.improvedFactions.core.utils.command.exceptions.CommandException;
import io.github.toberocat.improvedFactions.core.handler.message.MessageHandler;
import io.github.toberocat.improvedfactions.spigot.wrapper.BukkitWrapper;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class CommandExecutor extends Command implements TabExecutor {
    private CommandExecutor(@NotNull PluginCommand command, @NotNull String prefix) {
        super(command.getLabel(), command.getLabel());
        this.prefix = prefix;

        command.setExecutor(this);
        command.setTabCompleter(this);
    }

    public static @NotNull CommandExecutor createExecutor(
            @NotNull FileConfiguration config,
            @NotNull String command) {
        return createExecutor(config.getString("prefix", "§8[§eIF§8]§7"), command);
    }

    public static @NotNull CommandExecutor createExecutor(
            @NotNull String prefix,
            @NotNull String command) {
        PluginCommand pluginCommand = Bukkit.getPluginCommand(command);
        if (pluginCommand == null)
            throw new RuntimeException("Plugin Command " + command + " is null");

        return new CommandExecutor(pluginCommand, prefix);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull org.bukkit.command.Command command,
                             @NotNull String label,
                             @NotNull String[] args) {
        SubCommand sub = args.length == 0 ? null : children.get(args[0]);
        if (sub == null)
            return false;

        try {
            return sub.routeCall(BukkitWrapper.wrap(sender), args);
        } catch (CommandException e) {
            sender.sendMessage(prefix + "§c" + MessageHandler.api().format(e.getMessageId()));
            return false;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                                @NotNull org.bukkit.command.Command command,
                                                @NotNull String label,
                                                @NotNull String[] args) {
        List<String> unsorted = null;
        try {
            unsorted = getTab(sender, args);
        } catch (CommandException e) {
            sender.sendMessage(prefix + "§c" + MessageHandler.api().format(e.getMessageId()));
        }

        if (unsorted == null)
            return null;

        List<String> results = new ArrayList<>();
        for (String arg : args) {
            for (String a : unsorted) {
                if (a.toLowerCase().startsWith(arg.toLowerCase())) {
                    results.add(a);
                }
            }
        }

        return results;
    }

    private @Nullable List<String> getTab(@NotNull CommandSender sender, @NotNull String[] args)
            throws CommandException {
        if (args.length <= 1) return tabComplete;

        SubCommand sub = children.get(args[0]);

        return sub == null ? null : sub.routeTab(BukkitWrapper.wrap(sender), args);
    }
}
