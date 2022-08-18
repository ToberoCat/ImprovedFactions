package io.github.toberocat.improvedfactions.spigot.command;

import io.github.toberocat.improvedFactions.core.command.BaseCommand;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.sender.player.FactionPlayer;
import io.github.toberocat.improvedfactions.spigot.command.component.SpigotCommandHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class SpigotFactionCommand implements TabExecutor {

    private final SpigotCommandHandler handler;

    public SpigotFactionCommand() {
        this.handler = new SpigotCommandHandler(new BaseCommand());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {

        if (!(sender instanceof Player player)) return false;
        FactionPlayer<?> factionPlayer = ImprovedFactions.api().getPlayer(player.getUniqueId());
        if (factionPlayer == null) return false;

        SpigotCommandHandler.SearchResult result = handler.findCommand(String.join(" ", args));
        args = Arrays.copyOfRange(args, result.index() + 1, args.length);

        io.github.toberocat.improvedFactions.core.command.component.Command
                <io.github.toberocat.improvedFactions.core.command.component.Command.CommandPacket>
                cmd = (io.github.toberocat.improvedFactions.core.command.component.Command
                <io.github.toberocat.improvedFactions.core.command.component.Command.CommandPacket>)
                result.command();

        io.github.toberocat.improvedFactions.core.command.component.Command.CommandPacket packet =
                result.command().createFromArgs(factionPlayer, args);

        if (packet == null) return false;

        cmd.run(packet);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                                @NotNull Command command,
                                                @NotNull String alias,
                                                @NotNull String[] args) {
        SpigotCommandHandler.SearchResult result = handler.findCommand(String.join(" ", args));
        args = Arrays.copyOfRange(args, result.index() + 1, args.length);

        if (sender instanceof Player player) {
            FactionPlayer<?> executor = ImprovedFactions.api().getPlayer(player.getUniqueId());
            if (executor == null) return null;

            return result.command().tabCompletePlayer(executor, args);
        }

        return result.command().tabCompleteConsole(args);
    }
}
