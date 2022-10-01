package io.github.toberocat.improvedfactions.spigot.handler.message;

import io.github.toberocat.improvedFactions.core.handler.MessageHandler;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.core.utils.Formatting;
import io.github.toberocat.improvedfactions.spigot.placeholder.PlaceholderFormatter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public class SpigotMessageHandler implements MessageHandler {

    private final BiFunction<OfflinePlayer, String, String> processor;

    public SpigotMessageHandler() {
        processor = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI") ?
                new PlaceHolderAPIProcessor() : PlaceholderFormatter::parse;
    }

    @Override
    public @NotNull String stripColor(@NotNull String text) {
        return ChatColor.stripColor(text);
    }

    @Override
    public @NotNull String format(@NotNull String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    @Override
    public @NotNull String format(@NotNull OfflineFactionPlayer<?> player, @NotNull String text) {
        return processor.apply((OfflinePlayer) player.getRaw(), format(text));
    }
}
