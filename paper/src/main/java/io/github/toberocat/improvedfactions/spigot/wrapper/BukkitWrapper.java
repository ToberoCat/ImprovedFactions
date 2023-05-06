package io.github.toberocat.improvedfactions.spigot.wrapper;

import io.github.toberocat.improvedFactions.core.player.CommandSender;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class BukkitWrapper {
    public static @NotNull CommandSender wrap(@NotNull org.bukkit.command.CommandSender sender) {
        return new CommandSender() {
            @Override
            public void runCommand(@NotNull String command) {
                Bukkit.dispatchCommand(sender, command);
            }

            @Override
            public boolean hasPermission(@NotNull String permission) {
                return sender.hasPermission(permission);
            }

            @Override
            public void sendMessage(@NotNull String message) {
                sender.sendMessage(message);
            }

            @Override
            public String getName() {
                return sender.getName();
            }
        };
    }
}
