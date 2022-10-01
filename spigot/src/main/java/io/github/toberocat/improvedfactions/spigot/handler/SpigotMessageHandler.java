package io.github.toberocat.improvedfactions.spigot.handler;

import io.github.toberocat.improvedFactions.core.handler.MessageHandler;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public class SpigotMessageHandler implements MessageHandler {
    @Override
    public @NotNull String stripColor(@NotNull String text) {
        return ChatColor.stripColor(text);
    }

    @Override
    public @NotNull String format(@NotNull String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
