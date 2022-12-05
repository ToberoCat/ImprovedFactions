package io.github.toberocat.improvedfactions.spigot.command.component;

import io.github.toberocat.improvedFactions.core.command.component.Command;
import io.github.toberocat.improvedFactions.core.command.component.CommandSettings;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedfactions.spigot.command.worldguard.ScanRegionsToZone;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SpigotCommandHandler {

    private final Map<String, Command<?, ?>> lookup;
    private final Command<?, ?> base;

    public SpigotCommandHandler(@NotNull Command<?, ?> base) {
        lookup = new HashMap<>();
        this.base = base;

        //if (Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) ToDo: Fix the scan command
            //base.getCommands().get("zones").getCommands().put("scanWg", new ScanRegionsToZone());

        call(base);
    }

    private void call(@NotNull Command<?, ?> command) {
        lookup.putAll(command.getCommands());
        command.getCommands()
                .values()
                .forEach(this::call);
    }

    public @NotNull SearchResult findCommand(@NotNull String query) {
        String[] split = query.split(" ");
        for (int i = split.length - 1; i >= 0; i--) {
            Command<?, ?> cmd = lookup.get(split[i]);
            if (cmd != null) return new SearchResult(cmd, i);
        }

        return new SearchResult(base, 0);
    }

    public boolean executeCommandChain(@NotNull FactionPlayer<?> player, @NotNull String[] args) {
        SpigotCommandHandler.SearchResult result = findCommand(String.join(" ", args));
        if (args.length == 0)
            return runCommand((Command<Command.CommandPacket, ?>) base, player, new String[0]);


        args = Arrays.copyOfRange(args, result.index() + 1, args.length);

        Command<Command.CommandPacket, ?> cmd = (Command<Command.CommandPacket, ?>) result.command();

        return runCommand(cmd, player, args);
    }

    private boolean runCommand(Command<Command.CommandPacket, ?> cmd, FactionPlayer<?> player, String[] args) {
        CommandSettings.SettingResult query = cmd.settings().canExecute(player);
        if (!query.result()) {
            if (query.errorMessage() != null) player.sendTranslatable(query.errorMessage());
            return false;
        }

        Command.CommandPacket packet = cmd.createFromArgs(player, args);
        if (packet == null) return false;

        cmd.run(packet);
        return true;
    }

    public boolean executeCommandChain(@NotNull String[] args) {
        SpigotCommandHandler.SearchResult result = findCommand(String.join(" ", args));
        if (args.length == 0) return false;

        args = Arrays.copyOfRange(args, result.index() + 1, args.length);

        Command<?, Command.ConsoleCommandPacket> cmd = (Command<?, Command.ConsoleCommandPacket>)
                result.command();

        if (!cmd.settings().canExecuteConsole()) return false;

        Command.ConsoleCommandPacket packet = cmd.createFromArgs(args);

        if (packet == null) return false;

        cmd.runConsole(packet);
        return true;
    }

    public @NotNull List<String> tabCommandChain(@NotNull CommandSender sender,
                                                 @NotNull String[] args) {
        SearchResult result = findCommand(String.join(" ", args));
        args = Arrays.copyOfRange(args, result.index() + 1, args.length);

        Command<?, ?> command = result.command;

        if (sender instanceof Player player) {
            FactionPlayer<?> executor = ImprovedFactions.api().getPlayer(player.getUniqueId());
            if (executor == null) return Collections.emptyList();

            if (!command.settings().showTab(executor)) return Collections.emptyList();

            List<String> autoTabs = command.getCommands().values()
                    .stream()
                    .filter(x -> x.settings().showTab(executor))
                    .map(Command::label)
                    .toList();
            return autoTabs.size() == 0 ? command.tabCompletePlayer(executor, args) : autoTabs;
        }

        if (!command.settings().showTabConsole()) return Collections.emptyList();

        List<String> commands = command.tabCompleteConsole(args);

        // Sort results
        List<String> results = new ArrayList<>();
        for (String arg : args)
            for (String a : commands)
                if (a.toLowerCase().startsWith(arg.toLowerCase()))
                    results.add(a);

        return results;
    }

    public static record SearchResult(@NotNull Command<?, ?> command, int index) {

    }
}
