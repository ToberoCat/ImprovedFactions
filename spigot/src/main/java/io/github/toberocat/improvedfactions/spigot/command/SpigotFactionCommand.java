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

import java.util.List;

public class SpigotFactionCommand implements TabExecutor {

    private final BaseCommand baseCommand;
    private final SpigotCommandHandler handler;

    public SpigotFactionCommand() {
        this.baseCommand = new BaseCommand();
        this.handler = new SpigotCommandHandler(baseCommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {

        if (!(sender instanceof Player player)) return false;
        FactionPlayer<?> factionPlayer = ImprovedFactions.api().getPlayer(player.getUniqueId());
        System.out.println("exexu");
        if (factionPlayer == null) return false;

        io.github.toberocat.improvedFactions.core.command.component.Command cmd =
                handler.findCommand(String.join(" ", args));
        if (cmd == null) return false;

        System.out.println(cmd.label());
        io.github.toberocat.improvedFactions.core.command.component.Command.CommandPacket packet =
                cmd.createFromArgs(factionPlayer, args);
        if (packet == null) return false;

        cmd.run(packet);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                                @NotNull Command command,
                                                @NotNull String alias,
                                                @NotNull String[] args) {
        io.github.toberocat.improvedFactions.core.command.component.Command cmd =
                handler.findCommand(String.join(" ", args));
        if (cmd == null) return List.of();

        return cmd.tabComplete(args);
    }
}
