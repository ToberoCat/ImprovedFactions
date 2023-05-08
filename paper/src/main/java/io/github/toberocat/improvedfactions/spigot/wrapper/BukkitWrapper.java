package io.github.toberocat.improvedfactions.spigot.wrapper;

import io.github.toberocat.improvedFactions.core.exceptions.TranslatableException;
import io.github.toberocat.improvedFactions.core.exceptions.TranslatableRuntimeException;
import io.github.toberocat.improvedFactions.core.player.CommandSender;
import io.github.toberocat.improvedfactions.spigot.player.SpigotFactionPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BukkitWrapper {
    public static @NotNull CommandSender wrap(@NotNull org.bukkit.command.CommandSender sender) {
        if (sender instanceof Player player)
            return new SpigotFactionPlayer(player);
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
            public void sendException(@NotNull TranslatableException e) {
                sender.sendMessage(e.getTranslationKey()); // ToDo: Fix this
            }

            @Override
            public void sendException(@NotNull TranslatableRuntimeException e) {
                sender.sendMessage(e.getTranslationKey()); // ToDo: Fix this
            }


            @Override
            public String getName() {
                return sender.getName();
            }
        };
    }
}
