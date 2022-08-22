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

    private final SpigotCommandHandler handler;

    public SpigotFactionCommand() {
        this.handler = new SpigotCommandHandler(new BaseCommand());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {
        if (sender instanceof Player player) {
            FactionPlayer<?> factionPlayer = ImprovedFactions.api().getPlayer(player.getUniqueId());
            if (factionPlayer == null) return false;
            return handler.executeCommandChain(factionPlayer, args);
        } else return handler.executeCommandChain(args);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                                @NotNull Command command,
                                                @NotNull String alias,
                                                @NotNull String[] args) {
        return handler.tabCommandChain(sender, args);
    }
}
