package io.github.toberocat.improvedfactions.spigot.command.component;

import io.github.toberocat.improvedFactions.core.command.component.Command;
import io.github.toberocat.improvedFactions.core.command.component.CommandSettings;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SpigotCommandHandler {

    private final Map<String, Command<?, ?>> lookup;
    private final Command<?, ?> base;

    public SpigotCommandHandler(@NotNull Command<?, ?> base) {
        this.lookup = new HashMap<>();
        this.base = base;

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

        return new SearchResult(null, 0);
    }

    public boolean executeCommandChain(@NotNull FactionPlayer<?> player, @NotNull String[] args) {
        SpigotCommandHandler.SearchResult result = findCommand(String.join(" ", args));
        if (args.length == 0) return false;

        args = Arrays.copyOfRange(args, result.index() + 1, args.length);

        Command<Command.CommandPacket, ?> cmd = (Command<Command.CommandPacket, ?>) result.command();
        if (cmd == null) {
            player.sendTranslatable(translatable -> translatable
                    .getMessages()
                    .getCommand()
                    .get("command-settings")
                    .get("no-command-found"));
            return false;
        }

        CommandSettings.SettingResult query = cmd.readSettings().canExecute(player);
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
        if (cmd == null) return false;


        if (!cmd.readSettings().canExecuteConsole()) return false;

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
        if (command == null) command = base;

        if (sender instanceof Player player) {
            FactionPlayer<?> executor = ImprovedFactions.api().getPlayer(player.getUniqueId());
            if (executor == null) return Collections.emptyList();

            if (!command.readSettings().showTab(executor)) return Collections.emptyList();

            List<String> autoTabs = command.getCommands().values()
                    .stream()
                    .filter(x -> x.readSettings().showTab(executor))
                    .map(Command::label)
                    .toList();
            return autoTabs.size() == 0 ? command.tabCompletePlayer(executor, args) : autoTabs;
        }

        if (!command.readSettings().showTabConsole()) return Collections.emptyList();

        return command.tabCompleteConsole(args);
    }

    public static record SearchResult(@Nullable Command<?, ?> command, int index) {

    }
}
