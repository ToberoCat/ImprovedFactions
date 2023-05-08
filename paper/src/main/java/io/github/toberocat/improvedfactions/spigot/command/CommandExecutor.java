package io.github.toberocat.improvedfactions.spigot.command;

import io.github.toberocat.improvedFactions.core.utils.command.Command;
import io.github.toberocat.improvedFactions.core.utils.command.SubCommand;
import io.github.toberocat.improvedFactions.core.utils.command.exceptions.CommandException;
import io.github.toberocat.improvedfactions.spigot.wrapper.BukkitWrapper;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CommandExecutor extends Command implements TabExecutor {
    private CommandExecutor(@NotNull PluginCommand command) {
        super(command.getLabel(), command.getLabel());

        command.setExecutor(this);
        command.setTabCompleter(this);
    }

    public static @NotNull CommandExecutor createExecutor(@NotNull String command) {
        PluginCommand pluginCommand = Bukkit.getPluginCommand(command);
        if (pluginCommand == null)
            throw new RuntimeException("Plugin Command " + command + " is null");

        return new CommandExecutor(pluginCommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull org.bukkit.command.Command command,
                             @NotNull String label,
                             @NotNull String[] args) {
        SubCommand sub = args.length == 0 ? null : children.get(args[0]);
        if (sub == null)
            return false;

        io.github.toberocat.improvedFactions.core.player.CommandSender commandSender = BukkitWrapper.wrap(sender);
        try {
            return sub.routeCall(commandSender, args);
        } catch (CommandException e) {
            commandSender.sendException(e);
            return false;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                                @NotNull org.bukkit.command.Command command,
                                                @NotNull String label,
                                                @NotNull String[] args) {
        System.out.println("Calling");
        List<String> unsorted = null;
        io.github.toberocat.improvedFactions.core.player.CommandSender commandSender = BukkitWrapper.wrap(sender);
        try {
            unsorted = getTab(commandSender, args);
        } catch (CommandException e) {
            commandSender.sendException(e);
        }

        if (unsorted == null)
            return Collections.emptyList();

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

    private @Nullable List<String> getTab(@NotNull io.github.toberocat.improvedFactions.core.player.CommandSender sender,
                                          @NotNull String[] args)
            throws CommandException {
        if (args.length <= 1) return childrenTabList(sender, args);

        SubCommand sub = children.get(args[0]);

        return sub == null ? null : sub.routeTab(sender, args);
    }

    @Override
    public boolean showInTab(io.github.toberocat.improvedFactions.core.player.@NotNull CommandSender sender,
                             @NotNull String[] args) {
        return true;
    }
}
